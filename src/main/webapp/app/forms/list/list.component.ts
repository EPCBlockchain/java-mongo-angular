import { Component, ViewChild, OnInit } from '@angular/core';
import { FormioForm } from 'angular-formio';
import { Router, ActivatedRoute } from '@angular/router';
import { FormService } from 'app/forms/form.service';
import { GridExtension } from 'app/shared/extensions/grid.component';
import { HttpResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import * as moment from 'moment';
import { FormListService } from 'app/forms/list/form-list.service';
import { UserRoles } from 'app/shared/model';
import { Principal } from 'app/core';
import { CustomGridComponent } from 'app/shared/grid/custom-grid.component';
import { SubmissionsService } from 'app/forms/submissions/submissions.service';

@Component({
    templateUrl: './list.component.html',
    providers: [FormService, FormListService, SubmissionsService]
    
    // ORI
    //providers: [FormService, FormListService]
})
export class FormListComponent implements OnInit {
    public forms: FormioForm[];
    private isScreeningForm = false;
    public title: String;
    public isAdmin = false;
    public isOrganizationAdmin = false;
    public valuesStatusArrayPublic = [];
    public submissions: any = [];
    public formId: string;
    public form: any;
    public showKeys: any;
    public fields: any[] = [];
    public rowsDb: any[] = [];
    public columns2: any[] = [];
    public rows = [];
    public isKYCSubmission = false;
    public screeningType = 'shufti';

    @ViewChild(CustomGridComponent, { static: true }) customGrid: CustomGridComponent;
    public columns = [{
        label: 'ID',
        key: 'id'
    }, {
        label: 'Title',
        key: 'title'
    }, {
        label: 'Date Created',
        key: 'creationDate'
    }, {
        label: 'Last Updated',
        key: 'lastUpdated'
    }, {
        label: '',
        buttons: [
        {
            label: 'View Submissions',
            action: row => this.redirectToSubmissions(row)
        },
        {
            label: 'View Form',
            action: row => this.openRow(row)
        }]
    }];

    

    constructor(
        public service: FormListService,
        public router: Router,
        private toastr: ToastrService,
        protected activated: ActivatedRoute,
        private principal: Principal
    ) {
        this.activated.data.subscribe(data => {
            this.isScreeningForm = data['isScreeningForm'];
            this.title = data['pageTitle'];
            this.forms = [];
            this.isAdmin = this.principal.hasAnyAuthorityDirect([UserRoles.ADMIN]);
            this.isOrganizationAdmin = this.principal.hasAnyAuthorityDirect([UserRoles.ORG_ADMIN]);
            // if (this.principal.hasAnyAuthorityDirect([UserRoles.ADMIN])) {
                this.columns.splice(2, 0, {
                    label: 'Organization',
                    key: 'organizationName'
                });
            // }
        });
    }

    ngOnInit() {
        this.customGrid.loadData = () => this.loadData();
        this.loadData();
    }

    loadData() {
        /*
        const options = {
            page: this.customGrid.page - 1,
            size: this.customGrid.itemsPerPage,
            filters: this.customGrid.filters
        };*/
        // Note: with size = 0 , return all register
        const options = {
            page: this.customGrid.page - 1,
            size: 0,
            filters: this.customGrid.filters
        };

        if (!options.filters) {
            options.filters = {};
        }
        if (this.isScreeningForm) {
            options.filters['tags'] = 'screening';
        } else {
            options.filters['tags'] = 'customer';
        }
        this.service.getForms(options).subscribe(
            (res: HttpResponse<FormioForm[]>) => this.customGrid.gridOnSuccess(res, forms => {
                forms.forEach((form: any) => {
                    const newRow2 = {};

                    form.creationDate = moment(form.creationDate).format('MMM DD, YYYY HH:mm');
                    form.lastUpdated = moment(form.lastUpdated).format('MMM DD, YYYY HH:mm');
                    // if (this.isAdmin) {
                        form.organizationName = form.organization.name;
                    // }

                    newRow2["id"] = form.id;
                    newRow2["organizationName"] = form.organization.name;
                    newRow2["organizationId"] = form.organization.id;
                    newRow2["title"] = form.title;
                    this.valuesStatusArrayPublic.push(newRow2);
                });
                this.rows = forms;
            }),
            (res: HttpResponse<any>) => {
                console.log(res);
                this.toastr.error('Error processing request');
            }
        );
    }

    openRow(form) {
        this.router.navigateByUrl('/forms/' + form.id);
    }

    redirectToSubmissions(form: any) {
        this.router.navigateByUrl('/forms/' + form.id + '/submissions');
    }

    newForm() {
        this.router.navigate(['/forms/create'], { queryParams: { is: this.isScreeningForm ? 1 : 0 } });
    }

    filter() {
        this.customGrid.filterOptionsComponent.showAccordion();
    }

    submissionsInfoFilter() {
        this.showDatasubmissionsInfoFilter();
        let SubmissionsInfoStyleDisplay = document.getElementById('SubmissionsInfo').style.display;
        if (SubmissionsInfoStyleDisplay === '' || SubmissionsInfoStyleDisplay === 'none') {
            document.getElementById('SubmissionsInfo').style.display = 'block';
        } else {

            document.getElementById('SubmissionsInfo').style.display = 'none';
        }
    }

    showDatasubmissionsInfoFilter() {
        let valuesStatusArray = [];
        let valuesIdStatusArray = [];
        let RETURNED_TO_CUSTOMER: number = 0;
        let APPROVED: number = 0;
        let PENDING_APPROVAL: number = 0;
        let DRAFT: number = 0;
        let SUBMITTED: number = 0;
        let ESCALATED: number = 0;
        let RESEND: number = 0;
        let REJECTED: number = 0;
        let RETURNED_TO_CUSTOMER_STR: string = '';
        let APPROVED_STR: string = '';
        let PENDING_APPROVAL_STR: string = '';
        let DRAFT_STR: string = '';
        let SUBMITTED_STR: string = '';
        let ESCALATED_STR: string = '';
        let RESEND_STR: string = '';
        let REJECTED_STR: string = '';
        var infoDisplay = '';

        if (document.getElementById('SubmissionsInfo').innerHTML === '' ) {
            console.clear();
            
            for (let i = 0; i < this.valuesStatusArrayPublic.length; i++) {
            
                this.service.getSubmissions(this.valuesStatusArrayPublic[i]["id"], {
                    page: this.customGrid.page - 1,
                    size: 0, 
                    filters: this.customGrid.filters,
                    sort: this.customGrid.setSort({
                        status: 'asc'
                    }, 'asc')
                })
                .subscribe(
                    (res: HttpResponse<any[]>) => this.customGrid.gridOnSuccess(res, (submissions: any) => {
                        this.form = submissions.requestData.form;
                        this.showKeys = [];
                        this.isKYCSubmission = (this.form.tags as any[]).includes('customer');
                       
                        this.rowsDb = submissions.rows.map(rowDb => {
                            const newRow3 = {};
                            this.columns2.forEach(column2 => {
                                if (column2.key === 'creationDate' || column2.key === 'lastUpdate' || column2.key === 'lastUpdated') {
                                    newRow3[column2.key] = moment(rowDb[column2.key]).format('MMM DD, YYYY hh:mm');
                                } else if (rowDb[column2.key]) {
                                    newRow3[column2.key] = rowDb[column2.key];
                                } else {
                                    newRow3[column2.key] = rowDb.data[column2.key];
                                }
                            });
                            valuesStatusArray.push( rowDb );
                            return newRow3;                            
                        });
                        

                        RETURNED_TO_CUSTOMER = APPROVED = PENDING_APPROVAL = DRAFT = SUBMITTED = ESCALATED = RESEND = REJECTED = 0;
                        for (let j = 0; j < valuesStatusArray.length; j++) {
                            
                            // console.log(valuesStatusArray[j]["status"]);   
                            if (valuesStatusArray[j]["formId"]==this.valuesStatusArrayPublic[i]["id"]) {
                                if (valuesStatusArray[j]["status"] === 'RETURNED TO CUSTOMER') {
                                    RETURNED_TO_CUSTOMER++;
                                }
                                if (valuesStatusArray[j]["status"] === 'APPROVED') {
                                    APPROVED++;
                                }
                                if (valuesStatusArray[j]["status"] === 'PENDING_APPROVAL') {
                                    PENDING_APPROVAL++;
                                }
                                if (valuesStatusArray[j]["status"] === 'DRAFT') {
                                    DRAFT++;
                                }
                                if (valuesStatusArray[j]["status"] === 'SUBMITTED') {
                                    SUBMITTED++;
                                }
                                if (valuesStatusArray[j]["status"] === 'ESCALATED') {
                                    ESCALATED++;
                                }
                                if (valuesStatusArray[j]["status"] === 'RESEND') {
                                    RESEND++;
                                }
                                if (valuesStatusArray[j]["status"] === 'REJECTED') {
                                    REJECTED++;
                                }
                            }
                            
                        }

                        RETURNED_TO_CUSTOMER_STR = RETURNED_TO_CUSTOMER.toString();
                        APPROVED_STR = APPROVED.toString();
                        PENDING_APPROVAL_STR = PENDING_APPROVAL.toString();
                        DRAFT_STR = DRAFT.toString();
                        SUBMITTED_STR = SUBMITTED.toString();
                        ESCALATED_STR = ESCALATED.toString();
                        RESEND_STR = RESEND.toString();
                        REJECTED_STR = REJECTED.toString();
                        if ( RETURNED_TO_CUSTOMER > 0 ) {
                            RETURNED_TO_CUSTOMER_STR = "<font style='font-weight:bold;font-style: italic;'>"+RETURNED_TO_CUSTOMER+"</font>";
                        }
                        if ( APPROVED > 0 ) {
                            APPROVED_STR = "<font style='font-weight:bold;font-style: italic;'>"+APPROVED+"</font>";
                        }
                        if ( PENDING_APPROVAL > 0 ) {
                            PENDING_APPROVAL_STR = "<font style='font-weight:bold;font-style: italic;'>"+PENDING_APPROVAL+"</font>";
                        }
                        if ( DRAFT > 0 ) {
                            DRAFT_STR = "<font style='font-weight:bold;font-style: italic;'>"+DRAFT+"</font>";
                        }
                        if ( SUBMITTED > 0 ) {
                            SUBMITTED_STR = "<font style='font-weight:bold;font-style: italic;'>"+SUBMITTED+"</font>";
                        }
                        if ( ESCALATED > 0 ) {
                            ESCALATED_STR = "<font style='font-weight:bold;font-style: italic;'>"+ESCALATED+"</font>";
                        }
                        if ( RESEND > 0 ) {
                            RESEND_STR = "<font style='font-weight:bold;font-style: italic;'>"+RESEND+"</font>";
                        }
                        if ( REJECTED > 0 ) {
                            REJECTED_STR = "<font style='font-weight:bold;font-style: italic;'>"+REJECTED+"</font>";
                        }

                        infoDisplay = infoDisplay + "<font style='color:red;font-weight:bold;'>"+this.valuesStatusArrayPublic[i]["organizationName"]+": "+this.valuesStatusArrayPublic[i]["title"]+"</font><br>"
                        infoDisplay = infoDisplay + "<font style='font-weight:bold;'>SUBMISSIONS</font><br><br>";
                        infoDisplay = infoDisplay + 'RETURNED TO CUSTOMER: ' + RETURNED_TO_CUSTOMER_STR + '<br>';
                        infoDisplay = infoDisplay + 'APPROVED: ' + APPROVED_STR + '<br>';
                        infoDisplay = infoDisplay + 'PENDING APPROVAL: ' + PENDING_APPROVAL_STR + '<br>';
                        infoDisplay = infoDisplay + 'DRAFT: ' + DRAFT_STR + '<br>';
                        infoDisplay = infoDisplay + 'SUBMITTED: ' + SUBMITTED_STR + '<br>';
                        infoDisplay = infoDisplay + 'ESCALATED: ' + ESCALATED_STR + '<br>';
                        infoDisplay = infoDisplay + 'RESEND: ' + RESEND_STR + '<br>';
                        infoDisplay = infoDisplay + 'REJECTED: ' + REJECTED_STR + '<br> <hr>';
                        document.getElementById('SubmissionsInfo').innerHTML = document.getElementById('SubmissionsInfo').innerHTML + infoDisplay; 
                        infoDisplay = "";

                        }),
                    (res: HttpResponse<any>) => {
                        this.toastr.error('Error processing request');
                        // this.spinnerService.hide();
                    }
                );
            } // end for
        } // end if (document.getElementById('SubmissionsInfo').innerHTML === '' ) 
    } //end function
}
