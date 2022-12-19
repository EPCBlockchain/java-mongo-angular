import { JhiEventManager } from 'ng-jhipster';
import { HttpInterceptor, HttpRequest, HttpErrorResponse, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Injector } from '@angular/core';

export class ErrorHandlerInterceptor implements HttpInterceptor {
    constructor(private eventManager: JhiEventManager, private injector: Injector) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const toastr: ToastrService = this.injector.get(ToastrService);
        return next.handle(request).pipe(
            tap(
                (event: HttpEvent<any>) => {},
                (err: any) => {
                    if (err instanceof HttpErrorResponse) {
                        if (!(err.status === 401 && (err.message === '' || (err.url && err.url.indexOf('/api/account') === 0)))) {
                            if (this.eventManager !== undefined) {
                                this.eventManager.broadcast({ name: 'kycApp.httpError', content: err });
                            }
                        }
                    }
                }
            )
        );
    }
}
