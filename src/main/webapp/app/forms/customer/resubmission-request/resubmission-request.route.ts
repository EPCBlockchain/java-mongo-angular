import { Route } from '@angular/router';
import { FormRequestComponent } from 'app/forms/customer/request/form-request.component';
import { UserRouteAccessService } from 'app/core';
import { ResubmissionRequestComponent } from 'app/forms/customer/resubmission-request/resubmission-request.components';

export const resubmissionRequest: Route = {
    path: 'resubmission-request/:orgId/:formId',
    component: ResubmissionRequestComponent,
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService]
};
