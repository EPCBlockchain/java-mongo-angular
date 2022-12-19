import { Route } from '@angular/router';

import { OrganizationsComponent } from './index';
import { UserRouteAccessService } from '../../core';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRoles } from '../../shared/model';
import { OrganizationDetailComponent } from './detail/organization-detail.component';

export const organizationRoute: Route = {
    path: '',
    children: [
        {
            path: 'list',
            component: OrganizationsComponent,
            data: {
                pageTitle: 'Organizations',
                authorities: [UserRoles.ADMIN],
                defaultSort: 'status,asc'
            },
            resolve: {
                pagingParams: JhiResolvePagingParams
            }
        },
        {
            path: 'create',
            component: OrganizationDetailComponent,
            data: {
                authorities: [UserRoles.ADMIN],
                isNew: true
            },
            canActivate: [UserRouteAccessService]
        },
        {
            path: ':organizationId',
            component: OrganizationDetailComponent,
            data: {
                authorities: [UserRoles.ADMIN, UserRoles.ORG_ADMIN]
            },
            canActivate: [UserRouteAccessService]
        }
    ]
};
