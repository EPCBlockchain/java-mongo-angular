package io.proximax.kyc.web.rest;

import javax.servlet.http.HttpServletRequest;

import com.codahale.metrics.annotation.Timed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.proximax.kyc.config.ApplicationProperties;
import io.proximax.kyc.domain.globalsettings.EmailTemplate;
import io.proximax.kyc.domain.globalsettings.GlobalSettings;
import io.proximax.kyc.domain.globalsettings.SecuritySettings;
import io.proximax.kyc.security.AuthoritiesConstants;
import io.proximax.kyc.service.GlobalSettingsService;

@RestController
@RequestMapping("/api/settings")
public class GlobalSettingsResource {

    private final Logger log = LoggerFactory.getLogger(FormResource.class);
    
    private static final String ENTITY_NAME = "form";
    private final ApplicationProperties applicationProperties;
    private final GlobalSettingsService globalSettingsService;

    public GlobalSettingsResource(ApplicationProperties applicationProperties, GlobalSettingsService globalSettingsService) {
        this.applicationProperties = applicationProperties;
        this.globalSettingsService = globalSettingsService;        
    }

    @PutMapping("app") 
    @Timed
    @Secured({ AuthoritiesConstants.ORG_ADMIN, AuthoritiesConstants.OBTEAM, AuthoritiesConstants.ADMIN })
    public ResponseEntity<GlobalSettings> updateSettings(@RequestBody GlobalSettings settings, HttpServletRequest request) {
        this.globalSettingsService.save(settings);
        return new ResponseEntity<>(settings, HttpStatus.OK);
    }

    @GetMapping("app")
    @Timed
    public GlobalSettings getSettings() {
        GlobalSettings settings = globalSettingsService.getSettings();
        return settings;
    }

    @PutMapping("email")
    @Timed
    public GlobalSettings updateEmail(@RequestBody EmailTemplate template) {
        GlobalSettings settings = globalSettingsService.updadteEmailTemplate(template);
        return settings;
    }

    @PutMapping("security")
    @Timed
    public GlobalSettings updateSecurity(@RequestBody SecuritySettings securitySettings) {
        GlobalSettings settings = globalSettingsService.updateSecurity(securitySettings);
        return settings;
    }

}