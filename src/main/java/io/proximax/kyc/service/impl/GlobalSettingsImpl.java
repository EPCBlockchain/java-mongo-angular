package io.proximax.kyc.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.proximax.kyc.domain.constants.EmailTemplateTypeConstants;
import io.proximax.kyc.domain.globalsettings.EmailTemplate;
import io.proximax.kyc.domain.globalsettings.GlobalSettings;
import io.proximax.kyc.domain.globalsettings.SecuritySettings;
import io.proximax.kyc.repository.GlobalSettingsRepository;
import io.proximax.kyc.service.GlobalSettingsService;
/**
 * Service Implementation for managing Organization.
 */
@Service
public class GlobalSettingsImpl implements GlobalSettingsService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final GlobalSettingsRepository globalSettingsRepository;



    public GlobalSettingsImpl(GlobalSettingsRepository globalSettingsRepository) {
        this.globalSettingsRepository = globalSettingsRepository;
    }



	@Override
	public GlobalSettings save(GlobalSettings settings) {
		
		return null;
	}



	@Override
	public GlobalSettings getSettings() {
		List<GlobalSettings> settings = globalSettingsRepository.findAll();
		return settings.get(0);
	}

	@Override
	public GlobalSettings updadteEmailTemplate(EmailTemplate template) {
		GlobalSettings settings = globalSettingsRepository.findAll().get(0);

		switch(template.getType()) {
			case EmailTemplateTypeConstants.ACTIVATION_EMAIL:
				settings.getEmailSettings().setActivationEmail(template);
				break;
			case EmailTemplateTypeConstants.ACTIVATION_EMAIL_SUCCESS:
				settings.getEmailSettings().setActivationEmailSuccess(template);
				break;
			case EmailTemplateTypeConstants.APPROVAL_EMAIL:
				settings.getEmailSettings().setApprovalEmail(template);
				break;
			case EmailTemplateTypeConstants.CREATION_EMAIL:
				settings.getEmailSettings().setCreationEmail(template);
				break;
			case EmailTemplateTypeConstants.FORM_REQUEST_EMAIL:
				settings.getEmailSettings().setFormRequestEmail(template);
				break;
			case EmailTemplateTypeConstants.FORM_REQUEST_EMAIL_EDIT:
				settings.getEmailSettings().setFormRequestEmailEdit(template);
				break;
			case EmailTemplateTypeConstants.PASSWORD_RESET_EMAIL:
				settings.getEmailSettings().setPasswordResetEmail(template);
				break;
			case EmailTemplateTypeConstants.REJECTION_EMAIL:
				settings.getEmailSettings().setRejectionEmail(template);
				break;
			case EmailTemplateTypeConstants.STATUS_EMAIL:
				settings.getEmailSettings().setStatusEmail(template);
				break;
			case EmailTemplateTypeConstants.SUBMISSION_RECEIVED_EMAIL:
				settings.getEmailSettings().setSubmissionReceivedEmail(template);
				break;
		}
		globalSettingsRepository.save(settings);
		return settings;
	}

	@Override
	public EmailTemplate getEmailTemplate(String type) {
		GlobalSettings settings = globalSettingsRepository.findAll().get(0);

		switch(type) {
			case EmailTemplateTypeConstants.ACTIVATION_EMAIL:
				return settings.getEmailSettings().getActivationEmail();
				
			case EmailTemplateTypeConstants.ACTIVATION_EMAIL_SUCCESS:
				return settings.getEmailSettings().getActivationEmailSuccess();
				
			case EmailTemplateTypeConstants.APPROVAL_EMAIL:
				return settings.getEmailSettings().getApprovalEmail();
				
			case EmailTemplateTypeConstants.CREATION_EMAIL:
				return settings.getEmailSettings().getCreationEmail();
				
			case EmailTemplateTypeConstants.FORM_REQUEST_EMAIL:
				return settings.getEmailSettings().getFormRequestEmail();
				
			case EmailTemplateTypeConstants.FORM_REQUEST_EMAIL_EDIT:
				return settings.getEmailSettings().getFormRequestEmailEdit();
				
			case EmailTemplateTypeConstants.PASSWORD_RESET_EMAIL:
				return settings.getEmailSettings().getPasswordResetEmail();
				
			case EmailTemplateTypeConstants.REJECTION_EMAIL:
				return settings.getEmailSettings().getRejectionEmail();
				
			case EmailTemplateTypeConstants.STATUS_EMAIL:
				return settings.getEmailSettings().getStatusEmail();
				
			case EmailTemplateTypeConstants.SUBMISSION_RECEIVED_EMAIL:
				return settings.getEmailSettings().getSubmissionReceivedEmail();	
			default:
				return null;	
		}
	}

	@Override
	public GlobalSettings updateSecurity(SecuritySettings securitySettings) {
		GlobalSettings settings = globalSettingsRepository.findAll().get(0);
		settings.setSecuritySettings(securitySettings);
		globalSettingsRepository.save(settings);
		return settings;
	}
}
