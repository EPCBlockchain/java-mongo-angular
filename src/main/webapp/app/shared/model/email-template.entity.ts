import { EmailTemplateTypeEnum } from './enums/email-template-type.enum';

export class EmailTemplateEntity {
    template = '';
    subject = '';
    type: EmailTemplateTypeEnum;
    creationDate: number;
    lastUpdated: number;
}
