import { Routes } from '@angular/router';
import { ProfileRoute } from 'app/home/profile/profile.route';
import { NotFoundComponent } from 'app/home/not-found/not-found.component';
import { WelcomeComponent } from './welcome/welcome.component';

export const HomeRoutes: Routes = [
    {
        path: '',
        component: WelcomeComponent,
        children: [
            ProfileRoute,
            {
                path: 'not-found',
                component: NotFoundComponent
            }
        ]
    }
];
