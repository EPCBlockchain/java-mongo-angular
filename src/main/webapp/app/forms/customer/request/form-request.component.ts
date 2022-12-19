import {Component, OnInit, ViewChild} from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';
import {FormioComponent, FormioForm} from 'angular-formio';
import { Principal } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerFormService } from 'app/forms/customer/customer-form.service';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {SubmissionModalService} from 'app/forms/customer/request/submission-modal/submission-modal.service';
import {LocalStorageService} from 'angular-web-storage';

@Component({
    templateUrl: './form-request.component.html',
    styleUrls: ['./form-request.component.scss', '../../form-dialog.scss'],
    providers: [CustomerFormService]
})
export class FormRequestComponent implements OnInit {
    public isReadOnly = false;
    public form: FormioForm;
    public data: any;
    public submissionId: String;

    private currentAccount;
    private formId;

    @ViewChild(FormioComponent, { static: true }) formioComponent: FormioComponent;
    private submissionModalRef: NgbModalRef;

    constructor(
        public service: CustomerFormService,
        private localStorageService: LocalStorageService,
        private toastr: ToastrService,
        private spinner: NgxSpinnerService,
        private principal: Principal,
        private submissionModalService: SubmissionModalService,
        private activatedRoute: ActivatedRoute,
        private router: Router
    ) {
        this.activatedRoute.params.subscribe(param => {
            this.service.loadRequestForm(param['orgId'], param['formId']).subscribe(userForm => {
                const previousData = this.localStorageService.get(param['formId']);
                if (userForm.data) {
                    this.service.data = userForm;
                    this.router.navigate(['forms/detail', userForm.id]);
                } else if (previousData) {
                    try {
                        this.data = JSON.parse(previousData);
                    } catch (e) {
                        console.log('cannot parse data');
                    }
                } else {
                    this.submissionId = userForm.id;
                    this.data = userForm.data || {};
                }
                this.form = userForm.form;
            },
            err => {
                if (err.status === 400) {
                    this.router.navigateByUrl('/not-found');
                }
                }
            );
        });
    }

    onChange(event) {
        if (event.type === 'change') {
            try {
                this.localStorageService.set(this.formId + this.submissionId, JSON.stringify(this.formioComponent.formio.data), 24, 'h' );
            } catch (ex) {
                console.log('error - max length reached');
            }
        }
        if (this.isMobile()) {
            const test = document.getElementsByClassName('formio-component-file');
            Array.from(test).forEach(file => {
                file.getElementsByTagName('input')[0].classList.add('custom-file-input-formio');
                const textnode = file.getElementsByClassName('browse')[0].nextSibling;
                textnode.parentNode.removeChild(textnode);
            });
        }
    }

    isMobile = () => (typeof window.orientation !== 'undefined') || (navigator.userAgent.indexOf('IEMobile') !== -1);

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.activatedRoute.params.subscribe(param => {
            this.formId = param['formId'];
        });
    }

    beforeSubmit(submission: object) {
        submission['formId'] = this.formId;
        submission['submissionId'] = this.submissionId;
    }

    onSubmit(e) {
        this.spinner.hide();
        this.submissionModalRef = this.submissionModalService.open();
        this.submissionModalRef.result.then(
            result => {
                this.router.navigateByUrl('/');
            }
        );
    }
}
