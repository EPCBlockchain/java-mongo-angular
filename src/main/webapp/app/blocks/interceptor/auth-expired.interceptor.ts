import { Injector } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginService } from 'app/core/login/login.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

export class AuthExpiredInterceptor implements HttpInterceptor {
    constructor(private injector: Injector) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            tap(
                (event: HttpEvent<any>) => {},
                (err: any) => {
                    if (err instanceof HttpErrorResponse) {
                        if (err.status === 401) {
                            const loginService: LoginService = this.injector.get(LoginService);
                            const router: Router = this.injector.get(Router);
                            const toastr: ToastrService = this.injector.get(ToastrService);
                            loginService.logout();
                            router.navigateByUrl('/account/login');
                            // toastr.warning('Access denied');
                        }
                    }
                }
            )
        );
    }
}
