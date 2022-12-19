package io.proximax.kyc.web.rest;
/*
import java.net.URI;
import java.net.URISyntaxException;
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
import io.proximax.kyc.config.Constants;
import io.proximax.kyc.domain.Organization;
import io.proximax.kyc.domain.PermissionTable;
import io.proximax.kyc.domain.User;

import io.proximax.kyc.repository.UserRepository;
import io.proximax.kyc.repository.GSApplicationGeneralRepository;

import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.OrganizationService;
import io.proximax.kyc.service.PermissionTableService;

import io.proximax.kyc.service.UserService;
import io.proximax.kyc.service.GSApplicationGeneralService;

import io.proximax.kyc.service.dto.UserDTO;
import io.proximax.kyc.service.mail.MailService;
import io.proximax.kyc.web.rest.errors.BadRequestAlertException;
import io.proximax.kyc.web.rest.errors.EmailAlreadyUsedException;
import io.proximax.kyc.web.rest.errors.LoginAlreadyUsedException;
import io.proximax.kyc.web.rest.util.HeaderUtil;
import io.proximax.kyc.web.rest.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class GSApplicationGeneralResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    // private final UserService userService;

    private final GSApplicationGeneralService gsApplicationGeneralService;

    private final UserRepository userRepository;

    private final GSApplicationGeneralRepository gsagRepository;

    private final PermissionTableService permissionTableService;

    private final OrganizationService organizationService;

    private final MailService mailService;

    public GSApplicationGeneralResource(GSApplicationGeneralService gsApplicationGeneralService, 
                                        GSApplicationGeneralRepository gsagRepository,
                                        PermissionTableService permissionTableService, 
                                        OrganizationService organizationService,
                                        UserRepository userRepository,
                                        MailService mailService) {

        this.gsApplicationGeneralService = gsApplicationGeneralService;
        this.gsagRepository = gsagRepository;
        this.permissionTableService = permissionTableService;
        this.organizationService = organizationService;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

}
*/