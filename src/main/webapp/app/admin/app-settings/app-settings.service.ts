import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EmailTemplateTypeEnum } from '../../shared/model/enums/email-template-type.enum';
import { EmailTemplateEntity } from '../../shared/model/email-template.entity';
import { SecuritySettingsEntity } from '../../shared/model/app-settings';

@Injectable({ providedIn: 'root' })
export class AppSettingsService {
    emailTemplate: EmailTemplateEntity;

    constructor(private httpClient: HttpClient) {}

    get() {
        return this.httpClient.get('/api/settings/app');
    }

    updateEmailTemplate(template: EmailTemplateEntity) {
        return this.httpClient.put('/api/settings/email', template);
    }

    updateSecuritSettings(security: SecuritySettingsEntity) {
        return this.httpClient.put('/api/settings/security', security);
    }
}
