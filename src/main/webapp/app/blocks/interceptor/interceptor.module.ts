import { NgModule, Injector } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';
import { LocalStorageService, SessionStorageService } from 'angular-web-storage';
import { AuthExpiredInterceptor } from './auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './errorhandler.interceptor';
import { JhiEventManager } from 'ng-jhipster';
import { NotificationInterceptor } from './notification.interceptor';
import { CustomHttpInterceptor } from './custom-http.interceptor';
import { NgxSpinnerService, NgxSpinnerModule } from 'ngx-spinner';

const providers = [
    NgxSpinnerService,
    LocalStorageService,
    SessionStorageService,
    {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthInterceptor,
        multi: true,
        deps: [LocalStorageService, SessionStorageService]
    },
    {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthExpiredInterceptor,
        multi: true,
        deps: [Injector]
    },
    {
        provide: HTTP_INTERCEPTORS,
        useClass: ErrorHandlerInterceptor,
        multi: true,
        deps: [JhiEventManager, Injector]
    },
    {
        provide: HTTP_INTERCEPTORS,
        useClass: NotificationInterceptor,
        multi: true,
        deps: [Injector]
    },
    {
        provide: HTTP_INTERCEPTORS,
        useClass: CustomHttpInterceptor,
        multi: true,
        deps: [Injector]
    }
];
@NgModule({
    imports: [NgxSpinnerModule],
    providers
})
export class InterceptorModule {}
