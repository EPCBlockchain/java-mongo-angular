import { Route } from '@angular/router';
import { PendingRequestsComponent } from 'app/forms/customer/pending/pending-requests.component';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';

export const pendingRequestsRoute: Route = {
    path: 'pending-requests',
    component: PendingRequestsComponent,
    data: {
        defaultSort: 'status,asc',
        authorities: ['ROLE_USER']
    },
    resolve: {
        pagingParams: JhiResolvePagingParams
    },
    canActivate: [UserRouteAccessService]
};
