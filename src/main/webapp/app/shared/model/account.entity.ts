import { OrganizationEntity } from './organization.entity';

export class AccountEntity {
    public login: string;
    public firstName: string;
    public lastName: string;
    public id: string;
    public organization: OrganizationEntity;
    public authorities: string[];
    public accountType: string;
    public agreementAccepted: boolean;
    public email: string;
    constructor() {}
}
