import {Route, Routes} from '@angular/router';
import { FormDashboardComponent } from 'app/forms/dashboard/dashboard.component';

export const dashboardRoutes: Routes = [{
    path: 'dashboard/:submissionId/create',
    component: FormDashboardComponent,
    data: {
        isCreation: true
    }
}, {
    path: 'dashboard/:submissionId',
    component: FormDashboardComponent
}, {
    path: 'dashboard/',
    component: FormDashboardComponent
}];
