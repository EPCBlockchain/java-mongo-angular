import {Component, EventEmitter, OnInit, ViewChild} from '@angular/core';
import { SubmissionsService } from 'app/forms/submissions/submissions.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormStatus } from 'app/shared/model';
import {ToastrService} from 'ngx-toastr';
import {FormSubmissionUtil} from 'app/shared/util/form-submission.util';
import {FormioComponent} from 'angular-formio';
import { Principal } from 'app/core';

@Component({
    templateUrl: './submission-detail.component.html',
    providers: [SubmissionsService]
})
export class SubmissionDetailComponent implements OnInit {
    public form: any;
    public submissionData: any;
    public isScreeningForm = false;
    private submissionId: String;
    public referenceId: String;
    public buttonTitle = 'Create Screening';
    public refreshForm = new EventEmitter();
    public isAdmin = false;

    @ViewChild(FormioComponent, { static: true }) formComponent: FormioComponent;

    constructor(private submissionService: SubmissionsService, private router: Router,
                private activatedRoute: ActivatedRoute, private toasterServiee: ToastrService,
                public principal: Principal,
                private formSubmissionUtil: FormSubmissionUtil) {
        this.activatedRoute.params.subscribe(param => {
            this.submissionService.getFormSubmissionById(param['submissionId']).subscribe(submission => {
                this.formSubmissionUtil.fetchAndReplaceImages(submission.data);
                if (submission.isScreeningForm) {
                    this.router.navigate(['/forms/screening/', submission.formId, submission.submissionReferenceId]);
                }
                this.submissionData = submission.data;
                submission.form.display = 'form';
                this.form = submission.form;
                this.isScreeningForm = submission.isScreeningForm;
                this.referenceId = submission.formReferenceId;
                this.submissionId = param['submissionId'];
                if (submission.status !== FormStatus.SUBMITTED) {
                    this.buttonTitle = 'View Screening';
                }
                console.log(submission);
            });
        });
    }

    back = () => history.back();

    ngOnInit(): void {
        // console.log('init');
    }

    fetchImages(formData) {
        const data = formData;
        this.formSubmissionUtil.startLazyLoadImages(data, dta => this.refreshForm.emit({
            form: this.form,
            submission: { data: dta }
        }));
    }
    public createScreening() {
        
        if (this.form.screeningFormId) {
            this.router.navigate(['/forms/screening/', this.submissionId, 'create']);
        } else {
            this.toasterServiee.error('No Screening Form Selected in the Form Properties, If you are not an admin. Please call your form admin fix the issue.');
        }
    }
}
