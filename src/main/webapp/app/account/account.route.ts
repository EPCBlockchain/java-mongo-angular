import { Routes } from '@angular/router';

import {
    loginRoute,
    activateRoute,
    passwordRoute,
    passwordResetFinishRoute,
    passwordResetInitRoute,
    registerRoute,
    settingsRoute
} from './';
import { AccountMasterComponent } from 'app/account/master/account-master.component';

export const accountState: Routes = [
    {
        path: '',
        component: AccountMasterComponent,
        children: [activateRoute, loginRoute, passwordRoute, passwordResetFinishRoute, passwordResetInitRoute, registerRoute, settingsRoute]
    }
];
