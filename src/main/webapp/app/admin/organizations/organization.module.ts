import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { OrganizationService } from './organizations.service';
import { OrganizationDetailComponent } from './detail/organization-detail.component';
import { OrganizationsComponent } from './list/list.component';
import { AddEmailModalComponent } from './add-email-modal/add-email-modal.component';
import { AddEmailModalService } from './add-email-modal/add-email-modal.service';
import { HttpClientModule } from '@angular/common/http';
import { CustomGridModule } from '../../shared/grid/custom-grid.module';
import { KycSharedModule } from '../../shared';
import { QuillModule } from 'ngx-quill';
import { RouterModule } from '@angular/router';
import { organizationRoute } from './organizations.route';
import { InterceptorModule } from '../../blocks/interceptor/interceptor.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import { allIcons } from 'ng-bootstrap-icons/icons';

@NgModule({
    declarations: [OrganizationDetailComponent, OrganizationsComponent, AddEmailModalComponent],
    providers: [OrganizationService, AddEmailModalService],
    entryComponents: [AddEmailModalComponent],
    imports: [
        InterceptorModule,
        BootstrapIconsModule.pick(allIcons),
        HttpClientModule,
        CustomGridModule,
        KycSharedModule,
        FormsModule,
        ReactiveFormsModule,
        QuillModule.forRoot(),
        RouterModule.forChild([organizationRoute])
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports:[
        BootstrapIconsModule
    ]
})
export class OrganizationModule {}
