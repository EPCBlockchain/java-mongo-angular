import { Injector } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';

export class CustomHttpInterceptor implements HttpInterceptor {
    requestCounter = 0;
    public spinnerService: NgxSpinnerService;

    constructor(private injector: Injector) {
        this.spinnerService = this.injector.get(NgxSpinnerService);
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        this.requestCounter = this.requestCounter + 1;
        this.spinnerService.show();
        return next.handle(request).pipe(
            tap(
                (event: HttpEvent<any>) => {
                    if (event instanceof HttpResponse) {
                        this.requestCounter--;
                        if (this.requestCounter <= 0) {
                            this.requestCounter = 0;
                            this.spinnerService.hide();
                        }
                    }
                },
                (err: any) => {
                    if (err instanceof HttpErrorResponse) {
                        this.requestCounter--;
                        if (this.requestCounter <= 0) {
                            this.requestCounter = 0;
                            this.spinnerService.hide();
                        }
                    }
                }
            )
        );
    }
}
