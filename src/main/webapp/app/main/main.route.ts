import {Route} from '@angular/router';
import {MasterLayoutComponent} from './master/master.component';
import {UserRouteAccessService} from 'app/core';

export const MainRoute: Route = {
    path: '',
    component: MasterLayoutComponent,
    canActivate: [UserRouteAccessService],
    children: [
        {
            path: '',
            loadChildren: '../home/home.module#HomeModule'
        },
        {
            path: 'admin',
            loadChildren: '../admin/admin.module#KycAdminModule'
        },
        {
            path: 'forms',
            loadChildren: '../forms/forms.module#FormsModule'
        },
        {
            path: 'credentials',
            loadChildren: '../credentials/credentials.module#CredentialsModule'
        }/* ,
        {
            path: 'dashboard',
            loadChildren: '../dashboard/home.module#HomeModule'
        }, */
    ]
};
