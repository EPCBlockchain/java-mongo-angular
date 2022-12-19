import { Injectable } from '@angular/core';
import { JhiLanguageService } from 'ng-jhipster';

import { Principal } from '../../core/auth/principal.service';
import { AuthServerProvider } from '../../core/auth/auth-jwt.service';
import { JhiTrackerService } from '../../core/tracker/tracker.service';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LoginService {
    constructor(
        private languageService: JhiLanguageService,
        private principal: Principal,
        private trackerService: JhiTrackerService,
        private authServerProvider: AuthServerProvider
    ) {}

    login(credentials, callback?) {
        const cb = callback || function() {};
        return new Promise((resolve, reject) => {
            this.authServerProvider.login(credentials).subscribe(
                data => {
                    this.principal.identity(true).then(account => {
                        // After the login the language will be changed to
                        // the language selected by the user during his registration
                        if (account !== null) {
                            this.languageService.changeLanguage(account.langKey);
                        }
                        this.trackerService.sendActivity();
                        resolve(data);
                    });
                    return cb();
                },
                err => {
                    this.logout();
                    reject(err);
                    return cb(err);
                }
            );
        });
    }

    loginWithToken(jwt, rememberMe) {
        return this.authServerProvider.loginWithToken(jwt, rememberMe);
    }

    logout(): Observable<any> {
        this.principal.removeIdentity();
        return this.authServerProvider.logout();
    }
}
