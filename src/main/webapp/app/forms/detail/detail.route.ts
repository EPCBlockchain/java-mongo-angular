import { Route, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { FormDetailComponent } from './detail.component';

export const formDetailRoutes: Routes = [{
    path: 'create',
    component: FormDetailComponent,
    data: {
        isNew: true,
        authorities: ['ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
    },
    canActivate: [UserRouteAccessService]
}, {
    path: ':formId',
    component: FormDetailComponent,
    data: {
        authorities: ['ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
    },
    canActivate: [UserRouteAccessService]
}];
