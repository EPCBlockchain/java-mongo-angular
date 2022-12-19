package io.proximax.kyc.service;

import io.proximax.kyc.domain.globalsettings.EmailTemplate;
import io.proximax.kyc.domain.globalsettings.GlobalSettings;
import io.proximax.kyc.domain.globalsettings.SecuritySettings;

public interface GlobalSettingsService {
    GlobalSettings save(GlobalSettings settings);
    GlobalSettings getSettings();
	GlobalSettings updadteEmailTemplate(EmailTemplate template);
	EmailTemplate getEmailTemplate(String submissionReceivedEmail);
	GlobalSettings updateSecurity(SecuritySettings securitySettings);
}
