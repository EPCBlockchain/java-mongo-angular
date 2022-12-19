import { Component, EventEmitter, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormioComponent } from 'angular-formio';
import { ExternalFormService } from 'app/external/external.service';
import { FormEntity, FormStatus } from 'app/shared/model';
import { ToastrService } from 'ngx-toastr';

@Component({
    templateUrl: './external.component.html',
    providers: [ExternalFormService],
    styleUrls: ['./external.component.scss']
})
export class ExternalFormComponent {
    public form: FormEntity = null;
    public isReady = false;
    public isReadOnly = true;
    public refresh = new EventEmitter<any>();
    public submission: any = {};
    @ViewChild(FormioComponent, { static: false }) formComponent: FormioComponent;
    public errorMessage: string;
    constructor(public externalService: ExternalFormService, private activatedRoute: ActivatedRoute, private tostr: ToastrService) {
        this.activatedRoute.queryParams.subscribe(params => {
            Object.keys(params).forEach(key => {
                if (key.toLowerCase() === 'uniqueid') {
                    this.externalService.uniqueId = params[key];
                }
                if (key.toLowerCase() === 'validationid') {
                    this.externalService.validationId = params[key];
                }
            });

            this.activatedRoute.params.subscribe(param => {
                this.externalService.formId = param.formId;

                if (activatedRoute.snapshot.data.viewOnly) {
                    this.externalService.viewOnly = true;
                    this.externalService.getForm(param.formId).subscribe((form: FormEntity) => {
                        this.form = form;
                        this.isReadOnly = false;
                        this.isReady = true;
                    });
                    return;
                }

                this.externalService.getFormData(param.formId).subscribe((response: any) => {
                    this.submission.data = response.data;
                    this.form = response.form;
                    if ([FormStatus.DRAFT, null].includes(response.status)) {
                        this.isReadOnly = false;
                    }
                    this.isReady = true;
                }, ex => {
                    this.errorMessage = ex.statusText;
                });
            });
        });
    }

    onSubmit(submission: any) {
        this.isReadOnly = true;
        this.submission = {};
        this.submission.data = this.formComponent.formio.data;
        this.isReady = false;
        setTimeout(() => {
            this.isReady = true;
            this.tostr.success(this.externalService.viewOnly ? 'This is a view only page. No submission has been recorded' : 'Submission sent');
        }, 100);
    }
}
