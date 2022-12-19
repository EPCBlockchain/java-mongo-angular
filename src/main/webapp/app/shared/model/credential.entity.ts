import { ScreeningKeyPairsEntity } from 'app/shared/model/screening-key-pairs.entity';
import { OrganizationSecurity } from 'app/shared/model/organization-security.entity';

export class CredentialEntity {
    public id;
    public name = '';
    public image = '';
    public description = '';
    public code = 'Generated On Creation';
    public organizationId = '';
    public imageFile: any;
    public status = 'DISABLED';
}
