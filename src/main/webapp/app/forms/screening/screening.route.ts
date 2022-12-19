import {Route, Routes} from '@angular/router';
import { ScreeningComponent } from 'app/forms/screening/screening.component';

export const screeningRoute: Routes = [{
    path: 'screening/:submissionId/create',
    component: ScreeningComponent,
    data: {
        isCreation: true
    }
}, {
    path: 'screening/:submissionId',
    component: ScreeningComponent
}];
