import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { OrganizationService } from '../organizations/organizations.service';
import { UserManagementDetailComponent } from './detail.component';
import { UserMgmtDeleteDialogComponent } from './user-management-delete-dialog.component';
import { UserMgmtDetailComponent } from './user-management-detail.component';
import { UserMgmtUpdateComponent } from './user-management-update.component';
import { UserMgmtComponent } from './user-management.component';
import { userMgmtRoute } from './user-management.route';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModalModule, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import { allIcons } from 'ng-bootstrap-icons/icons';



@NgModule({
    providers: [OrganizationService],
    imports: [CommonModule, NgbModule, BootstrapIconsModule.pick(allIcons),NgbModalModule, FormsModule, ReactiveFormsModule, RouterModule.forChild([userMgmtRoute])],
    declarations: [
        UserMgmtComponent,
        UserMgmtDetailComponent,
        UserMgmtUpdateComponent,
        UserMgmtDeleteDialogComponent,
        UserManagementDetailComponent
    ],
    entryComponents: [UserMgmtDeleteDialogComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UserManagementModule {}
