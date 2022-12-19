import { Component, OnInit } from '@angular/core';
import { OrganizationEntity } from 'app/shared/model';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OrganizationService } from 'app/admin/organizations/organizations.service';
import { AddEmailModalService } from 'app/admin/organizations/add-email-modal/add-email-modal.service';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-organization-detail',
    templateUrl: './organization-detail.component.html',
    styles: [],
    providers: [OrganizationService, AddEmailModalService]
})
export class OrganizationDetailComponent implements OnInit {
    public organization: OrganizationEntity = new OrganizationEntity();
    public addEmailModalRef: NgbModalRef;
    public pageTitle: string;
    public isNew = false;

    constructor(
        private service: OrganizationService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private emailModalService: AddEmailModalService
    ) {
        this.activatedRoute.data.subscribe(data => {
            this.isNew = data['isNew'];
            this.pageTitle = this.isNew ? 'Create New Organization' : 'Organization Detail';
        });

        this.activatedRoute.params.subscribe(params => {
            if (params['organizationId']) {
                this.service.get(params['organizationId']).subscribe(organization => {
                    this.organization = organization;
                });
            }
        });
    }

    ngOnInit() {}

    public save() {
        if (!this.organization.name) {
            this.toastr.error('Organization Name is required');
            return;
        }
        if (this.isNew) {
            this.organization
            this.service.create(this.organization).subscribe((res: OrganizationEntity) => {
                this.toastr.success('Organization created.');
                this.service.currentData = res;
                this.router.navigateByUrl('/admin/orgs/' + res.id);
            });
        } else {
            this.service.update(this.organization).subscribe(res => {
                this.toastr.success('Organization updated.');
            });
        }
    }

    public deleteEmail(recipient) {
        this.organization.emailRecipients = this.organization.emailRecipients.filter(test => test !== recipient);
    }

    public addEmailRecipient() {
        this.addEmailModalRef = this.emailModalService.open();
        this.addEmailModalRef.result.then(
            result => {
                const emailToAdd = {
                    email: result.email,
                    types: []
                };

                if (result.submission) {
                    emailToAdd.types.push('submissions');
                }
                if (result.approve) {
                    emailToAdd.types.push('approvals');
                }
                if (result.reject) {
                    emailToAdd.types.push('rejects');
                }

                this.organization.emailRecipients.push(emailToAdd);
            }
        );
    }
}
