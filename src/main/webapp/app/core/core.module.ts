import { NgModule, LOCALE_ID } from '@angular/core';
import { DatePipe, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Title } from '@angular/platform-browser';
import locale from '@angular/common/locales/en';
import { ClaimsService } from 'app/core/auth/claims.service';

@NgModule({
    imports: [HttpClientModule],
    exports: [],
    declarations: [],
    providers: [
        ClaimsService,
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'en'
        },
        DatePipe
    ]
})
export class KycCoreModule {
    constructor() {
        registerLocaleData(locale);
    }
}
