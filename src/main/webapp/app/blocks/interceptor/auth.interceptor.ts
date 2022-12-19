import { Observable } from 'rxjs';
import { LocalStorageService, SessionStorageService } from 'angular-web-storage';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';

export class AuthInterceptor implements HttpInterceptor {
    constructor(private localStorage: LocalStorageService, private sessionStorage: SessionStorageService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!request || !request.url || (/^http/.test(request.url) && !(SERVER_API_URL && request.url.startsWith(SERVER_API_URL)))) {
            return next.handle(request);
        }
        const token = this.localStorage.get('authenticationToken') || this.sessionStorage.get('authenticationToken');
        if (!!token) {
            request = request.clone({ setHeaders: { Authorization: 'Bearer ' + token } });
        }
        return next.handle(request);
    }
}
