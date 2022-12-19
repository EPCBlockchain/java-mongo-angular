import { ScreeningKeyPairsEntity } from 'app/shared/model/screening-key-pairs.entity';
import { OrganizationSecurity } from 'app/shared/model/organization-security.entity';

export class OrganizationEntity {
    public id: string = null;
    public name = '';
    public email = '';
    public website = '';
    public apiKey = '';
    public allowedHosts = '';
    public creationDate = '';
    public status = 'inactive';
    public screeningKeys: ScreeningKeyPairsEntity = new ScreeningKeyPairsEntity();
    public security: OrganizationSecurity = new OrganizationSecurity();
    public emailRecipients: { types: string[]; email }[];
    constructor() {
        this.emailRecipients = [];
    }
}
