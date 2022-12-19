import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrganizationService } from '../../admin/organizations/organizations.service';
import { HttpResponse } from '@angular/common/http';
import { OrganizationEntity } from '../../shared/model';

import { User } from 'app/core';

@Component({
    selector: 'jhi-user-mgmt-detail',
    templateUrl: './user-management-detail.component.html',
    providers: [OrganizationService]
})
export class UserMgmtDetailComponent implements OnInit {
    user: User;
    public orgs: any[];
    organizations = [];

    constructor(
        private service: OrganizationService,
        private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
            console.log(this.user['organizationId']);
            this.service.queryOrganizations({}).subscribe(
                (res: HttpResponse<OrganizationEntity[]>) => {
                    console.log(res.body);
                    this.orgs = res.body;
                    this.orgs.forEach((org: any) => {
                        if (org.id === this.user['organizationId']) {
                            const x = <HTMLInputElement>document.getElementById('orgname');
                            x.value = org.name;
                            console.log(org.name);
                        }
                    });
                    console.log(this.organizations);
                },
                (res: HttpResponse<any>) => alert(res.body)
            );
        });
    }
}
