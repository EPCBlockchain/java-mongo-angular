import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
// import { errorRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { UserRouteAccessService } from 'app/core';

// const LAYOUT_ROUTES = [...errorRoute];

const APP_ROUTES: Routes = [
    {
        path: '',
        loadChildren: './main/main.module#MainModule'
    },
    {
        path: 'account',
        loadChildren: './account/account.module#AccountModule'
    },
    {
        path: 'external',
        loadChildren: './external/external.module#ExternalFormModule'
    }
    // ...LAYOUT_ROUTES,
];

@NgModule({
    imports: [RouterModule.forRoot(APP_ROUTES, { useHash: true })], // , enableTracing: DEBUG_INFO_ENABLED
    exports: [RouterModule]
})
export class KycAppRoutingModule {}
