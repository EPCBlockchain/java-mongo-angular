import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiLanguageHelper, User, UserService, Principal } from '../../core';
import { HttpResponse } from '@angular/common/http';
import { OrganizationEntity, UserRoles } from '../../shared/model';
import { OrganizationService } from '../../admin/organizations/organizations.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'jhi-user-mgmt-update',
    templateUrl: './user-management-update.component.html',
    providers: [OrganizationService]
})
export class UserMgmtUpdateComponent implements OnInit {
    user: User;
    currentAccount: any;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    public users: any[];
    userList = [];
    public orgs: any[];
    organizations = [];

    constructor(
        private principal: Principal,
        private service: OrganizationService,
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private toaster: ToastrService
    ) {}

    ngOnInit() {
        this.isSaving = false;
        // this.queryOrganizationList();
        this.route.data.subscribe(({ user }) => {
            user.langKey = 'en';
            this.service.queryOrganizations({}).subscribe(
                (res: HttpResponse<OrganizationEntity[]>) => {
                    let orgs = res.body;
                    const localUser = user.body ? user.body : user;
                    const userIdentity = this.principal.getIdentity() as User;
                    if (userIdentity.authorities.includes(UserRoles.ADMIN)) {
                        orgs = res.body.filter(org => org.status === 'active');
                    } else {
                        orgs = res.body.filter(org => org.status === 'active' && org.id === userIdentity.organizationId);
                        localUser.organizationId = orgs[0].id;
                    }
                    // TODO Apply a more dynamic approach
                    let authorities = [UserRoles.OB_TEAM, UserRoles.ORG_ADMIN];
                    if (userIdentity.authorities.includes(UserRoles.ADMIN)) {
                        authorities = authorities.concat([UserRoles.ADMIN]);
                    }
                    this.authorities = authorities.sort((roleA, roleB) => (roleB > roleA ? -1 : 1));
                    this.languageHelper.getAll().then(languages => {
                        this.languages = languages;
                    });
                    this.user = localUser;
                    this.organizations = orgs;
                },
                (res: HttpResponse<any>) => alert(res.body)
            );
        });
    }

    previousState() {
        this.router.navigate(['/admin/user-management']);
    }

    save() {
        // this.userService.query({}).subscribe(
        //     (res: HttpResponse<User[]>) => {
        //         this.users = res.body;
        //         this.users.forEach((user: any) => {
        //             this.userList.push({id: user.id, name: user.firstName, email: user.email});
        //         });
        //         console.log(this.users);
        //     },
        //     (res: HttpResponse<any>) => alert(res.body)
        // );
        this.isSaving = true;
        this.userList.forEach((users: any) => {
            if (users.name === this.user.login) {
                this.toaster.error('User login already exists');
            }
        });

        this.userList.forEach((users: any) => {
            if (users.email === this.user.email) {
                this.toaster.error('Email Address already in use');
            }
        });
        if (this.user.id !== null) {
            this.validateUser(() => {
                this.userService.update(this.user).subscribe(
                    response => this.onSaveSuccess(response),
                    err => this.onSaveError(err)
                );
            });
        } else {
            this.validateUser(() => {
                this.userService.create(this.user).subscribe(
                    response => this.onSaveSuccess(response),
                    err => this.onSaveError(err)
                );
            });
        }
    }
    private validateUser(callback: Function) {
        if (this.user.authorities == null) {
            this.toaster.error('Please select User Role');
            this.isSaving = false;
        } else if (this.user.organizationId == null) {
            this.toaster.error('Please select an Organization');
            this.isSaving = false;
        } else {
            if (typeof this.user.authorities === typeof '') {
                this.user.authorities = [this.user.authorities];
            }
            callback();
        }
    }

    private onSaveSuccess(result) {
        this.toaster.success('User created successfully');
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError(response) {
        this.isSaving = false;
        this.toaster.error(response.error.title);
    }

    public queryUsersList() {
        this.userService.query({}).subscribe(
            (res: HttpResponse<User[]>) => {
                this.users = res.body;
                this.users.forEach((user: any) => {
                    this.userList.push({ id: user.id, name: user.firstName, email: user.email });
                });
            },
            (res: HttpResponse<any>) => alert(res.body)
        );
    }

    public queryOrganizationList() {}
}
