import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilderComponent, FormioForm} from 'angular-formio';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {EmailModalService} from 'app/forms/detail/email-modal/email-modal.service';
import {FormService} from 'app/forms/form.service';
import {ToastrService} from 'ngx-toastr';
import {FormListService} from 'app/forms/list/form-list.service';
import {FormEntity, OrganizationEntity, UserRoles} from 'app/shared/model';
import {Principal} from 'app/core';
import {DeleteModalService} from 'app/forms/detail/delete-modal/delete-modal.service';
import {OrganizationService} from 'app/admin/organizations/organizations.service';
import {ImportExportModalService} from './import-export/import-export-modal.service';
import {CustomTypeaheadComponent} from 'app/shared/typeahead/typeahead.component';
import {CredentialEntity} from 'app/shared/model/credential.entity';
import {FormFieldsUtil} from 'app/shared/util/form-fields.util';
import {CredentialModalService} from 'app/forms/detail/credentials/credential-modal.service';

@Component({
    templateUrl: './detail.component.html',
    styleUrls: ['./detail.component.scss', '../form-dialog.scss'],
    providers: [
        EmailModalService, FormListService, FormService,
        DeleteModalService, ImportExportModalService, CredentialModalService
    ],
    entryComponents: []
})
export class FormDetailComponent implements OnInit {
    form: FormEntity;
    detailOptions = {
        isNew: false,
        isEditting: false,
        requestLink: '',
        isKYCForm: false,
        isReady: false,
        formId: undefined
    };
    formLinks = {
        anonymous: '',
        authenticated: ''
    };
    autocomplete = {
        screening: {
            title: '',
            selected: false,
            badges: [],
            raw: []
        },
        organization: {
            name: '',
            selected: false,
            badges: [],
            raw: [],
            id: ''
        }
    };

    credentials: any[] = [];

    modalRef: {
        email: NgbModalRef;
        delete: NgbModalRef;
        exportImport: NgbModalRef;
    } = {
        email: undefined,
        delete: undefined,
        exportImport: undefined
    };

    @ViewChild(FormBuilderComponent, { static: true }) formBuilder: FormBuilderComponent;
    @ViewChild('credentialsTypeahead', { static: false }) credentialsTypeahead: CustomTypeaheadComponent;

    constructor(
        private formService: FormService,
        public principal: Principal,
        private toasterService: ToastrService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private organizationService: OrganizationService,
        private emailModalService: EmailModalService,
        private deleteModalService: DeleteModalService,
        private importExportModalService: ImportExportModalService,
        private credentialModalService: CredentialModalService
    ) {
        this.activatedRoute.params.subscribe(param => (this.detailOptions.formId = param['formId']));
        this.activatedRoute.data.subscribe(data => {
            this.detailOptions.isEditting = this.detailOptions.isNew = data['isNew'];
            if (this.detailOptions.isNew) {
                this.activatedRoute.queryParams.subscribe(queryParams => {
                    this.detailOptions.isKYCForm = queryParams['is'] === '0';
                });
            }
        });

        this.formService.getCredentials().subscribe((credentials: CredentialEntity[]) => {
            this.credentials = credentials.map(credential => ({
                id: credential.id,
                label: credential.name
            }));
        });

    }

    ngOnInit(): void {
        this.detailOptions.isReady = false;
        if (this.detailOptions.formId) {
            this.formService.getForm(this.detailOptions.formId).subscribe(form => {
                this.form = form;
                this.detailOptions.isKYCForm = form.tags.includes('customer');
                this.formLinks.authenticated = `${window.location.origin}/#/forms/request/${this.form.organizationId}/${this.form.id}`;
                this.formLinks.anonymous = `${window.location.origin}/#/external/${this.form.id}`;
                if (this.principal.getIdentity().organizationId === form.organizationId) {
                    this.autocomplete.organization.selected = true;
                }
                this.loadAutoComplete(undefined);
            });
        } else {
            this.form = new FormEntity();
            this.loadAutoComplete(undefined);
        }
    }

    loadAutoComplete(organizationName: string) {
        this.formService.getScreeningForms(organizationName).subscribe((forms: FormioForm[]) => {
            this.autocomplete.screening.raw = forms;
            if (this.form.screeningFormId) {
                const screeningForm = this.autocomplete.screening.raw.find(scrnForm => {
                    return scrnForm.id === this.form.screeningFormId;
                });
                this.autocomplete.screening.title = screeningForm.title;
                this.autocomplete.screening.selected = true;
            }
        });
        if (organizationName === undefined) {
            if (this.principal.hasAnyAuthorityDirect([UserRoles.ADMIN])) {
                this.organizationService.queryOrganizations().subscribe(response => {
                    this.autocomplete.organization.raw = response.body as OrganizationEntity[];
                    if (this.form.organizationId) {
                        const organization = this.autocomplete.organization.raw.find(org => {
                            return org.id === this.form.organizationId;
                        });
                        this.useOrganizationValue(organization);
                    }
                });
            }
        }
    }

    openRequest() {
        this.modalRef.email = this.emailModalService.open(this.form.id);
        this.modalRef.email.result.then(
            result => {
                console.log(result);
            },
            reason => {
                console.log('reason', reason);
            }
        );
    }

    editForm() {
        this.detailOptions.isEditting = true;
    }

    onFormSelectChange(event) {
        const formString = JSON.stringify(this.form);
        const form = JSON.parse(formString);
        form.display = event.target.value;
        this.form = form;
    }

    redirectToSubmissions = () => this.router.navigateByUrl('/forms/' + this.form.id + '/submissions');

    deleteForm() {
        this.modalRef.delete = this.deleteModalService.open(this.form.id);
        this.modalRef.delete.result.then(
            () => {
                this.formService.deleteForm(this.form.id).subscribe(response => {
                    this.toasterService.success('Form deleted');
                    this.router.navigateByUrl('/forms');
                });
            },
            () => {
                this.toasterService.error('Cannot delete form with submissions.');
            }
        );
    }

    discardChanges() {}

    back = () => history.back();

    getOrganization = (): OrganizationEntity =>
        this.autocomplete.organization.raw.find(organization => {
            return this.autocomplete.organization.name === organization.name;
        });

    saveForm() {
        if (!this.form.title || !this.form.title.trim().length) {
            this.toasterService.error('Title is required');
            return;
        }
        if (this.principal.hasAnyAuthorityDirect(['ROLE_ADMIN'])) {
            const org = this.getOrganization();
            if (org && this.autocomplete.organization.name) {
                this.form.organizationId = org.id;
            } else if (this.autocomplete.organization.name) {
                this.toasterService.error('Organization Not Found');
                return;
            } else {
                this.toasterService.error('Organization Required');
                return;
            }
        }

        const screeningForm = this.autocomplete.screening.raw.find(scrForm => {
            return this.autocomplete.screening.title === scrForm.title;
        });
        if (screeningForm && this.autocomplete.screening.title) {
            this.form.screeningFormId = screeningForm.id;
        } else if (this.autocomplete.screening.title) {
            this.toasterService.error('Screening Form Not Found');
            return;
        }

        if (!this.form.id) {
            this.form.tags = ['common', this.detailOptions.isKYCForm ? 'customer' : 'screening'];
        }

        this.formService.saveForm(this.form).subscribe((form: FormEntity) => {
            if (form) {
                this.toasterService.success(this.form.title + ' saved');
                this.router.navigateByUrl('/forms/' + form.id);
            }
        });
    }

    displayOrganizationBadges(event) {
        this.autocomplete.organization.selected = false;
        this.autocomplete.organization.name = event.target.value;
        this.autocomplete.organization.badges = this.autocomplete.organization.name
            ? this.autocomplete.organization.raw.filter(organization => {
                  return organization.name.includes(this.autocomplete.organization.name);
              })
            : [];
        if (
            this.autocomplete.organization.badges.length === 1 &&
            this.autocomplete.organization.badges.some(organization => organization.name === this.autocomplete.organization.name)
        ) {
            this.useOrganizationValue(this.autocomplete.organization.badges[0]);
            this.autocomplete.organization.badges = [];
        }
    }

    useOrganizationValue(organization: OrganizationEntity) {
        this.autocomplete.organization.name = organization.name;
        this.autocomplete.organization.badges = [];
        this.loadAutoComplete(organization.name);
        this.autocomplete.organization.selected = true;

        // Reset Screening Form if Organization Changed
        this.autocomplete.screening = {
            title: '',
            selected: false,
            badges: [],
            raw: []
        };
    }

    displayScreeningBadges(event) {
        this.autocomplete.screening.selected = false;
        this.autocomplete.screening.title = event.target.value;
        console.log(this.autocomplete);
        this.autocomplete.screening.badges = this.autocomplete.screening.title
            ? this.autocomplete.screening.raw.filter(screeningForm => {
                  return screeningForm.title.toLocaleLowerCase().includes(this.autocomplete.screening.title.toLocaleLowerCase());
              })
            : [];
        if (
            this.autocomplete.screening.badges.length === 1 &&
            this.autocomplete.screening.badges.some(screeningBadge => screeningBadge.title === this.autocomplete.screening.title)
        ) {
        }
    }

    useScreeningValue(form: FormEntity) {
        this.autocomplete.screening.title = form.title;
        this.autocomplete.screening.badges = [];
    }

    onTitleChange(title: string) {
        this.form.title = title;
        if (title) {
            const nospace = title.replace(/\s/g, '');
            this.form.name = nospace.charAt(0).toLowerCase() + nospace.substr(1);
            this.form.path = nospace.toLowerCase();
        } else {
            this.form.path = '';
            this.form.name = '';
        }
    }

    importForm() {
        this.modalRef.exportImport = this.importExportModalService.open('');
        this.modalRef.exportImport.result.then(
            result => {
                result.id = undefined;
                result.creationDate = undefined;
                const organization = this.autocomplete.organization.raw.find(org => org.id === result.organizationId);
                if (organization) {
                    this.autocomplete.organization.name = organization.name;
                }
                this.form = result;
            },
            reason => {
                console.log('reason', reason);
            }
        );
    }

    viewPublishedForm() {
        window.open( '/#/external/form-view/' + this.form.id)
    }

    exportForm() {
        this.modalRef.exportImport = this.importExportModalService.open(JSON.stringify(this.form));
        this.modalRef.exportImport.result.then(
            result => {
                console.log(result);
            },
            reason => {
                console.log('reason', reason);
            }
        );
    }

    onAutocompleteChange(event: any) {
        this.form.credentialId = event.id;
    }

    viewCredentialContents() {
        const credentials = FormFieldsUtil.findCredentialFields(
            this.form,
        component =>
                component.properties.hasOwnProperty('credentialContent'))
            .map(field => ({
                label: field.label,
                value: field.properties.credentialContent
            }));
        this.credentialModalService.open(credentials);
    }
}
