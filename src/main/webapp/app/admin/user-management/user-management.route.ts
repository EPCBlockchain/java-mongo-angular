import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { Principal, User, UserService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserManagementDetailComponent } from './detail.component';
import { UserMgmtUpdateComponent } from './user-management-update.component';
import { UserMgmtComponent } from './user-management.component';
import { Route } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class UserResolve implements CanActivate {
    constructor(private principal: Principal) {}

    canActivate() {
        return this.principal.identity().then(account => this.principal.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_ORGANIZATION_ADMIN']));
    }
}

@Injectable({ providedIn: 'root' })
export class UserMgmtResolve implements Resolve<any> {
    constructor(private service: UserService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['login'] ? route.params['login'] : null;
        if (id) {
            return this.service.find(id);
        }
        return new User();
    }
}

export const userMgmtRoute: Route = {
    path: '',
    children: [
        {
            path: '',
            component: UserMgmtComponent,
            resolve: {
                pagingParams: JhiResolvePagingParams
            },
            data: {
                defaultSort: 'id,asc'
            }
        },
        {
            path: 'new',
            component: UserManagementDetailComponent,
            resolve: {
                user: UserMgmtResolve
            }
        },
        {
            path: ':login/view',
            component: UserManagementDetailComponent,
            resolve: {
                user: UserMgmtResolve
            }
        },
        {
            path: ':login/edit',
            component: UserMgmtUpdateComponent,
            resolve: {
                user: UserMgmtResolve
            }
        }
    ]
};
