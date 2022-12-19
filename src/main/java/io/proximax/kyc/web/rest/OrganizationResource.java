package io.proximax.kyc.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.screening.keys.ShuftiProKeyPair;
import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.service.UserService;
import io.proximax.kyc.service.util.EncryptionUtil;
import io.proximax.kyc.web.rest.errors.BadRequestAlertException;
import io.proximax.kyc.web.rest.util.HeaderUtil;
import io.proximax.kyc.web.rest.util.PaginationUtil;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class    OrganizationResource {

	private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

	private static final String ENTITY_NAME = "organizations";

	private final UserService userService;

	private final OrganizationService organizationService;
	private final EncryptionUtil encryptionUtil;
    private final ApplicationProperties applicationProperties;

    public OrganizationResource(OrganizationService organizationService, UserService userService, EncryptionUtil encryptionUtil,
                                ApplicationProperties applicationProperties) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.encryptionUtil = encryptionUtil;
        this.applicationProperties = applicationProperties;
    }

	/**
     * POST  /organizations : Create a new organization.
     *
     * @param organization the organization to create
     * @return the ResponseEntity with status 201 (Created) and with body the new organization, or with status 400 (Bad Request) if the organization has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/organizations")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN})
    public Organization createOrganization(@RequestBody Organization organization, HttpServletRequest request) throws URISyntaxException {
        if (organization.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (organization.getScreeningKeys() != null) {
            if (organization.getScreeningKeys().getShuftiPro() != null) {
                ShuftiProKeyPair keyPair = organization.getScreeningKeys().getShuftiPro();
                keyPair.setClientId(this.encryptionUtil.encrypt(keyPair.getClientId()));
                keyPair.setSecretKey(this.encryptionUtil.encrypt(keyPair.getSecretKey()));
                organization.getScreeningKeys().setShuftiPro(keyPair);
            }
        }
        organization.setCreationDate(Instant.now());
        if (!this.applicationProperties.getSecurity().getUsingGlobal()) {
            String apiKey = UUID.randomUUID().toString();
            organization.getSecurity().setApiKey(apiKey);
        }
        organizationService.save(organization);
        return organization;
    }

    /**
     * POST  /udorganizations : Updates an existing organization.
     *
     * @param organization the organization to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated organization,
     * or with status 400 (Bad Request) if the organization is not valid,
     * or with status 500 (Internal Server Error) if the organization couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/organizations")
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN})
    public ResponseEntity<Organization> updateOrganization(@RequestBody Organization organization, HttpServletRequest request) throws URISyntaxException {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        Optional<Organization> dbOrganization = organizationService.findOne(organization.getId());
        if (dbOrganization.isPresent()) {
            Organization org = dbOrganization.get();
            if (organization.getScreeningKeys() != null) {
                if (organization.getScreeningKeys().getShuftiPro() != null) {
                    ShuftiProKeyPair keyPair = organization.getScreeningKeys().getShuftiPro();
                    keyPair.setClientId(this.encryptionUtil.encrypt(keyPair.getClientId()));
                    keyPair.setSecretKey(this.encryptionUtil.encrypt(keyPair.getSecretKey()));
                    org.getScreeningKeys().setShuftiPro(keyPair);
                }
            }
            if (org.getSecurity().getApiKey() == null && !this.applicationProperties.getSecurity().getUsingGlobal()) {
                String apiKey = UUID.randomUUID().toString();
                org.getSecurity().setApiKey(apiKey);
            }
            if (organization.getSecurity().getAllowedHosts() != null) {
                org.getSecurity().setAllowedHosts(organization.getSecurity().getAllowedHosts());
            }
            if (organization.getSecurity().getPostBackUrl() != null) {
                org.getSecurity().setPostBackUrl(organization.getSecurity().getPostBackUrl());
            }
            if (organization.getSecurity().getValidationUrl() != null) {
                org.getSecurity().setValidationUrl(organization.getSecurity().getValidationUrl());
            }
            org.setStatus(organization.getStatus());
            org.setName(organization.getName());
            org.setEmail(organization.getEmail());
            org.setEmailRecipients(organization.getEmailRecipients());
            org.setWebsite(organization.getWebsite());
            Organization result = organizationService.save(org);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return ResponseUtil.wrapOrNotFound(dbOrganization);
    }

    /**
     * GET  /organizations : get all the organizations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizations in body
     */
    // @GetMapping("/organizations")
    // @Timed
    // public List<Organization> getAllOrganizations() {
    //     log.debug("REST request to get all Organizations");
    //     return organizationService.findAll();
    // }

    @GetMapping("/organizations")
    @Timed
    public ResponseEntity<List<Organization>> getAllOrganizations(Pageable pageable,
                                                                  HttpServletRequest request,
                                                                  @RequestParam(name = "apikey", required = false) String apiKey) {
//        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
        log.debug("marker-1");
        if (apiKey != null) {
            String appAPIKey = applicationProperties.getSecurity().getApiKey();
            if (!appAPIKey.equals(apiKey)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else if (request.getRemoteUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        log.debug("marker");

        Page<Organization> page = organizationService.getAllOrganizations(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/organizations");
        List<Organization> orgs = page.getContent();
        orgs.forEach((organization) -> {
            organization.setScreeningKeys(null);
        });
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  / : get an organization by id.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of organizations in body
     */

    @GetMapping("/organizations/{id}")
    @Timed
    public ResponseEntity<Organization> getOrganization(@PathVariable String id,
                                                        HttpServletRequest request,
                                                        @RequestParam(name = "apikey", required = false) String apiKey){

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
        Optional<Organization> organization;
        if (apiKey == null || appAPIKey.equals(apiKey)) {
            organization = organizationService.findOne(id);
        } else {
            organization = organizationService.findOneByApiKey(apiKey);
        }
        if (organization.get().getScreeningKeys() != null) {
            if (organization.get().getScreeningKeys().getShuftiPro() != null) {
                ShuftiProKeyPair keyPair = organization.get().getScreeningKeys().getShuftiPro();
                keyPair.setClientId(this.encryptionUtil.decrypt(keyPair.getClientId()));
                keyPair.setSecretKey(this.encryptionUtil.decrypt(keyPair.getSecretKey()));
                organization.get().getScreeningKeys().setShuftiPro(keyPair);
            }
        }
        if (this.applicationProperties.getSecurity().getUsingGlobal()) {
            organization.get().getSecurity().setApiKey("API Key is set on application level");
        }
        return ResponseUtil.wrapOrNotFound(organization);
    }


    /**
     * DELETE  /organizations/draft/:id : delete the "id" organization.
     *
     * @param id the id of the organization to delete
     * @return the ResponseEntity with status 200 (OK)
     * /api/organizations/${organizationId}/delete
     */
    @DeleteMapping("/organizations/{id}/delete")
    @Timed
    public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
        Optional<Organization> organization = organizationService.findOne(id);
        if (organization.get() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
        }
        organizationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * GET  /organizations/generate-report-excel : Generate excel file.
     *
     * @return the ResponseEntity with status 200 (OK) and the generated excel file.
     */
    @GetMapping("/organizations/generate-report-excel")
    @Timed
    public ResponseEntity<byte[]> generateAllOrganizations(Pageable pageable, HttpServletRequest request, @RequestParam(name = "apikey", required = false) String apiKey) throws Exception {
      // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
      if (apiKey != null) {
          String appAPIKey = applicationProperties.getSecurity().getApiKey();
          if (!appAPIKey.equals(apiKey)) {
              return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
          }
      } else if (request.getRemoteUser() == null) {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }


      String source = "Organization";
      String fileName = source+"-"+LocalDate.now() + "-" + LocalTime.now() + ".xlsx";

      List<Organization> orgs = organizationService.findAll();
      XSSFWorkbook workbook = new XSSFWorkbook();
      XSSFSheet sheet = workbook.createSheet(source);

      // HEADER
      Row rowHead = sheet.createRow(0);
      int colHeadnum = 0;

      Cell cellHeadID = rowHead.createCell(colHeadnum++);
      cellHeadID.setCellValue("ID");

      Cell cellHeadOrgName = rowHead.createCell(colHeadnum++);
      cellHeadOrgName.setCellValue("Name");

      Cell cellHeadStatus = rowHead.createCell(colHeadnum++);
      cellHeadStatus.setCellValue("Status");

      Cell cellHeadCreated = rowHead.createCell(colHeadnum++);
      cellHeadCreated.setCellValue("Date Created");

      int childRowNum = 1;
      // Body
      for (Organization data : orgs) {

        Row row = sheet.createRow(childRowNum++);
        int childColNum = 0;

        Cell cellOrg = row.createCell(childColNum++);
        cellOrg.setCellValue(data.getId());

        Cell cellName = row.createCell(childColNum++);
        cellName.setCellValue(data.getName());

        Cell cellStatus = row.createCell(childColNum++);
        cellStatus.setCellValue(data.getStatus());

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
     * GET  /organizations/generate-report-pdf : Generate pdf file.
     *
     * @return the ResponseEntity with status 200 (OK) and the generated pdf file.
     */
    @GetMapping("/organizations/generate-report-pdf")
    @Timed
    public ResponseEntity<byte[]> generateAllOrganizationPdf(Pageable pageable, HttpServletRequest request, @RequestParam(name = "apikey", required = false) String apiKey) throws Exception {
      // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
      if (apiKey != null) {
          String appAPIKey = applicationProperties.getSecurity().getApiKey();
          if (!appAPIKey.equals(apiKey)) {
              return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
          }
      } else if (request.getRemoteUser() == null) {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }

      String source = "Organizations";
      String fileName = source+"-"+LocalDate.now() + "-" + LocalTime.now() + ".pdf";

      List<Organization> orgs = organizationService.findAll();

      Document document = new Document();
      document.setPageSize(PageSize.A4.rotate());

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, baos);

      document.open();

      // add a table
      // HEADER
      PdfPTable table = new PdfPTable(4);
      table.setWidthPercentage(100);

      PdfPCell cellHeadID = new PdfPCell(new Phrase("ID"));
      cellHeadID.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadID);

      PdfPCell cellHeadOrgName = new PdfPCell(new Phrase("Name"));
      cellHeadOrgName.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadOrgName);

      PdfPCell cellHeadOrgStatus = new PdfPCell(new Phrase("Status"));
      cellHeadOrgStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadOrgStatus);

      PdfPCell cellHeadCreated = new PdfPCell(new Phrase("Date Created"));
      cellHeadCreated.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadCreated);

      // Body
      for (Organization data : orgs) {
        table.addCell(data.getId());
        table.addCell(data.getName());
        table.addCell(data.getStatus());
        table.addCell(data.getCreationDate().toString());
      }
      // now add this to the document
      document.add(table);

      document.close();

      byte[] documentContent = baos.toByteArray();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType("application/pdf"));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+fileName+"");
      headers.setContentLength(documentContent.length);
      return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
    }
}
