import { Route } from '@angular/router';
import { CustomerDetailFormComponent } from 'app/forms/customer/detail/detail-form.component';
import { UserRouteAccessService } from 'app/core';

export const customerDetailFormRoute: Route = {
    path: 'detail/:submissionId',
    component: CustomerDetailFormComponent,
    data: {
        authorities: ['ROLE_USER']
    },
    canActivate: [UserRouteAccessService]
};
