import './vendor.ts';
import { NgModule, Injector } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AngularWebStorageModule, LocalStorageService, SessionStorageService } from 'angular-web-storage';
import { JhiEventManager } from 'ng-jhipster';

import { AuthInterceptor } from './blocks/interceptor/auth.interceptor';
import { AuthExpiredInterceptor } from './blocks/interceptor/auth-expired.interceptor';
import { ErrorHandlerInterceptor } from './blocks/interceptor/errorhandler.interceptor';
import { NotificationInterceptor } from './blocks/interceptor/notification.interceptor';
import { CustomHttpInterceptor } from './blocks/interceptor/custom-http.interceptor';
import { KycSharedModule } from 'app/shared';
import { KycCoreModule } from 'app/core';
import { KycAppRoutingModule } from './app-routing.module';
import { FormsModule } from 'app/forms/forms.module';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { ToastrModule } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { MainComponent } from 'app/main/main.component';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        NgxSpinnerModule,
        ToastrModule.forRoot(),
        // Core Modules
        KycAppRoutingModule,
        KycSharedModule,
        KycCoreModule,
        TranslateModule.forRoot()
    ],
    declarations: [MainComponent],
    providers: [
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
    ],
    schemas: [NO_ERRORS_SCHEMA],
    bootstrap: [MainComponent]
})
export class KycAppModule {}
