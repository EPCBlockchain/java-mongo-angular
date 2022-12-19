import { Injectable, isDevMode } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

import { Principal } from '../';
import { StateStorageService } from './state-storage.service';
import { ToastrService } from 'ngx-toastr';

@Injectable({ providedIn: 'root' })
export class UserRouteAccessService implements CanActivate {
    constructor(
        private router: Router,
        private principal: Principal,
        private stateStorageService: StateStorageService,
        private toastr: ToastrService
    ) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Promise<boolean> {
        const authorities = route.data['authorities'];
        if (authorities) {
            authorities.push('ROLE_ADMIN');
        }
        return this.checkLogin(authorities, state.url);
    }

    checkLogin(authorities: string[], url: string): Promise<boolean> {
        const principal = this.principal;
        return Promise.resolve(
            principal.identity().then(account => {
                const urlHex = Buffer.from(url).toString('hex');
                if (account && authorities) {
                    return principal.hasAnyAuthority(authorities).then(response => {
                        if (response) {
                            return true;
                        }
                        if (isDevMode()) {
                            console.error('User has not any of required authorities: ', authorities);
                            // this.toastr.warning('Invalid Credentials. Please login to continue');
                            this.router.navigateByUrl('/account/login?redirect=' + urlHex);
                        }
                        return false;
                    });
                } else if (account) {
                    return true;
                }
                if (url) {
                    // this.toastr.warning('Invalid Credentials. Please login to continue');
                    this.router.navigateByUrl('/account/login?redirect=' + urlHex);
                }
                return false;
            })
        );
    }
}
