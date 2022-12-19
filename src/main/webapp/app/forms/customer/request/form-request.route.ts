import { Route } from '@angular/router';
import { FormRequestComponent } from 'app/forms/customer/request/form-request.component';
import { UserRouteAccessService } from 'app/core';

export const formRequestRoute: Route = {
    path: 'request/:orgId/:formId',
    component: FormRequestComponent,
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService]
};
