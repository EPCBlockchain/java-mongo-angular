import { Routes } from '@angular/router';
import { FormListComponent } from 'app/forms/list/list.component';
import { FormDashboardComponent } from 'app/forms/dashboard/dashboard.component';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';

export const formListRoutes: Routes = [
    {
        path: '',
        component: FormListComponent,
        data: {
            defaultSort: 'status,asc',
            pageTitle: 'KYC Forms',
            isScreeningForm: false,
            authorities: ['ROLE_OBTEAM', 'ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
        },
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'screening',
        component: FormListComponent,
        data: {
            defaultSort: 'status,asc',
            pageTitle: 'Screening Forms',
            isScreeningForm: true,
            authorities: ['ROLE_OBTEAM', 'ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
        },
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'screening2',
        component: FormListComponent,
        data: {
            defaultSort: 'status,asc',
            pageTitle: '...Screening Forms',
            isScreeningForm: true,
            authorities: ['ROLE_OBTEAM', 'ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
        },
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'dashboard',
        component: FormDashboardComponent,
        data: {
            defaultSort: 'status,asc',
            pageTitle: 'Dashboard',
            isScreeningForm: true,
            authorities: ['ROLE_OBTEAM', 'ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
        },
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        canActivate: [UserRouteAccessService]
    }
];
