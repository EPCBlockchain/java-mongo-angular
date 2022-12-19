import { FormDashboardComponent } from 'app/forms/dashboard/dashboard.component';
import { FormListComponent } from 'app/forms/list/list.component';
import { FormDetailComponent } from 'app/forms/detail/detail.component';
import { CustomerSubmissionsComponent } from 'app/forms/customer/submissions/customer-submissions.component';
import { PendingRequestsComponent } from 'app/forms/customer/pending/pending-requests.component';
import { FormRequestComponent } from 'app/forms/customer/request/form-request.component';
// import { ScreeningComponent } from 'app/forms/obteam/submissions/screening/screening.component';
import { EmailModalComponent } from 'app/forms/detail/email-modal/email-modal.component';
import { CustomerDetailFormComponent } from 'app/forms/customer/detail/detail-form.component';
import { SubmissionDetailComponent } from 'app/forms/submissions/detail/submission-detail.component';
import { ScreeningEmailModalComponent } from 'app/forms/screening/email-modal/email-modal.component';
import { FormSubmissionsComponent } from 'app/forms/submissions/submissions.component';
import { ScreeningComponent } from 'app/forms/screening/screening.component';
// import { Screening2Component } from 'app/forms/screening2/screening2.component';
import { ExternalScreeningModalComponent } from 'app/forms/screening/ext-screening-modal/ex-screening-modal.component';
import { IdentityMindScreeningComponent } from 'app/forms/screening/ext-screening/identity-mind/identity-mind.component';
import { JumioScreeningComponent } from 'app/forms/screening/ext-screening/jumio/jumio.component';
import { ShuftiScreeningComponent } from 'app/forms/screening/ext-screening/shufti/shufti.component';
import { ThomsonReutersScreeningComponent } from 'app/forms/screening/ext-screening/thomson-reuters/thomson-reuters.component';
import { ResubmissionRequestComponent } from 'app/forms/customer/resubmission-request/resubmission-request.components';
import { CustomGridComponent } from 'app/shared/grid/custom-grid.component';
import { FilterOptionsComponent } from 'app/shared/grid/filter-options.component';
import { DeleteModalComponent } from 'app/forms/detail/delete-modal/delete-modal.component';
import { RemarksModalComponent } from 'app/forms/screening/remarks-modal/remarks-modal.component';
import { SubmissionModalComponent } from 'app/forms/customer/request/submission-modal/submission-modal.component';
import { ImportExportModalComponent } from './detail/import-export/import-export-modal.component';
import {CredentialModalComponent} from "app/forms/detail/credentials/credential-modal.component"

export const FormComponentSet: any[] = [
    CustomerSubmissionsComponent,
    PendingRequestsComponent,
    FormDashboardComponent,
    FormListComponent,
    FormDetailComponent,
    FormRequestComponent,
    ScreeningComponent,
    // Screening2Component,
    EmailModalComponent,
    CustomerDetailFormComponent,
    ResubmissionRequestComponent,
    FormSubmissionsComponent,
    SubmissionDetailComponent,
    ExternalScreeningModalComponent,
    IdentityMindScreeningComponent,
    JumioScreeningComponent,
    ShuftiScreeningComponent,
    ThomsonReutersScreeningComponent,
    ScreeningEmailModalComponent,
    DeleteModalComponent,
    RemarksModalComponent,
    SubmissionModalComponent,
    ImportExportModalComponent,
    CredentialModalComponent
];
