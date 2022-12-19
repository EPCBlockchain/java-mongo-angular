package io.proximax.kyc.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FormMailService {
    private final Logger log = LoggerFactory.getLogger(FormMailService.class);
    private final MailGunService mailgunService;

    public FormMailService(MailGunService mailgunService) {
        this.mailgunService = mailgunService;
    }
}
