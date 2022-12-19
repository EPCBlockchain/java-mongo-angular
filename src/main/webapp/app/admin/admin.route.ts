import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';

const ADMIN_ROUTES = [
    {
        path: 'user-management',
        loadChildren: './user-management/user-management.module#UserManagementModule'
    },
    {
        path: 'app-settings',
        loadChildren: './app-settings/app-settings.module#AppSettingsModule'
    },
    {
        path: 'orgs',
        loadChildren: './organizations/organization.module#OrganizationModule'
    } /* ,
    {
        path: 'dashboard',
        loadChildren: './dashboard/dashboard.module.ts#HomeModule'
    } */
];

export const adminState: Routes = [
    {
        path: '',
        data: {
            authorities: ['ROLE_ADMIN', 'ROLE_ORGANIZATION_ADMIN']
        },
        canActivate: [UserRouteAccessService],
        children: ADMIN_ROUTES
    }
];
