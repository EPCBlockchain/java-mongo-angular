import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { IUser, Principal, UserService } from '../../core';
import { OrganizationService } from '../organizations/organizations.service';
import { OrganizationEntity, UserRoles } from '../../shared/model';
import { ToastrService } from 'ngx-toastr';

@Component({
    templateUrl: './detail.component.html'
})
export class UserManagementDetailComponent implements OnInit {
    user: IUser;
    organizations: OrganizationEntity[];
    authorities = [UserRoles.ADMIN,  UserRoles.ORG_ADMIN, UserRoles.OB_TEAM];
    buttonText = 'Save';
    isSaving = false;
    authority = '';

    constructor(
        private service: OrganizationService,
        public router: Router,
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private principal: Principal,
        private toastr: ToastrService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe((response: any) => {
            this.user = response.user;
            if (response.user.id) {
                this.buttonText = 'Update';
                this.authority = response.user.authorities[0];
            } else {
                this.user.activated = false;
            }

            this.principal.identity().then(identity => {
                if (!identity.authorities.includes(UserRoles.ADMIN)) {
                    this.authorities.splice(0, 1);
                }
            });
            this.service.queryOrganizations({}).subscribe(
                (organizationsResponse: any) => {
                    this.organizations = organizationsResponse.body;
                    if (this.organizations.length === 1) {
                        this.user.organizationId = this.organizations[0].id;
                    }
                },
                res => alert(res.body)
            );
        });
    }

    previousState = () => history.back();

    save() {
        this.isSaving = true;
        this.user.authorities = [this.authority];
        if (this.user.id !== null) {
            this.userService.update(this.user).subscribe(
                response => this.onSaveSuccess(response),
                err => this.onSaveError(err)
            );
        } else {
            this.userService.create(this.user).subscribe(
                response => this.onSaveSuccess(response),
                err => this.onSaveError(err)
            );
        }
    }

    private onSaveSuccess(result) {
        this.toastr.success('User created successfully');
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError(response) {
        this.isSaving = false;
        this.toastr.error(response.error.title);
    }
}
