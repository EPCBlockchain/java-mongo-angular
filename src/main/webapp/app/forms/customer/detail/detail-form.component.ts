import {Component, EventEmitter, ViewChild} from '@angular/core';
import { CustomerFormService } from 'app/forms/customer/customer-form.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormStatus } from 'app/shared/model';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';
import {FormioComponent, FormioForm} from 'angular-formio';
import {FormSubmissionUtil} from 'app/shared/util/form-submission.util';
import {LocalStorageService} from 'angular-web-storage';

@Component({
    templateUrl: './detail-form.component.html',
    styleUrls: ['./detail-form.component.scss', '../../form-dialog.scss']
})
export class CustomerDetailFormComponent {
    private formId: string;
    public title: string;
    public form: FormioForm;
    public data: any;
    public readOnly = false;
    private submissionId: String;
    public refreshForm = new EventEmitter();

    @ViewChild(FormioComponent, { static: true }) formComponent: FormioComponent;

    constructor(
        public service: CustomerFormService,
        protected activated: ActivatedRoute,
        private spinner: NgxSpinnerService,
        private toastr: ToastrService,
        private localStorageService: LocalStorageService,
        private router: Router,
        private formSubmissionUtil: FormSubmissionUtil
    ) {
        this.activated.params.subscribe(params => {
            this.submissionId = params['submissionId'];
            this.loadData();
        });
    }

    onChange(event) {
        if (event.type === 'change') {
            try {
                this.localStorageService.set(this.formId, JSON.stringify(this.formComponent.formio.data), 24, 'h');
            } catch (ex) {
                console.log('error - max length reached');
            }
        }
        if (this.isMobile()) {
          const test = document.getElementsByClassName('formio-component-file');
          Array.from(test).forEach(file => {
              file.getElementsByTagName('input')[0].classList.add('custom-file-input-formio');
          });
        }
    }
    // console.log('change', event);
    onNext = event => console.log('next', event);
    onPrev = event => console.log('prev', event);

    isMobile = () => (typeof window.orientation !== 'undefined') || (navigator.userAgent.indexOf('IEMobile') !== -1);

    loadData() {
        this.service.loadForm(this.submissionId).subscribe(res => {
            this.formSubmissionUtil.fetchAndReplaceImages(res.data);
            this.readOnly = res.status !== FormStatus.DRAFT;
            this.title = res.form.title;
            if (this.readOnly) {
                res.form.display = 'form';
            }
            this.form = res.form;
            this.formId = res.formId;
            if (res.data) {
                this.data = res.data;
            } else {
                try {
                    const previousData = this.localStorageService.get(res.formId);
                    if (previousData) {
                        this.data = JSON.parse(previousData);
                    }
                } catch (e) {
                    console.log('cannot parse data');
                }
            }
        });
    }

    fetchImages(formData) {
        this.formSubmissionUtil.startLazyLoadImages(formData, data => this.refreshForm.emit({
            form: this.form,
            submission: { data }
        }));
    }

    beforeSubmit(submission: object) {
        submission['formId'] = this.formId;
        submission['submissionId'] = this.submissionId;
    }

    onSubmit(e) {
        this.spinner.hide();
        this.toastr.success('Form Submitted');
        this.router.navigateByUrl('/');
    }
}
