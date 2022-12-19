import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KycSharedModule } from 'app/shared';

import {
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    LoginComponent,
    accountState
} from './';
import { AccountMasterComponent } from 'app/account/master/account-master.component';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
    imports: [
        KycSharedModule,
        RouterModule.forChild(accountState),
        TranslateModule
    ],
    declarations: [
        AccountMasterComponent,
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent,
        LoginComponent
    ],
    exports: [RouterModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AccountModule {}
