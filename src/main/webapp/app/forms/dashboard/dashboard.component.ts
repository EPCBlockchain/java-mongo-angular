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
// import * as CanvasJS from './canvasjs.min';
// import * as CanvasJS from 'node_modules/canvasjs/dist/canvasjs.js';
import * as CanvasJS from '../../../assets/js/canvasjs.min.js';

@Component({
    templateUrl: './dashboard.component.html',
    providers: [FormService, FormListService, SubmissionsService]
})

export class FormDashboardComponent implements OnInit {
    public forms: FormioForm[];
    private isScreeningForm = false;
    public title: String;
    public isAdmin = false;
    public isOrganizationAdmin = false;
    public valuesStatusArrayPublic = [];
    public valuesStatusArrayMonthsPublic = [];
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
    public graphDataPoints1: any = [];
    public graphDataPointsBand1 = false;
    public mL = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    public mS = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];

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
        private router: Router,
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
            if (this.principal.hasAnyAuthorityDirect([UserRoles.ADMIN])) {
                this.columns.splice(2, 0, {
                    label: 'Organization',
                    key: 'organizationName'
                });
            }
        });
    }

    ngOnInit() {
        let graphDataPoints_1 = {};
        const yearCurrent = new Date().getFullYear();

        for (let i = 0; i < this.mL.length; i++) {
            graphDataPoints_1 = {};
            graphDataPoints_1['y'] = 0;
            graphDataPoints_1['label'] = yearCurrent.toString() + '-' + this.mL[i];
            graphDataPoints_1['name'] = yearCurrent.toString() + '-' + this.mL[i];
            this.valuesStatusArrayMonthsPublic.push(graphDataPoints_1);
        }

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Returned Customer';
        graphDataPoints_1['name'] = 'Returned Customer';
        this.graphDataPoints1.push(graphDataPoints_1);

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Pendign Approval';
        graphDataPoints_1['name'] = 'Pendign Approval';
        this.graphDataPoints1.push(graphDataPoints_1);

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Escalated';
        graphDataPoints_1['name'] = 'Escalated';
        this.graphDataPoints1.push(graphDataPoints_1);

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Approved';
        graphDataPoints_1['name'] = 'Approved';
        this.graphDataPoints1.push(graphDataPoints_1);

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Rejected';
        graphDataPoints_1['name'] = 'Rejected';
        this.graphDataPoints1.push(graphDataPoints_1);

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Submitted';
        graphDataPoints_1['name'] = 'Submitted';
        this.graphDataPoints1.push(graphDataPoints_1);

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Draft';
        graphDataPoints_1['name'] = 'Draft';
        this.graphDataPoints1.push(graphDataPoints_1);

        graphDataPoints_1 = {};
        graphDataPoints_1['y'] = 0;
        graphDataPoints_1['label'] = 'Resend';
        graphDataPoints_1['name'] = 'Resend';
        this.graphDataPoints1.push(graphDataPoints_1);

        this.customGrid.loadData = () => this.loadData();
        this.loadData();
    }

    loadData() {
        const valuesStatusArray = [];
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
                    if (this.isAdmin) {
                        form.organizationName = form.organization.name;
                    }
                    newRow2['id'] = form.id;
                    newRow2['organizationName'] = form.organization.name;
                    newRow2['organizationId'] = form.organization.id;
                    newRow2['title'] = form.title;
                    this.valuesStatusArrayPublic.push(newRow2);
                });
                // this.rows = forms;
                if (this.valuesStatusArrayPublic.length > 0) {
                    // this.submissionsInfoFilter();
                    console.clear();
                    for (let i = 0; i < this.valuesStatusArrayPublic.length; i++) {
                        this.service.getSubmissions(this.valuesStatusArrayPublic[i]['id'], {
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
                                }); // end submissions rows

                                // Fill data array to graph 1
                                if ( valuesStatusArray.length > 0 ) {
                                    this.graphDataPointsFill1(valuesStatusArray);
                                }

                                }), // end subscribe
                            (res: HttpResponse<any>) => {
                                this.toastr.error('Error processing request');
                            }
                        );
                    } // end for
                }
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

    graphDataPointsFill1(valuesStatusArray) {
        const valuesIdStatusArray = [];
        let RETURNED_TO_CUSTOMER = 0;
        let APPROVED = 0;
        let PENDING_APPROVAL = 0;
        let DRAFT = 0;
        let SUBMITTED = 0;
        let ESCALATED = 0;
        let RESEND = 0;
        let REJECTED = 0;
        // let TOTAL_RETURNED_TO_CUSTOMER = 0;
        // let TOTAL_APPROVED = 0;
        // let TOTAL_PENDING_APPROVAL = 0;
        // let TOTAL_DRAFT = 0;
        // let TOTAL_SUBMITTED = 0;
        // let TOTAL_ESCALATED = 0;
        // let TOTAL_RESEND = 0;
        // let TOTAL_REJECTED = 0;
        // let RETURNED_TO_CUSTOMER_STR = '';
        // let APPROVED_STR = '';
        // let PENDING_APPROVAL_STR = '';
        // let DRAFT_STR = '';
        // let SUBMITTED_STR = '';
        // let ESCALATED_STR = '';
        // let RESEND_STR = '';
        // let REJECTED_STR = '';
        // let infoDisplay = '';
        // let graphDataPoints_1 = {};
        let strDate1 = '';
        let strDate2 = [];
        let strDate3 = '';
        let strDate4 = [];
        const yearCurrent = new Date().getFullYear();

        for (let j = 0; j < valuesStatusArray.length; j++) {
            strDate1 = valuesStatusArray[j]['creationDate']; // 2020-07-05T21:40:24.532Z
            strDate2 = strDate1.split('T');
            strDate3 = strDate2[0];
            strDate4 = strDate3.split('-'); // 2020-07-05
            for (let i = 0; i < this.valuesStatusArrayMonthsPublic.length; i++) {
                if ( this.valuesStatusArrayMonthsPublic[i]['label'] === yearCurrent.toString() + '-' + this.mL[this.mS.indexOf(strDate4[1])] ) {
                    this.valuesStatusArrayMonthsPublic[i]['y'] = this.valuesStatusArrayMonthsPublic[i]['y'] + 1;
                }
            }

            for (let i = 0; i < this.valuesStatusArrayPublic.length; i++) {
                if (valuesStatusArray[j]['formId'] === this.valuesStatusArrayPublic[i]['id']) {
                    if (valuesStatusArray[j]['status'] === 'RETURNED TO CUSTOMER') {
                        RETURNED_TO_CUSTOMER++;
                    }
                    if (valuesStatusArray[j]['status'] === 'APPROVED') {
                        APPROVED++;
                    }
                    if (valuesStatusArray[j]['status'] === 'PENDING_APPROVAL') {
                        PENDING_APPROVAL++;
                    }
                    if (valuesStatusArray[j]['status'] === 'DRAFT') {
                        DRAFT++;
                    }
                    if (valuesStatusArray[j]['status'] === 'SUBMITTED') {
                        SUBMITTED++;
                    }
                    if (valuesStatusArray[j]['status'] === 'ESCALATED') {
                        ESCALATED++;
                    }
                    if (valuesStatusArray[j]['status'] === 'RESEND') {
                        RESEND++;
                    }
                    if (valuesStatusArray[j]['status'] === 'REJECTED') {
                        REJECTED++;
                    }
                }
            } // end for valuesStatusArrayPublic.length
        } // end for valuesStatusArray.length

        for (let i = 0; i < this.graphDataPoints1.length; i++) {
            /*
            if (this.graphDataPoints1[i]['label'] === 'Returned Customer') {
                document.getElementById('totalReturnedCustomer').innerHTML = RETURNED_TO_CUSTOMER.toString();
                this.graphDataPoints1[i]['y'] = RETURNED_TO_CUSTOMER;
                this.graphDataPoints1[i]['color'] = '#78B6E4';
            }*/
            if (this.graphDataPoints1[i]['label'] === 'Pendign Approval') {
                document.getElementById('totalPendingApproval').innerHTML = PENDING_APPROVAL.toString();
                this.graphDataPoints1[i]['y'] = PENDING_APPROVAL;
                this.graphDataPoints1[i]['color'] = '#F4A900';
            }
            if (this.graphDataPoints1[i]['label'] === 'Escalated') {
                document.getElementById('totalEscalated').innerHTML = ESCALATED.toString();
                this.graphDataPoints1[i]['y'] = ESCALATED;
                this.graphDataPoints1[i]['color'] = '#F16521';
            }
            if (this.graphDataPoints1[i]['label'] === 'Approved') {
                document.getElementById('totalApproved').innerHTML = APPROVED.toString();
                this.graphDataPoints1[i]['y'] = APPROVED;
                this.graphDataPoints1[i]['color'] = '#20B5AC';
            }
            if (this.graphDataPoints1[i]['label'] === 'Rejected') {
                document.getElementById('totalRejected').innerHTML = REJECTED.toString();
                this.graphDataPoints1[i]['y'] = REJECTED;
                this.graphDataPoints1[i]['color'] = '#b0b0b0';
            }
            /*
            if (this.graphDataPoints1[i]['label'] === 'Submitted') {
                document.getElementById('totalSubmitted').innerHTML = SUBMITTED.toString();
                this.graphDataPoints1[i]['y'] = SUBMITTED;
                this.graphDataPoints1[i]['color'] = '#00580a';
            }
            if (this.graphDataPoints1[i]['label'] === 'Draft') {
                document.getElementById('totalDraft').innerHTML = DRAFT.toString();
                this.graphDataPoints1[i]['y'] = DRAFT;
                this.graphDataPoints1[i]['color'] = '#DDD3B9';
            }
            if (this.graphDataPoints1[i]['label'] === 'Resend') {
                document.getElementById('totalResend').innerHTML = RESEND.toString();
                this.graphDataPoints1[i]['y'] = RESEND;
                this.graphDataPoints1[i]['color'] = '#d8a016';
            }*/
        }
        this.canvasJs();
        this.canvasJsPie();
        this.canvasJsLine();
    }

    canvasJs() {
        const chart = new CanvasJS.Chart('chartContainer', {
            animationEnabled: true,
            exportEnabled: true,
            creditText: ' ',
            title: {
                text: ' '
            },
            data: [{
                type: 'column',
                dataPoints: this.graphDataPoints1
            }]
        });
        chart.render();
    }

    canvasJsLine() {
        const chart = new CanvasJS.Chart('chartContainer3', {
            animationEnabled: true,
            exportEnabled: true,
            creditText: ' ',
            title: {
                text: ' '
            },
            data: [{
                type: 'line',
                dataPoints: this.valuesStatusArrayMonthsPublic
            }]
        });
        chart.render();
    }

    canvasJsPie() {
        const chart2 = new CanvasJS.Chart('chartContainer2', {
        theme: 'light2',
        creditText: ' ',
        animationEnabled: true,
        exportEnabled: true,
        title: {
            text: ' '
        },
        data: [{
            type: 'pie',
            showInLegend: true,
            toolTipContent: '<b>{name}</b>: {y} (#percent%)',
            indexLabel: '{name} - #percent%',
            dataPoints: this.graphDataPoints1
            }]
        });
        chart2.render();
    }

    submissionsInfoFilter() {
        this.showDatasubmissionsInfoFilter();
        const SubmissionsInfoStyleDisplay = document.getElementById('SubmissionsInfo').style.display;
        if (SubmissionsInfoStyleDisplay === '' || SubmissionsInfoStyleDisplay === 'none') {
            document.getElementById('SubmissionsInfo').style.display = 'block';
        } else {

            document.getElementById('SubmissionsInfo').style.display = 'none';
        }
    }

    showDatasubmissionsInfoFilter() {
        // console.clear();
        // console.log(" ******************************* this.valuesStatusArrayPublic ***********************************");
        // console.log(this.valuesStatusArrayPublic);
        /*
        const valuesStatusArray = [];
        const valuesIdStatusArray = [];
        let RETURNED_TO_CUSTOMER = 0;
        let APPROVED = 0;
        let PENDING_APPROVAL = 0;
        let DRAFT = 0;
        let SUBMITTED = 0;
        let ESCALATED = 0;
        let RESEND = 0;
        let REJECTED = 0;
        let TOTAL_RETURNED_TO_CUSTOMER = 0;
        let TOTAL_APPROVED = 0;
        let TOTAL_PENDING_APPROVAL = 0;
        let TOTAL_DRAFT = 0;
        let TOTAL_SUBMITTED = 0;
        let TOTAL_ESCALATED = 0;
        let TOTAL_RESEND = 0;
        let TOTAL_REJECTED = 0;
        let RETURNED_TO_CUSTOMER_STR = '';
        let APPROVED_STR = '';
        let PENDING_APPROVAL_STR = '';
        let DRAFT_STR = '';
        let SUBMITTED_STR = '';
        let ESCALATED_STR = '';
        let RESEND_STR = '';
        let REJECTED_STR = '';
        let infoDisplay = '';
        let graphDataPoints_1 = {};
        if (document.getElementById('SubmissionsInfo').innerHTML === '' ) {
            console.clear();
            for (let i = 0; i < this.valuesStatusArrayPublic.length; i++) {
                this.service.getSubmissions(this.valuesStatusArrayPublic[i]['id'], {
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
                        // for (let j = 0; j < valuesStatusArray.length; j++) {
                        for (let j = 0; j < valuesStatusArray.length; j++) {
                            if (valuesStatusArray[j]['formId'] === this.valuesStatusArrayPublic[i]['id']) {
                                if (valuesStatusArray[j]['status'] === 'RETURNED TO CUSTOMER') {
                                    RETURNED_TO_CUSTOMER++;
                                    TOTAL_RETURNED_TO_CUSTOMER++;
                                }
                                if (valuesStatusArray[j]['status'] === 'APPROVED') {
                                    APPROVED++;
                                    TOTAL_APPROVED++;
                                }
                                if (valuesStatusArray[j]['status'] === 'PENDING_APPROVAL') {
                                    PENDING_APPROVAL++;
                                    TOTAL_PENDING_APPROVAL++;
                                }
                                if (valuesStatusArray[j]['status'] === 'DRAFT') {
                                    DRAFT++;
                                    TOTAL_DRAFT++;
                                }
                                if (valuesStatusArray[j]['status'] === 'SUBMITTED') {
                                    SUBMITTED++;
                                    TOTAL_SUBMITTED++;
                                }
                                if (valuesStatusArray[j]['status'] === 'ESCALATED') {
                                    ESCALATED++;
                                    TOTAL_ESCALATED++;
                                }
                                if (valuesStatusArray[j]['status'] === 'RESEND') {
                                    RESEND++;
                                    TOTAL_RESEND++;
                                }
                                if (valuesStatusArray[j]['status'] === 'REJECTED') {
                                    REJECTED++;
                                    TOTAL_REJECTED++;
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
                            RETURNED_TO_CUSTOMER_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + RETURNED_TO_CUSTOMER + '</font>';
                        }
                        if ( APPROVED > 0 ) {
                            APPROVED_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + APPROVED + '</font>';
                        }
                        if ( PENDING_APPROVAL > 0 ) {
                            PENDING_APPROVAL_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + PENDING_APPROVAL + '</font>';
                        }
                        if ( DRAFT > 0 ) {
                            DRAFT_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + DRAFT + '</font>';
                        }
                        if ( SUBMITTED > 0 ) {
                            SUBMITTED_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + SUBMITTED + '</font>';
                        }
                        if ( ESCALATED > 0 ) {
                            ESCALATED_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + ESCALATED + '</font>';
                        }
                        if ( RESEND > 0 ) {
                            RESEND_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + RESEND + '</font>';
                        }
                        if ( REJECTED > 0 ) {
                            REJECTED_STR = '<font style=' + 'font-weight:bold;font-style: italic;' + '>' + REJECTED + '</font>';
                        }
                        infoDisplay = infoDisplay + '<font style=' + 'color:red;font-weight:bold;' + '>';
                        infoDisplay = infoDisplay + this.valuesStatusArrayPublic[i]['organizationName'] + ': ';
                        infoDisplay = infoDisplay + this.valuesStatusArrayPublic[i]['title'] + '</font><br>';
                        infoDisplay = infoDisplay + '<font style=' + 'font-weight:bold;' + '>SUBMISSIONS</font><br><br>';
                        infoDisplay = infoDisplay + 'RETURNED TO CUSTOMER: ' + RETURNED_TO_CUSTOMER_STR + '<br>';
                        infoDisplay = infoDisplay + 'APPROVED: ' + APPROVED_STR + '<br>';
                        infoDisplay = infoDisplay + 'PENDING APPROVAL: ' + PENDING_APPROVAL_STR + '<br>';
                        infoDisplay = infoDisplay + 'DRAFT: ' + DRAFT_STR + '<br>';
                        infoDisplay = infoDisplay + 'SUBMITTED: ' + SUBMITTED_STR + '<br>';
                        infoDisplay = infoDisplay + 'ESCALATED: ' + ESCALATED_STR + '<br>';
                        infoDisplay = infoDisplay + 'RESEND: ' + RESEND_STR + '<br>';
                        infoDisplay = infoDisplay + 'REJECTED: ' + REJECTED_STR + '<br> <hr>';
                        // document.getElementById('SubmissionsInfo').innerHTML = document.getElementById('SubmissionsInfo').innerHTML + infoDisplay;

                        // this.graphDataPoints1 = [];
                        document.getElementById('totalReturnedCustomer').innerHTML = TOTAL_RETURNED_TO_CUSTOMER.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_RETURNED_TO_CUSTOMER;
                        graphDataPoints_1['label'] = 'RETURNED_TO_CUSTOMER';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        document.getElementById('totalPendingApproval').innerHTML = TOTAL_PENDING_APPROVAL.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_PENDING_APPROVAL;
                        graphDataPoints_1['label'] = 'PENDING_APPROVAL';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        document.getElementById('totalEscalated').innerHTML = TOTAL_ESCALATED.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_ESCALATED;
                        graphDataPoints_1['label'] = 'TOTAL_ESCALATED';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        document.getElementById('totalApproved').innerHTML = TOTAL_APPROVED.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_APPROVED;
                        graphDataPoints_1['label'] = 'TOTAL_APPROVED';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        document.getElementById('totalRejected').innerHTML = TOTAL_REJECTED.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_REJECTED;
                        graphDataPoints_1['label'] = 'TOTAL_REJECTED';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        document.getElementById('totalSubmitted').innerHTML = TOTAL_SUBMITTED.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_SUBMITTED;
                        graphDataPoints_1['label'] = 'TOTAL_SUBMITTED';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        document.getElementById('totalDraft').innerHTML = TOTAL_DRAFT.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_DRAFT;
                        graphDataPoints_1['label'] = 'TOTAL_DRAFT';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        document.getElementById('totalResend').innerHTML = TOTAL_RESEND.toString();
                        graphDataPoints_1 = {};
                        graphDataPoints_1['y'] = TOTAL_RESEND;
                        graphDataPoints_1['label'] = 'TOTAL_RESEND';
                        this.graphDataPoints1.push(graphDataPoints_1);

                        infoDisplay = '';
                        // this.drawPie();
                        }),
                    (res: HttpResponse<any>) => {
                        this.toastr.error('Error processing request');
                    }
                );
            } // end for
        } // end if (docment.getElementById('SubmissionsInfo').innerHTML === '' )
        console.clear();
        console.log('this.graphDataPoints1.length: '+this.graphDataPoints1.length);
        if (this.graphDataPoints1.length > 0 ) {
            // console.log(this.graphDataPoints1.length + ' ************************************ :) graphDataPoints1 ********************************');
            // console.log(this.graphDataPoints1);
            this.canvasJs();
        }
        */
    } // end function
}
