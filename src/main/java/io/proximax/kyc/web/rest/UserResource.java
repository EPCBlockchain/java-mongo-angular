package io.proximax.kyc.web.rest;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.jhipster.web.util.ResponseUtil;
import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.config.Constants;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.PermissionTable;
import io.proximax.kyc.domain.User;
import io.proximax.kyc.repository.UserRepository;
import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.service.PermissionTableService;
import io.proximax.kyc.service.UserService;
import io.proximax.kyc.service.dto.UserDTO;
import io.proximax.kyc.service.mail.MailService;
import io.proximax.kyc.web.rest.errors.BadRequestAlertException;
import io.proximax.kyc.web.rest.errors.EmailAlreadyUsedException;
import io.proximax.kyc.web.rest.errors.LoginAlreadyUsedException;
import io.proximax.kyc.web.rest.util.HeaderUtil;
import io.proximax.kyc.web.rest.util.PaginationUtil;

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
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of
 * authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship
 * between User and Authority, and send everything to the client side: there
 * would be no View Model and DTO, a lot less code, and an outer-join which
 * would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities,
 * because people will quite often do relationships with the user, and we don't
 * want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application
 * because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not
 * a real issue as we have by default a second-level cache. This means on the
 * first HTTP call we do the n+1 requests, but then all authorities come from
 * the cache, so in fact it's much better than doing an outer join (which will
 * get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO
 * layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this
 * case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

  private final Logger log = LoggerFactory.getLogger(UserResource.class);

  private final UserService userService;

  private final UserRepository userRepository;

  private final PermissionTableService permissionTableService;

  private final OrganizationService organizationService;

  private final MailService mailService;

  private final ApplicationProperties applicationProperties;

  public UserResource(UserService userService, UserRepository userRepository,
      PermissionTableService permissionTableService, OrganizationService organizationService, MailService mailService, ApplicationProperties applicationProperties) {

    this.userService = userService;
    this.userRepository = userRepository;
    this.permissionTableService = permissionTableService;
    this.organizationService = organizationService;
    this.mailService = mailService;
    this.applicationProperties = applicationProperties;
  }

  /**
   * POST /users : Creates a new user.
   * <p>
   * Creates a new user if the login and email are not already used, and sends an
   * mail with an activation link. The user needs to be activated on creation.
   *
   * @param userDTO the user to create
   * @return the ResponseEntity with status 201 (Created) and with body the new
   *         user, or with status 400 (Bad Request) if the login or email is
   *         already in use
   * @throws URISyntaxException       if the Location URI syntax is incorrect
   * @throws BadRequestAlertException 400 (Bad Request) if the login or email is
   *                                  already in use
   */
  @PostMapping("/users")
  @Timed
  @Secured({ AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN })
  public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO, HttpServletRequest request)
      throws URISyntaxException {
    log.debug("REST request to save User : {}", userDTO);
    User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
    Organization organization;
    if (user.getAuthorities().contains(AuthoritiesConstants.ADMIN)) {
      organization = organizationService.findOne(userDTO.getOrganizationId()).get();
    } else {
      organization = organizationService.findOne(user.getOrganizationId()).get();
    }

    if (userDTO.getId() != null) {
      throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
      // Lowercase the user login before comparing with database
    } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
      throw new LoginAlreadyUsedException();
    } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
      throw new EmailAlreadyUsedException();
    } else {
      User newUser = userService.createUser(userDTO);
      mailService.sendCreationEmail(newUser);
      return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
          .headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin())).body(newUser);
    }
  }

  /**
   * PUT /users : Updates an existing User.
   *
   * @param userDTO the user to update
   * @return the ResponseEntity with status 200 (OK) and with body the updated
   *         user
   * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already
   *                                   in use
   * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already
   *                                   in use
   */
  @PutMapping("/users")
  @Timed
  @Secured({ AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN })
  public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
    log.debug("REST request to update User : {}", userDTO);
    Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
    if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
      throw new EmailAlreadyUsedException();
    }
    existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
    if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
      throw new LoginAlreadyUsedException();
    }
    Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

    return ResponseUtil.wrapOrNotFound(updatedUser,
        HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));
  }

  /**
   * GET /users : get all users.
   *
   * @param pageable the pagination information
   * @return the ResponseEntity with status 200 (OK) and with body all users
   */
  @GetMapping("/users")
  @Timed
  public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable, HttpServletRequest request) {
    User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
    final Page<UserDTO> page;
    if (user.getAuthorities().stream().anyMatch(a -> a.getName().equals(AuthoritiesConstants.ADMIN))) {
      page = userService.getAllManagedUsers(pageable);
    } else {
      page = userService.getAllManagedUsersByOrganizationId(user.getOrganizationId(), pageable);
    }
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @GetMapping("/users-by-login")
  @Timed
  public ResponseEntity<List<UserDTO>> findUsersByLogin(Pageable pageable, @RequestParam("login") String login,
      @RequestHeader("apiKey") String apiKey) {
    Optional<Organization> organization = organizationService.findOneByApiKey(apiKey);
    if (!organization.isPresent()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    Page<UserDTO> page = userService.findAllByLoginLike(pageable, login);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users-by-login");
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  /**
   * @return a string list of the all of the roles
   */
  @GetMapping("/users/authorities")
  @Timed
  @Secured({ AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN })
  public List<String> getAuthorities() {
    return userService.getAuthorities();
  }

  /**
   * GET /users/:login : get the "login" user.
   *
   * @param login the login of the user to find
   * @return the ResponseEntity with status 200 (OK) and with body the "login"
   *         user, or with status 404 (Not Found)
   */
  @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
  @Timed
  public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
    log.debug("REST request to get User : {}", login);
    return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesByLogin(login).map(UserDTO::new));
  }

  /**
   * DELETE /users/:login : delete the "login" User.
   *
   * @param login the login of the user to delete
   * @return the ResponseEntity with status 200 (OK)
   */
  @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
  @Timed
  @Secured({ AuthoritiesConstants.ADMIN, AuthoritiesConstants.ORG_ADMIN })
  public ResponseEntity<Void> deleteUser(@PathVariable String login) {
    log.debug("REST request to delete User: {}", login);
    userService.deleteUser(login);
    return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
  }

  @PostMapping("users/addPermission/{requesterId}/{ownerId}")
  @Timed
  public ResponseEntity<PermissionTable> addPermission(HttpServletRequest request,
      @PathVariable(value = "requesterId", required = true) String requesterId,
      @PathVariable(value = "ownerId", required = true) String ownerId) throws URISyntaxException {
    User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
    Optional<Organization> requester = organizationService.findOne(requesterId);
    if (requester.isPresent() && !permissionTableService
        .findOneByUserIdAndRequesterIdAndOwnerId(user.getId(), requesterId, ownerId).isPresent()) {
      PermissionTable permissionTable = new PermissionTable();
      permissionTable.setOwnerId(ownerId);
      permissionTable.setRequesterId(requesterId);
      permissionTable.setId(user.getId());
      permissionTable.setStatus("PENDING");
      return new ResponseEntity<>(permissionTableService.save(permissionTable), null, HttpStatus.OK);
    }
    return null;
  }

  @PostMapping("users/addPermission/{requesterId}/{userId}/{status}")
  @Timed
  @Secured(AuthoritiesConstants.OBTEAM)
  public ResponseEntity<PermissionTable> updatePermission(HttpServletRequest request,
      @PathVariable(value = "requesterId", required = true) String requesterId,
      @PathVariable(value = "userId", required = true) String userId,
      @PathVariable(value = "status", required = true) String status) throws URISyntaxException {
    User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
    Optional<Organization> requester = organizationService.findOne(requesterId);
    if (requester.isPresent()) {
      Optional<PermissionTable> record = permissionTableService.findOneByUserIdAndRequesterIdAndOwnerId(userId,
          requesterId, user.getOrganizationId());
      if (!record.isPresent())
        return null;
      record.get().setStatus(status);
      return new ResponseEntity<>(permissionTableService.save(record.get()), null, HttpStatus.OK);
    }
    return null;
  }

  /**
   * GET /users/generate-report-excel : Generate excel file.
   *
   * @return the ResponseEntity with status 200 (OK) and the generated excel file.
   */
  @GetMapping("/users/generate-report-excel")
  @Timed
  public ResponseEntity<byte[]> generateAllUsers(Pageable pageable, HttpServletRequest request,
      @RequestParam(name = "apikey", required = false) String apiKey) throws Exception {
    // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
    if (apiKey != null) {
      String appAPIKey = applicationProperties.getSecurity().getApiKey();
      if (!appAPIKey.equals(apiKey)) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
    } else if (request.getRemoteUser() == null) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    String source = "Users";
    String fileName = source + "-" + LocalDate.now() + "-" + LocalTime.now() + ".xlsx";
    final List<User> users;
    User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
    if (user.getAuthorities().stream().anyMatch(a -> a.getName().equals(AuthoritiesConstants.ADMIN))) {
        users  = userService.getAllUsers();
    } else {
        users  = userService.getAllUsersByOrganizationId(user.getOrganizationId());
    }
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet(source);

    // HEADER
    Row rowHead = sheet.createRow(0);
    int colHeadnum = 0;

    Cell cellHeadID = rowHead.createCell(colHeadnum++);
    cellHeadID.setCellValue("ID");

    Cell cellHeadLogin = rowHead.createCell(colHeadnum++);
    cellHeadLogin.setCellValue("Login");

    Cell cellHeadEmail = rowHead.createCell(colHeadnum++);
    cellHeadEmail.setCellValue("Email");

    Cell cellHeadStatus = rowHead.createCell(colHeadnum++);
    cellHeadStatus.setCellValue("Status");

    Cell cellHeadProfile = rowHead.createCell(colHeadnum++);
    cellHeadProfile.setCellValue("Profiles");

    Cell cellHeadCreatedDate = rowHead.createCell(colHeadnum++);
    cellHeadCreatedDate.setCellValue("Created Date");

    Cell cellHeadLastModifiedBy = rowHead.createCell(colHeadnum++);
    cellHeadLastModifiedBy.setCellValue("Last Modified By");

    Cell cellHeadLastModifiedDate = rowHead.createCell(colHeadnum++);
    cellHeadLastModifiedDate.setCellValue("Last Modified Date");

    int childRowNum = 1;
    // Body
    for (User data : users) {
      Row row = sheet.createRow(childRowNum++);
      int childColNum = 0;

      Cell cellOrg = row.createCell(childColNum++);
      cellOrg.setCellValue(data.getId());

      Cell cellLogin = row.createCell(childColNum++);
      cellLogin.setCellValue(data.getLogin());

      Cell cellEmail = row.createCell(childColNum++);
      cellEmail.setCellValue(data.getEmail());

      Cell cellStatus = row.createCell(childColNum++);
      cellStatus.setCellValue((data.getActivated())?"Activated":"Inactive");

      Cell cellProfile = row.createCell(childColNum++);
      cellProfile.setCellValue(data.getAuthorities().toString());

      Cell cellCreated = row.createCell(childColNum++);
      cellCreated.setCellValue(data.getCreatedDate().toString());

      Cell cellLastModifiedBy = row.createCell(childColNum++);
      cellLastModifiedBy.setCellValue(data.getLastModifiedBy());

      Cell cellLastModifiedByDate = row.createCell(childColNum++);
      cellLastModifiedByDate.setCellValue(data.getLastModifiedDate().toString());
    }

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    workbook.write(bout);
    workbook.close();

    byte[] documentContent = bout.toByteArray();
    HttpHeaders headers = new HttpHeaders();
    headers
        .setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName + "");
    headers.setContentLength(documentContent.length);
    return new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
  }

    /**
     * GET  /users/generate-report-pdf : Generate pdf file.
     *
     * @return the ResponseEntity with status 200 (OK) and the generated pdf file.
     */
    @GetMapping("/users/generate-report-pdf")
    @Timed
    public ResponseEntity<byte[]> generateAllUsersPdf(Pageable pageable, HttpServletRequest request, @RequestParam(name = "apikey", required = false) String apiKey) throws Exception {
      // check if api key is not null, if null, check if authenticated, if not throw unauthorize exception
      if (apiKey != null) {
          String appAPIKey = applicationProperties.getSecurity().getApiKey();
          if (!appAPIKey.equals(apiKey)) {
              return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
          }
      } else if (request.getRemoteUser() == null) {
          return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }

      String source = "Users";
      String fileName = source+"-"+LocalDate.now() + "-" + LocalTime.now() + ".pdf";

      final List<User> users;
      User user = userService.getUserWithAuthoritiesByLogin(request.getRemoteUser()).get();
      if (user.getAuthorities().stream().anyMatch(a -> a.getName().equals(AuthoritiesConstants.ADMIN))) {
          users  = userService.getAllUsers();
      } else {
          users  = userService.getAllUsersByOrganizationId(user.getOrganizationId());
      }

      Document document = new Document();
      document.setPageSize(PageSize.A4.rotate());

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PdfWriter.getInstance(document, baos);

      document.open();

      // add a table
      // HEADER
      PdfPTable table = new PdfPTable(8);
      table.setWidthPercentage(100);

      PdfPCell cellHeadID = new PdfPCell(new Phrase("ID"));
      cellHeadID.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadID);

      PdfPCell cellHeadLogin = new PdfPCell(new Phrase("Login"));
      cellHeadLogin.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadLogin);

      PdfPCell cellHeadEmail = new PdfPCell(new Phrase("Email"));
      cellHeadEmail.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadEmail);

      PdfPCell cellHeadStatus = new PdfPCell(new Phrase("Status"));
      cellHeadStatus.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadStatus);

      PdfPCell cellHeadProfile = new PdfPCell(new Phrase("Profiles"));
      cellHeadProfile.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadProfile);

      PdfPCell cellHeadCreatedDate = new PdfPCell(new Phrase("Date Created"));
      cellHeadCreatedDate.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadCreatedDate);

      PdfPCell cellHeadLastModifiedBy = new PdfPCell(new Phrase("Last Modified By"));
      cellHeadLastModifiedBy.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadLastModifiedBy);

      PdfPCell cellHeadLastModifiedDate = new PdfPCell(new Phrase("Last Modified Date"));
      cellHeadLastModifiedDate.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(cellHeadLastModifiedDate);

      // Body
      for (User data : users) {
        table.addCell(data.getId());
        table.addCell(data.getLogin());
        table.addCell(data.getEmail());
        table.addCell((data.getActivated())?"Activated":"Inactive");
        table.addCell(data.getAuthorities().toString());
        table.addCell(data.getCreatedDate().toString());
        table.addCell(data.getLastModifiedBy());
        table.addCell(data.getLastModifiedDate().toString());
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
