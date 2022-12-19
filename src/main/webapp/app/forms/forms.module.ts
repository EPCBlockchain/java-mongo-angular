import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormioModule, FormBuilderComponent } from 'angular-formio';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsState } from 'app/forms/forms.route';
import { KycSharedModule } from 'app/shared';
import { FormComponentSet } from 'app/forms/form.component-set';
import { FormService } from 'app/forms/form.service';
import { EmailModalComponent } from 'app/forms/detail/email-modal/email-modal.component';
import { CustomerFormService } from 'app/forms/customer/customer-form.service';
import { ScreeningEmailModalComponent } from 'app/forms/screening/email-modal/email-modal.component';
import { ExternalScreeningModalComponent } from 'app/forms/screening/ext-screening-modal/ex-screening-modal.component';
import { FormSubmissionUtil } from 'app/shared/util/form-submission.util';
import { DeleteModalComponent } from 'app/forms/detail/delete-modal/delete-modal.component';
import { DeleteModalService } from 'app/forms/detail/delete-modal/delete-modal.service';
import { RemarksModalComponent } from 'app/forms/screening/remarks-modal/remarks-modal.component';
import { RemarksModalService } from 'app/forms/screening/remarks-modal/remarks-modal.service';
import { SubmissionModalComponent } from 'app/forms/customer/request/submission-modal/submission-modal.component';
import { SubmissionModalService } from 'app/forms/customer/request/submission-modal/submission-modal.service';
import { OrganizationService } from 'app/admin/organizations/organizations.service';
import { ImportExportModalComponent } from './detail/import-export/import-export-modal.component';
import { CustomGridModule } from '../shared/grid/custom-grid.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {CustomTypeAheadModule} from "app/shared/typeahead/typeahead.module"
import {CredentialModalComponent} from "app/forms/detail/credentials/credential-modal.component";
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import { allIcons } from 'ng-bootstrap-icons/icons';
import { saveAs } from 'file-saver';
import {  ReactiveFormsModule } from '@angular/forms';

@NgModule({
    imports: [
        NgbModule,
        CustomGridModule,
        CommonModule,
        FormioModule,
        KycSharedModule,
        RouterModule.forChild(FormsState),
        CustomTypeAheadModule,
        BootstrapIconsModule.pick(allIcons),
        ReactiveFormsModule
     
    ],
    declarations: FormComponentSet,
    providers: [
        FormService,
        CustomerFormService,
        FormSubmissionUtil,
        DeleteModalService,
        RemarksModalService,
        SubmissionModalService,
        OrganizationService
    ],
    entryComponents: [
        FormBuilderComponent,
        EmailModalComponent,
        ScreeningEmailModalComponent,
        ExternalScreeningModalComponent,
        DeleteModalComponent,
        RemarksModalComponent,
        SubmissionModalComponent,
        ImportExportModalComponent,
        CredentialModalComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports:[
        BootstrapIconsModule
    ]
})
export class FormsModule {}
