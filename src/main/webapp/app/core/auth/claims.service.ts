import { LocalStorageService } from 'angular-web-storage';
import { Injectable } from '@angular/core';
import { AccountEntity, OrganizationEntity } from 'app/shared/model';

@Injectable()
export class ClaimsService {
    private readonly claimName = 'u_claim';

    constructor(private $localstorage: LocalStorageService) {}

    public account(): AccountEntity {
        return this.$localstorage.get(this.claimName) || new AccountEntity();
    }

    public create(json: any): void {
        this.$localstorage.set(this.claimName, json);
    }

    public destroy(): void {
        this.$localstorage.remove(this.claimName);
    }

    public updateAgreement(): void {
        const account = this.account();
        account.agreementAccepted = true;
        this.create(account);
    }

    public agreeementAccepted() {
        return this.account().agreementAccepted;
    }

    public isInRole(role: string): boolean {
        return this.account().authorities.includes(role);
    }

    public userId(): string {
        if (this.account) {
            return this.account().id;
        }
        return null;
    }

    public accountType(): string {
        return this.account().accountType;
    }

    public organization(): OrganizationEntity {
        if (this.account) {
            return this.account().organization;
        }
        return null;
    }
}
