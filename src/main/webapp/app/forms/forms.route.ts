import { Routes } from '@angular/router';

// import { dashboardRoutes } from 'app/forms/dashboard/dashboard.route';
import { dashboardRoutes } from 'app/forms/dashboard/dashboard.route';

import { formListRoutes } from 'app/forms/list/list.route';
import { submissionsRoute } from 'app/forms/submissions/submissions.route';
import { formRequestRoute } from 'app/forms/customer/request/form-request.route';
import { formDetailRoutes } from 'app/forms/detail/detail.route';
import { customerSubmissionsRoute } from 'app/forms/customer/submissions/customer-submissions.route';
import { pendingRequestsRoute } from 'app/forms/customer/pending/pending-requests.route';
import { customerDetailFormRoute } from 'app/forms/customer/detail/detail-form.route';

import { screeningRoute } from 'app/forms/screening/screening.route';
// import { screeningRoute2 } from 'app/forms/screening2/screening2.route';

import { resubmissionRequest } from 'app/forms/customer/resubmission-request/resubmission-request.route';

const formRoutes: Routes = [
    customerSubmissionsRoute,
    pendingRequestsRoute,
    customerDetailFormRoute,
    ...formListRoutes,        
    ...formDetailRoutes,
    ...submissionsRoute,
    ...screeningRoute,
    // ...screeningRoute2,
    ...dashboardRoutes,
    // dashboardRoutes,
    formRequestRoute,
    resubmissionRequest
];

export const FormsState: Routes = [
    {
        path: '',
        children: formRoutes
    }
];
