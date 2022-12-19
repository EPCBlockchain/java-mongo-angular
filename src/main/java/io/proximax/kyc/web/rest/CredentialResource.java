package io.proximax.kyc.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.*;
import io.proximax.kyc.domain.constants.FormStatusConstants;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.domain.mongo.KYCSubmission;
import io.proximax.kyc.domain.screening.keys.ShuftiProKeyPair;
import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.*;
import io.proximax.kyc.service.dto.FormGridDTO;
import io.proximax.kyc.service.dto.KYCSubmissionDTO;
import io.proximax.kyc.service.storage.StorageService;
import io.proximax.kyc.service.util.FormUtil;
import io.proximax.kyc.web.rest.util.HeaderUtil;
import io.proximax.kyc.web.rest.util.PaginationUtil;
import io.proximax.kyc.web.rest.vm.LoggerVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/api/credentials")
public class CredentialResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final CredentialService credentialService;
    private final ApplicationProperties applicationProperties;
    private final OrganizationService organizationService;
    private final SubmissionService submissionService;

    public CredentialResource(CredentialService credentialService, OrganizationService organizationService,
                              ApplicationProperties applicationProperties, SubmissionService submissionService) {
        this.credentialService = credentialService;
        this.organizationService = organizationService;
        this.submissionService = submissionService;
        this.applicationProperties = applicationProperties;
    }

    @PostMapping("")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.ADMIN })
    public Credential create(@RequestBody Credential credential, HttpServletRequest request, Authentication authentication) {
        Credential dbCredential = this.credentialService.save(credential);
        dbCredential.setImage(getFileURL(dbCredential.getImage()));
        return dbCredential;
    }

//    @PutMapping("")
//    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
//    public ResponseEntity<Credential> update(@RequestBody Credential credential, HttpServletRequest request) throws URISyntaxException {
//        Optional<Credential> dbCredential = this.credentialService.findOne(credential.getId());
//        if (dbCredential.isPresent()) {
//            Credential credential1 = dbCredential.get();
//            credential1.setCode(credential.getCode());
//            credential1.setDescription(credential.getDescription());
//            credential1.setStatus(credential.getStatus());
//            credential1.setImageFile(credential.getImageFile());
//            Credential result = credentialService.save(credential1);
//            result.setImage(getFileURL(result.getImage()));
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }
//        return ResponseUtil.wrapOrNotFound(dbCredential);
//    }

    @GetMapping("")
    @Timed
    public ResponseEntity<List<Credential>> getAllCredentials(Pageable pageable,
                                      HttpServletRequest request,
                                      @RequestParam(name = "apikey", required = false) String apiKey) {
        log.debug("{}", request.getRemoteUser() == null);
        if (apiKey != null) {
            log.debug(apiKey);
            String appAPIKey = applicationProperties.getSecurity().getApiKey();
            if (!appAPIKey.equals(apiKey)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else if (request.getRemoteUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Page<Credential> page = credentialService.page(pageable);
        List<Credential> contents = page.getContent();
        contents.stream().forEach(content -> {
            content.setImage(getFileURL(content.getImage()));
        });
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credentials");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("auto-complete")
    @Timed
    public List<Credential> findAll() {
        // need to optimize
        return this.credentialService.findAll();
    }


    @GetMapping("{id}")
    @Timed
    public ResponseEntity<Credential> getCredential(@PathVariable String id, HttpServletRequest request,
                                                        @RequestParam(name = "apikey", required = false) String apiKey) {
        String appAPIKey = applicationProperties.getSecurity().getApiKey();

        if (apiKey != null) {
            if (!appAPIKey.equals(apiKey)) {
                Optional<Organization> userOrganization = organizationService.findOneByApiKey(apiKey);
                if (!userOrganization.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        } else if (request.getRemoteUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Credential> credential = credentialService.findOne(id);
        credential.ifPresent(value -> value.setImage(getFileURL(value.getImage())));
        return ResponseUtil.wrapOrNotFound(credential);
    }

    @DeleteMapping("{id}")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Optional<Credential> credential = credentialService.findOne(id);
        if (!credential.isPresent()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityDeletionAlert("credentials", id)).build();
        }
        organizationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("credentials", id)).build();
    }


    @GetMapping("validate-user/{userId}/{code}")
    @Timed
    public ResponseEntity validateUser(@PathVariable String userId, @PathVariable String code,
                                         HttpServletRequest request,
                                         @RequestParam(name = "apikey", required = false) String apiKey) {
        String appAPIKey = applicationProperties.getSecurity().getApiKey();

        if (apiKey != null) {
            if (!appAPIKey.equals(apiKey)) {
                log.debug("{} {}", apiKey, appAPIKey);
                Optional<Organization> userOrganization = organizationService.findOneByApiKey(apiKey);
                if (!userOrganization.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
        } else if (request.getRemoteUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Credential> credential = credentialService.findByCode(code);

        if (!credential.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid User Credential");
        }

        KYCSubmissionDTO submission = submissionService.findByUserIdAndCredentialId(
            userId,
            credential.get().getId()
        );

        if (submission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission Not Found");
        }

        SubmissionCredential dto = new SubmissionCredential();
        dto.setCredential(credential.get());
        dto.setSubmission(submission);

        return new ResponseEntity<SubmissionCredential>(dto, HttpStatus.OK);
    }

    private String getFileURL(String hash) {
        return applicationProperties.getBaseHost() + "/api/data/file/" + hash;
    }

    /**
     * GET  /credentials/generate-report-excel : Generate excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and the generated excel file.
     */
    @GetMapping("/generate-report-excel")
    @Timed
    public ResponseEntity<byte[]> generateAllCredentials(Pageable pageable, HttpServletRequest request, @RequestParam(name = "apikey", required = false) String apiKey) throws Exception {
      // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
      if (apiKey != null) {
          String appAPIKey = applicationProperties.getSecurity().getApiKey();
          if (!appAPIKey.equals(apiKey)) {
              return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
          }
      } else if (request.getRemoteUser() == null) {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }

      String source = "Credentials";
      String fileName = source+"-"+LocalDate.now() + "-" + LocalTime.now() + ".xlsx";

      List<Credential> creds = credentialService.findAll();
      XSSFWorkbook workbook = new XSSFWorkbook();
      XSSFSheet sheet = workbook.createSheet(source);

      // HEADER
      Row rowHead = sheet.createRow(0);
      int colHeadnum = 0;

      Cell cellHeadID = rowHead.createCell(colHeadnum++);
      cellHeadID.setCellValue("ID");

      Cell cellHeadOrgName = rowHead.createCell(colHeadnum++);
      cellHeadOrgName.setCellValue("Name");

      Cell cellHeadCreated = rowHead.createCell(colHeadnum++);
      cellHeadCreated.setCellValue("Date Created");

      int childRowNum = 1;
      // Body
      for (Credential data : creds) {

        Row row = sheet.createRow(childRowNum++);
        int childColNum = 0;

        Cell cellOrg = row.createCell(childColNum++);
        cellOrg.setCellValue(data.getId());

        Cell cellName = row.createCell(childColNum++);
        cellName.setCellValue(data.getName());

        Cell cellCreated = row.createCell(childColNum++);
        cellCreated.setCellValue(data.getCreationDate().toString());
      }

      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      workbook.write(bout);
      workbook.close();

      byte[] documentContent = bout.toByteArray();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+fileName+"");
      headers.setContentLength(documentContent.length);
      return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
    }


    /**
     * GET  /credentials/generate-report-pdf : Generate pdf file.
     *
     * @return the ResponseEntity with status 200 (OK) and the generated pdf file.
     */
    @GetMapping("/generate-report-pdf")
    @Timed
    public ResponseEntity<byte[]> generateAllCredentialsPdf(Pageable pageable, HttpServletRequest request, @RequestParam(name = "apikey", required = false) String apiKey) throws Exception {
      // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
      if (apiKey != null) {
          String appAPIKey = applicationProperties.getSecurity().getApiKey();
          if (!appAPIKey.equals(apiKey)) {
              return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
          }
      } else if (request.getRemoteUser() == null) {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }

      String source = "Credentials";
      String fileName = source+"-"+LocalDate.now() + "-" + LocalTime.now() + ".pdf";

      List<Credential> creds = credentialService.findAll();

      Document document = new Document();
      document.setPageSize(PageSize.A4.rotate());

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, baos);

      document.open();

      // add a table
      // HEADER
      PdfPTable table = new PdfPTable(3);
      table.setWidthPercentage(100);

      PdfPCell cellHeadID = new PdfPCell(new Phrase("ID"));
      cellHeadID.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadID);

      PdfPCell cellHeadOrgName = new PdfPCell(new Phrase("Name"));
      cellHeadOrgName.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadOrgName);

      PdfPCell cellHeadCreated = new PdfPCell(new Phrase("Date Created"));
      cellHeadCreated.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadCreated);

      // Body
      for (Credential data : creds) {
        table.addCell(data.getId());
        table.addCell(data.getName());
        table.addCell(data.getCreationDate().toString());
      }
      // now add this to the document
      document.add(table);

      document.close();

      byte[] documentContent = baos.toByteArray();
      HttpHeaders headers = new HttpHeaders();
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+fileName+"");
      headers.setContentLength(documentContent.length);
      return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
    }
}
