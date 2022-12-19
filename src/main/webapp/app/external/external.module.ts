import {NgModule} from '@angular/core';
import {ExternalFormComponent} from 'app/external/external.component';
import {RouterModule} from '@angular/router';
import {externalFormRoute} from 'app/external/external.route';
import {ExternalFormService} from 'app/external/external.service';
import {HttpClientModule} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {FormioModule} from 'angular-formio';
import {KycSharedModule} from 'app/shared';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@NgModule({
    declarations: [ExternalFormComponent],
    imports:
    [CommonModule,
    FormioModule,
    FormsModule,
    ReactiveFormsModule,
    KycSharedModule,
    HttpClientModule,
    RouterModule.forChild(externalFormRoute)],
    providers: [
        ExternalFormService
    ]
})
export class ExternalFormModule {}
