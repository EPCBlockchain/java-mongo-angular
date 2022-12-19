import {NgModule} from '@angular/core'
import {RouterModule, Routes} from '@angular/router'
import {CustomGridModule} from 'app/shared/grid/custom-grid.module'
import {CredentialsListComponent} from 'app/credentials/list/list.component'
import {HttpClientModule} from '@angular/common/http'
import {CredentialDetailComponent} from 'app/credentials/detail/detail.component'
import {CommonModule} from '@angular/common'
import {CredentialsService} from 'app/credentials/credentials.service'
import {JhiResolvePagingParams} from 'ng-jhipster'
import {UserRouteAccessService} from 'app/core'
import {CredentialDetailResolver} from 'app/credentials/detail/detail.resolver'
import {FormsModule} from '@angular/forms'
import {InterceptorModule} from 'app/blocks/interceptor/interceptor.module'
import {KycSharedModule} from 'app/shared';
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import { allIcons } from 'ng-bootstrap-icons/icons';

const routes: Routes = [ {
    path: '',
    children: [
        {
            path: 'list',
            component: CredentialsListComponent,
            data: {
                authorities: ['ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
            },
            resolve: {
                pagingParams: JhiResolvePagingParams
            },
            canActivate: [UserRouteAccessService]
        }, {
            path: 'create',
            component: CredentialDetailComponent,
            data: {
                authorities: ['ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
            },
            canActivate: [UserRouteAccessService]
        }, {
            path: ':id',
            component: CredentialDetailComponent,
            data: {
                authorities: ['ROLE_ORGANIZATION_ADMIN', 'ROLE_ADMIN']
            },
            resolve: {
                details: CredentialDetailResolver
            },
            canActivate: [UserRouteAccessService]
        }
    ]
}];

@NgModule({
    imports: [
        RouterModule.forChild(routes),
        CustomGridModule,
        CommonModule,
        HttpClientModule,
        FormsModule,
        KycSharedModule,
        InterceptorModule,
        BootstrapIconsModule.pick(allIcons)
    ],
    declarations: [
        CredentialsListComponent,
        CredentialDetailComponent
    ],
    providers: [
        CredentialsService,
        CredentialDetailResolver
    ],
    exports: [RouterModule,BootstrapIconsModule]
})
export class CredentialsModule {}
