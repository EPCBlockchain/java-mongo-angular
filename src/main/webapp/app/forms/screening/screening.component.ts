import {Component, EventEmitter, ViewChild} from '@angular/core';
import {ScreeningService} from 'app/forms/screening/screening.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SubmittedFormEntity} from 'app/shared/model/submitted-form.entity';
import {FormioComponent} from 'angular-formio';
import {FormStatus, UserRoles} from 'app/shared/model';
import {Principal} from 'app/core/auth/principal.service';
import {ExternalScreeningModalService} from 'app/forms/screening/ext-screening-modal/ex-screening-modal.service';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ScreeningType} from 'app/shared/model/enums/screening-type.enum';
import {ToastrService} from 'ngx-toastr';
import {ReturnCustomerDataModalService} from 'app/forms/screening/email-modal/email-modal.service';
import {FormSubmissionUtil} from 'app/shared/util/form-submission.util';
import {RemarksModalService} from 'app/forms/screening/remarks-modal/remarks-modal.service';

// import { EventEmitter } from 'events';

@Component({
    templateUrl: './screening.component.html',
    styleUrls: ['./screening.component.scss', '../form.component.scss'],
    providers: [ScreeningService, ExternalScreeningModalService, ReturnCustomerDataModalService]
})
export class ScreeningComponent {
    private baseScreening: any;
    public screeningForm: any;
    public isReady = false;
    public submissionId: String;
    public submission: any;
    // public defaultValues: any = {};
    public formSubmission: SubmittedFormEntity = new SubmittedFormEntity();
    private screeningModalRef: NgbModalRef;
    private emailModalRef: NgbModalRef;
    private rejectionModalRef: NgbModalRef;
    public screeningType: ScreeningType;
    public ScreeningType = ScreeningType;
    private submittedForm: any;
    public screeningResult: any;
    public screeningDataReady = false;
    public isViewOnly = false;
    public refreshForm: EventEmitter<any> = new EventEmitter<any>();
    public FormStatus = FormStatus;
    public lastUpdatedBy = '';
    public isNotAdmin = true;
    public notResend = true;

    @ViewChild(FormioComponent, { static: false }) public formioComponent: FormioComponent;

    constructor(
        public screeningService: ScreeningService,
        private activatedRoute: ActivatedRoute,
        private toastr: ToastrService,
        private emailService: ReturnCustomerDataModalService,
        private remarksModalService: RemarksModalService,
        private router: Router,
        private principal: Principal,
        private extScreeningModal: ExternalScreeningModalService,
        private formSubmissionUtil: FormSubmissionUtil
    ) {
        this.activatedRoute.params.subscribe(param => {
            this.screeningService.getScreening(param['submissionId'], this.activatedRoute.snapshot.data['isCreation']).subscribe((screening: SubmittedFormEntity) => {
                this.init(screening);
                if (this.principal.getIdentity().authorities.includes(UserRoles.ADMIN) || this.principal.getIdentity().authorities.includes(UserRoles.ORG_ADMIN)) {
                    this.isNotAdmin = false;
                }
            });
        });
    }

    fetchImages(formData) {
        const data = formData;
        this.formSubmissionUtil.startLazyLoadImages(data, () => this.refreshForm.emit({
            form: this.screeningForm,
            submission: { data }
        }));
    }

    init(screening: SubmittedFormEntity) {
        this.baseScreening = screening;
        this.screeningForm = screening.form;
        this.submittedForm = screening.submissionReference;
        this.submissionId = screening.submissionReferenceId;
        // this.notResend = screening.status !== FormStatus.RESEND;
        this.isReady = true;
        this.formSubmissionUtil.fetchAndReplaceImages(screening.data);
        this.formSubmission = screening;

        if ( this.formSubmission.status === 'RESEND' ) {
            this.formSubmission.status = 'RETURNED TO CUSTOMER';
        }

        if (screening.screeningType) {
            this.screeningType = <ScreeningType>screening.screeningType;
            this.screeningResult = screening.screeningData;
            this.screeningDataReady = true;
        }
        this.lastUpdatedBy = screening.lastUpdatedBy;
        this.isViewOnly = <FormStatus>screening.status !== FormStatus.PENDING_APPROVAL && <FormStatus>screening.status !== FormStatus.FURTHER_APPROVAL;
    }

    rejectForm() {
        this.submitForm(FormStatus.REJECTED, () => {
            this.toastr.error('Submission has been rejected');
        });
    }

    back = () => history.back();

    furtherApproval() {
        //alert("furtherApproval()")
        //this.submitForm(FormStatus.FURTHER_APPROVAL, () => this.toastr.info('Submission has been sent to senior management'));
        this.submitForm(FormStatus.ESCALATED, () => this.toastr.info('Submission has been sent to senior management'));
    }

    resendToCustomer() {
        this.emailModalRef = this.emailService.open();
        this.emailModalRef.result.then(
            result => {
                this.screeningService.resubmit(this.formSubmission.submissionReferenceId, result).subscribe(() => {
                    this.toastr.success('Submission has been returned');
                    this.formSubmission.status = FormStatus.RESEND;
                    console.log(this.formSubmission.status);
                    if ( this.formSubmission.status === 'RESEND' ) {
                        this.formSubmission.status = 'RETURNED TO CUSTOMER'
                    }
                    // this.notResend = false;
                    this.formSubmission.remarks = result;
                }, () => this.toastr.error('Internal Server error'));
            },
            reason => {
                console.log('reason', reason);
            }
        );
    }

    approveForm() {
        this.submitForm(FormStatus.APPROVED, () => this.toastr.success('Submission has been approved'));
    }

    private submitForm(status: FormStatus, callback: any = null) {
        this.rejectionModalRef = this.remarksModalService.open();
        this.rejectionModalRef.result.then( remarks => {

                // Note: 2021-01-09
                // If are formId 615467de527bf300012631c9 & 6153a4f4527bf300012631c8, dont get formio.data
                if( this.formSubmission.formId != "615467de527bf300012631c9" && this.formSubmission.formId != "6153a4f4527bf300012631c8" ) {
                    this.formSubmission.data = this.formioComponent.formio.data;
                }

                this.formSubmission.formId = this.screeningForm.id;
                this.formSubmission.isScreeningForm = true;
                this.formSubmission.screeningType = this.screeningType;
                this.screeningService.submitForm(status, remarks, this.formSubmission).subscribe((res: SubmittedFormEntity) => {
                    if (callback) {
                        this.formSubmission.remarks = remarks;
                        this.lastUpdatedBy = res.lastUpdatedBy;
                        callback(res); 
                        console.log( "status: " + status );                        
                        /*
                        if ( status === 'RESEND' ) {
                            status = 'RETURNED TO CUSTOMER';
                        }

                        if ( this.formSubmission.status === 'RESEND' ) {
                            this.formSubmission.status = 'RETURNED TO CUSTOMER'
                        }*/

                        this.formSubmission.status = status;
                     }
                }, () => this.toastr.error('Error processing request.') );
            },
            reason => {
                console.log('reason', reason);
            }
        );
    }

    public requestScreening() {
        this.screeningModalRef = this.extScreeningModal.open();
        this.screeningModalRef.result.then(
            result => {
                this.screeningType = result;
                this.screeningService.requestScreening(this.submissionId, result).subscribe(
                    res => {
                        this.screeningResult = res;
                        this.screeningDataReady = true;
                        console.log(res);
                    },
                    error => {
                        this.toastr.error(error);
                        console.log(error);
                    }
                );
            },
            reason => {
                console.log('reason', reason);
            }
        );
    }
}
