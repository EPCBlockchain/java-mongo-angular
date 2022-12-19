import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router"
import {Injectable} from "@angular/core"
import {Observable} from "rxjs"
import {CredentialsService} from "app/credentials/credentials.service"
import {CredentialEntity} from "app/shared/model/credential.entity"
import {OrganizationEntity} from "app/shared/model"

@Injectable()
export class CredentialDetailResolver implements Resolve<CredentialEntity> {
    constructor(private service: CredentialsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any>|Promise<any>|any {
        return this.service.getById(route.paramMap.get('id'))
    }
}
