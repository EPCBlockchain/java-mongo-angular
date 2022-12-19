import { Route } from '@angular/router';
import { CustomerSubmissionsComponent } from 'app/forms/customer/submissions/customer-submissions.component';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';

export const customerSubmissionsRoute: Route = {
    path: 'user-submissions',
    component: CustomerSubmissionsComponent,
    data: {
        defaultSort: 'status,asc',
        authorities: ['ROLE_USER']
    },
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    canActivate: [UserRouteAccessService]
};
