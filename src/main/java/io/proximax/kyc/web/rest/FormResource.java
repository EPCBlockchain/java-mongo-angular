package io.proximax.kyc.web.rest;

import com.codahale.metrics.annotation.Timed;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.Authority;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.domain.mongo.Form;
import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.*;
import io.proximax.kyc.service.dto.FormGridDTO;
import io.github.jhipster.web.util.ResponseUtil;

import io.proximax.kyc.service.util.FormUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import io.proximax.kyc.web.rest.util.PaginationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import java.io.ByteArrayOutputStream;
import java.util.*;


/*Thread*/
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/forms")
public class FormResource {

    private final Logger log = LoggerFactory.getLogger(FormResource.class);

    private final SubmittedFormService submittedFormService;
    private final FormService formService;
    private final UserService userService;
    private static final String ENTITY_NAME = "form";
    private final FormUtil formUtil;
    private final ApplicationProperties applicationProperties;
    private final OrganizationService organizationService;

    public FormResource(SubmittedFormService submittedFormService, FormService formService,
            ApplicationProperties applicationProperties, FormUtil formUtil, UserService userService,
            OrganizationService organizationService) {
        this.submittedFormService = submittedFormService;
        this.formService = formService;
        this.userService = userService;
        this.formUtil = formUtil;
        this.applicationProperties = applicationProperties;
        this.organizationService = organizationService;
    }

    /**
     * POST Request for creating new forms
     * 
     * @param form    form to crete
     * @param request current request
     * @return created Form
     */
    @PostMapping("")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.ADMIN })
    public Form createForm(@RequestBody Form form, HttpServletRequest request, Authentication authentication) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        if (!user.getAuthorities().contains(authority)) {
            form.setOrganizationId(user.getOrganizationId());
        }
        return formService.createVersion(form, user);
    }

    /**
     * PUT Request for updating forms
     * 
     * @param form    form to save
     * @param request current request
     * @return updated Form
     */
    @PutMapping("")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public Form updateForm(@RequestBody Form form, HttpServletRequest request) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        if (!user.getAuthorities().contains(authority)) {
            form.setOrganizationId(user.getOrganizationId());
        }
        return formService.createVersion(form, user);
    }

    @GetMapping("by-organization/{organizationId}")
    @Timed
    public ResponseEntity<List<FormGridDTO>> getFormsByOrganization(HttpServletRequest request,
            @PathVariable(value = "organizationId", required = true) String organizationId,
            @RequestParam(name = "filters", required = false) String filters, Pageable page,
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
        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);

        Page<Form> forms = formService.findAllbyFilters(organizationId, jsonFilters, page);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(forms, "/api/forms");
        List<FormGridDTO> formDTOs = forms.getContent().stream().map(form -> new FormGridDTO(form))
                .collect(Collectors.toList());
        return new ResponseEntity<>(formDTOs, headers, HttpStatus.OK);
    }

    /**
     * GEt Request for fetching kyc forms
     * 
     * @param request current request
     * @param filters request filters
     * @param page    page filters
     * @return list of forms
     */
    @GetMapping("")
    @Timed
    public ResponseEntity<List<FormGridDTO>> getForms(HttpServletRequest request,
            @RequestParam(name = "filters", required = false) String filters, Pageable page,
            @RequestParam(name = "apikey", required = false) String apiKey) {
        String organizationId = null;
        String appAPIKey = applicationProperties.getSecurity().getApiKey();
        
        if (apiKey != null) {
            if (!appAPIKey.equals(apiKey)) {
                Optional<Organization> userOrganization = organizationService.findOneByApiKey(apiKey);
                Boolean isPresent = userOrganization.isPresent();
                if (!isPresent) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
                organizationId = userOrganization.get().getId();
            }
        } else if (request.getRemoteUser() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HashMap<String, String> jsonFilters = this.formUtil.convertToHashMapFilters(filters);
        boolean isAdmin = false;

        Optional<User> user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser());
        if (user.isPresent() && organizationId == null) {
            organizationId = user.get().getOrganizationId();
            isAdmin = user.get().getAuthorities().stream()
                    .anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN));
        }

        Page<Form> forms;
        
        if (isAdmin) {
            forms = formService.findAllbyFilters(null, jsonFilters, page);
        } else {
            forms = formService.findAllbyFilters(organizationId, jsonFilters, page);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(forms, "/api/forms");
        List<FormGridDTO> formDTOs = forms.getContent().stream().map(form -> new FormGridDTO(form))
                .collect(Collectors.toList());
        return new ResponseEntity<>(formDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET Request to fetch form detail by id
     * 
     * @param formId form id
     * @return Form
     */
    @GetMapping("/{formId}")
    @Timed
    // @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM })
    public ResponseEntity<Form> getFormDetail(@PathVariable(value = "formId", required = true) String formId,
            @RequestParam(name = "apikey", required = false) String apiKey, HttpServletRequest request) {
        // String organizationId = null;
        // String appAPIKey = applicationProperties.getSecurity().getApiKey();
        // if (apiKey != null) {
        // if (!appAPIKey.equals(apiKey)) {
        // Optional<Organization> userOrganization =
        // organizationService.findOneByApiKey(apiKey);
        // if (!userOrganization.isPresent()) {
        // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // }
        // organizationId = userOrganization.get().getId();
        // }
        // } else if (request.getRemoteUser() == null) {
        // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // }
        
        log.debug("********************** Form Id ********************************");  
        log.debug( formId );  // aqui victor
        log.debug("********************** Form Id ********************************");  
        Optional<Form> form = formService.findOne(formId);
        // if (form.isPresent() && organizationId != null) {
        // if (!organizationId.equals(form.get().getOrganizationId()))
        // return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // }
        return ResponseUtil.wrapOrNotFound(form);
    }

    /**
     * DELETE Request using form id
     * 
     * @param request current request
     * @param formId  form id
     * @return Form
     */
    @DeleteMapping("/{formId}")
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public Boolean updateForm(HttpServletRequest request, @PathVariable(value = "formId") String formId) {
        User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
        return formService.remove(formId, user);
    }

    @GetMapping("/generate-report-excel")
    @Timed
    public ResponseEntity<byte[]> generateReportExcel(HttpServletRequest request,
    @RequestParam(name = "filters", required = false) String filters,
    @RequestParam(name = "apikey", required = false) String apiKey,
    HttpServletResponse response) throws Exception {
      String organizationId = null;
      String appAPIKey = applicationProperties.getSecurity().getApiKey();
      // if (apiKey != null) {
      //     if (!appAPIKey.equals(apiKey)) {
      //         Optional<Organization> userOrganization = organizationService.findOneByApiKey(apiKey);
      //         Boolean isPresent = userOrganization.isPresent();
      //         if (!isPresent) {
      //             return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      //         }
      //         organizationId = userOrganization.get().getId();
      //     }
      // } else if (request.getRemoteUser() == null) {
      //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      // }

        String source = "Forms";
        String fileName = LocalDate.now() + "-" + LocalTime.now() + ".xlsx";

        List<Form> forms = formService.findAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(source);
        System.out.println(forms);


        // header row
          Row rowHead = sheet.createRow(0);
          int colHeadnum = 0;

          Cell cellHeadId = rowHead.createCell(colHeadnum++);
          cellHeadId.setCellValue("ID");

          Cell cellHeadTitle = rowHead.createCell(colHeadnum++);
          cellHeadTitle.setCellValue("Title");

          Cell cellHeadOrganization = rowHead.createCell(colHeadnum++);
          cellHeadOrganization.setCellValue("Organization");

          Cell cellHeadDateCreated = rowHead.createCell(colHeadnum++);
          cellHeadDateCreated.setCellValue("Date Created");

          int childRowNum = 1;

        // data / child row
        for (Form data : forms) {

          Row row = sheet.createRow(childRowNum++);
          int childColNum = 0;

          Cell cellID = row.createCell(childColNum++);
          cellID.setCellValue(data.getId());

          Cell cellTitle = row.createCell(childColNum++);
          cellTitle.setCellValue(data.getTitle());

          Cell cellOrg = row.createCell(childColNum++);
          cellOrg.setCellValue(data.getOrganizationId());

          Cell cellDateCreated = row.createCell(childColNum++);
          cellDateCreated.setCellValue(data.getCreationDate().toString());
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
    @GetMapping("/generate-report-pdf")
    @Timed
    public ResponseEntity<byte[]> generateAllFormsPdf(Pageable pageable, HttpServletRequest request, @RequestParam(name = "apikey", required = false) String apiKey) throws Exception {
      // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
      // if (apiKey != null) {
      //     String appAPIKey = applicationProperties.getSecurity().getApiKey();
      //     if (!appAPIKey.equals(apiKey)) {
      //         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      //     }
      // } else if (request.getRemoteUser() == null) {
      //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      // }

      String source = "Forms";
      String fileName = source+"-"+LocalDate.now() + "-" + LocalTime.now() + ".pdf";

      List<Form> forms = formService.findAll();

      Document document = new Document();
      document.setPageSize(PageSize.A4.rotate());

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, baos);

      document.open();

      // add a table
      // HEADER
      PdfPTable table = new PdfPTable(5);
      table.setWidthPercentage(100);

      PdfPCell cellHeadID = new PdfPCell(new Phrase("ID"));
      cellHeadID.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadID);

      PdfPCell cellHeadOrgName = new PdfPCell(new Phrase("Title"));
      cellHeadOrgName.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadOrgName);

      PdfPCell cellHeadOrgStatus = new PdfPCell(new Phrase("Organization"));
      cellHeadOrgStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadOrgStatus);

      PdfPCell cellHeadCreated = new PdfPCell(new Phrase("Date Created"));
      cellHeadCreated.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadCreated);

      // Body
      for (Form data : forms) {
        table.addCell(data.getId());
        table.addCell(data.getTitle());
        table.addCell(data.getOrganizationId());
        table.addCell(data.getCreationDate().toString());
      }
      // now add this to the document
      document.add(table);

      document.close();

      byte[] documentContent = baos.toByteArray();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename="+fileName+"");
      headers.setContentLength(documentContent.length);
      return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
    }
}
