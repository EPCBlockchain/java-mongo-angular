import { Component, ViewChild, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpResponse } from '@angular/common/http';
import { SubmissionsService } from 'app/forms/submissions/submissions.service';
import { CustomGridComponent } from 'app/shared/grid/custom-grid.component';
import * as moment from 'moment';
import {NgxSpinnerService} from 'ngx-spinner';

@Component({
    templateUrl: './submissions.component.html',
    styleUrls: ['./submissions.component.scss'],
    providers: [SubmissionsService]
})
export class FormSubmissionsComponent implements OnInit {
    public submissions: any = [];
    public formId: string;
    public form: any;
    public showKeys: any;

    public fields: any[] = [];
    public rows: any[] = [];

    public columns: any[] = [];

    public isKYCSubmission = false;
    public screeningType = 'shufti';

    @ViewChild(CustomGridComponent, { static: true }) customGrid: CustomGridComponent;
    // @ViewChild('SubmissionsInfo', { static: true }) SubmissionsInfo: SubmissionsInfo;
    // @ViewChild('SubmissionsInfo') SubmissionsInfo: ElementRef;

    constructor(
        public service: SubmissionsService,
        protected router: Router,
        private toastr: ToastrService,
        protected activated: ActivatedRoute,
        private spinnerService: NgxSpinnerService
    ) {
        this.activated.params.subscribe(param => { this.formId = param['formId']; },
            () => { this.router.navigateByUrl('/not-found'); }
        );
    }

    back = () => history.back();

    ngOnInit() {
        this.customGrid.addRowClass = item => this.addNewRowClass(item);
        this.customGrid.addTDClass = item => this.addNewTDClass(item);
        this.customGrid.loadData = () => this.getSubmissions();
        this.customGrid.loadData();

    }

    ngAfterViewInit() {
        // console.log("---ngAfterViewInit() Demo---");
        // this.SubmissionsInfo.nativeElement.innerHTML = '1 asdasdasdas asas<br>2 asdasdasdas asas';
        // document.getElementById('SubmissionsInfo').innerHTML = '1 asdasdasdas asas<br>2 asdasdasdas asas';
        // console.log( this.SubmissionsInfo.nativeElement.innerHTML );
        // alert('ngAfterViewInit');
    }

    addNewRowClass(item: any) {
        return item ? item.status : '';
    }

    addNewTDClass(item: any) {
        return item ? item.key : '';
    }

    public getSubmissions() {
        this.submissions = [];
        this.spinnerService.show();
        this.service.getSubmissions(this.formId, {
                page: this.customGrid.page - 1,
                size: this.customGrid.itemsPerPage,
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

                    let bandFormStatic = false;
                    // Form Static to API: EDLX
                    if( this.formId === "615467de527bf300012631c9" ) {

                            bandFormStatic = true;

                            if (this.columns.length === 0) {
                                this.columns = [];
                                this.columns.push({ label: 'ID' , key: 'id' });
                                this.columns.push({ label: 'Status', key: 'status' });
                                this.columns.push({ label: 'Datos Api', key: 'dataApi' });
                                // this.columns.push({ label: 'IdForm', key: '_IdForm_' });
                                this.columns.push({ label: 'Name', key: '_Name_' });
                                this.columns.push({ label: 'SurName', key: '_SurName_' });
                                this.columns.push({ label: 'IdentificationNumber', key: '_IdentificationNumber_' });
                                this.columns.push({ label: 'Email', key: '_Email_' });
                                this.columns.push({ label: 'PublicKey', key: '_PublicKey_' });
                                if (!this.isKYCSubmission) {
                                    this.columns.push({ label: 'Remarks', key: 'remarks' });
                                }
                                this.columns.push({ label: 'Created Date', key: 'creationDate' });
                                this.columns.push({ label: 'Last Updated', key: 'lastUpdate' });
                            }
                            this.rows = submissions.rows.map(row => {
                                const newRow = {};
                                this.columns.forEach(column => {
                                    if (column.key === '_Name_') {
                                        newRow[column.key] = row["dataFormio"].e9neck6;
                                    } else if (column.key === '_SurName_') {
                                        newRow[column.key] = row["dataFormio"].ekx0q1p;
                                    } else if (column.key === '_IdentificationNumber_') {
                                        newRow[column.key] = row["dataFormio"].efjjuau;
                                    } else if (column.key === '_Email_') {
                                        newRow[column.key] = row["dataFormio"].ena1q9d;
                                    } else if (column.key === '_PublicKey_') {
                                        newRow[column.key] = row["dataFormio"].etmftam;
                                    } else if (column.key === 'creationDate' || column.key === 'lastUpdate' || column.key === 'lastUpdated') {
                                        newRow[column.key] = moment(row[column.key]).format('MMM DD, YYYY hh:mm');
                                    } else if (row[column.key]) {
                                        newRow[column.key] = row[column.key];
                                    } else {
                                        newRow[column.key] = row.data[column.key];
                                    }
                                });
                                
                                return newRow;
                            });
                            console.log( "this.rows[i] : " );
                            for ( let i = 0 ; i < this.rows.length ; i++ ) {
                                console.log ( this.rows[i].status );
                                if ( this.rows[i].status === 'RESEND' ) {
                                    this.rows[i].status = 'RETURNED TO CUSTOMER';
                                }
                            }
                            this.rows.map(row => {
                                this.columns.forEach(column => row[column.key] = row[column.key]);
                            });
                            this.spinnerService.hide();

                    }

                    // Form Static to API: Mall Nuestro
                    if( this.formId === "6153a4f4527bf300012631c8" ) {

                        bandFormStatic = true;

                        if (this.columns.length === 0) {
                            this.columns = [];
                            this.columns.push({ label: 'ID' , key: 'id' });
                            this.columns.push({ label: 'Status', key: 'status' });
                            this.columns.push({ label: 'Datos Api', key: 'dataApi' });
                            // this.columns.push({ label: 'IdForm', key: '_IdForm_' });
                            this.columns.push({ label: 'Name', key: '_Name_' });
                            this.columns.push({ label: 'SurName', key: '_SurName_' });
                            this.columns.push({ label: 'Identification o Rif', key: '_IdentificationNumber_' });
                            this.columns.push({ label: 'Email', key: '_Email_' });
                            if (!this.isKYCSubmission) {
                                this.columns.push({ label: 'Remarks', key: 'remarks' });
                            }
                            this.columns.push({ label: 'Created Date', key: 'creationDate' });
                            this.columns.push({ label: 'Last Updated', key: 'lastUpdate' });
                        }
                        this.rows = submissions.rows.map(row => {
                            const newRow = {};
                            this.columns.forEach(column => {
                                if (column.key === '_Name_') {
                                    newRow[column.key] = row["dataFormio"].e5i6bs;
                                } else if (column.key === '_SurName_') {
                                    newRow[column.key] = row["dataFormio"].e4kpbp;
                                } else if (column.key === '_IdentificationNumber_') {
                                    newRow[column.key] = row["dataFormio"].efjjuau;
                                } else if (column.key === '_Email_') {
                                    newRow[column.key] = row["dataFormio"].er9vyit;
                                } else if (column.key === 'creationDate' || column.key === 'lastUpdate' || column.key === 'lastUpdated') {
                                    newRow[column.key] = moment(row[column.key]).format('MMM DD, YYYY hh:mm');
                                } else if (row[column.key]) {
                                    newRow[column.key] = row[column.key];
                                } else {
                                    newRow[column.key] = row.data[column.key];
                                }
                            });
                            
                            return newRow;
                        });
                        console.log( "this.rows[i] : " );
                        for ( let i = 0 ; i < this.rows.length ; i++ ) {
                            console.log ( this.rows[i].status );
                            if ( this.rows[i].status === 'RESEND' ) {
                                this.rows[i].status = 'RETURNED TO CUSTOMER';
                            }
                        }
                        this.rows.map(row => {
                            this.columns.forEach(column => row[column.key] = row[column.key]);
                        });
                        this.spinnerService.hide();

                    }
                    
                    // Not is Form Static of API     
                    if ( bandFormStatic === false ) { 
                            if (this.columns.length === 0) {
                                this.columns = [];
                                this.columns.push({ label: 'ID' , key: 'id' });
                                this.columns.push({ label: 'Status', key: 'status' });
                                this.columns.push({ label: 'Datos Api', key: 'dataApi' });
                                this.columns.push(...submissions.columns);
                                if (!this.isKYCSubmission) {
                                    this.columns.push({ label: 'Remarks', key: 'remarks' });
                                }
                                this.columns.push({ label: 'Created Date', key: 'creationDate' });
                                this.columns.push({ label: 'Last Updated', key: 'lastUpdate' });
                            }
                            this.rows = submissions.rows.map(row => {
                                const newRow = {};
                                this.columns.forEach(column => {
                                    if (column.key === 'creationDate' || column.key === 'lastUpdate' || column.key === 'lastUpdated') {
                                        newRow[column.key] = moment(row[column.key]).format('MMM DD, YYYY hh:mm');
                                    } else if (row[column.key]) {
                                        newRow[column.key] = row[column.key];
                                    } else {
                                        newRow[column.key] = row.data[column.key];
                                    }
                                });
                                
                                return newRow;
                            });
                            console.log( "this.rows[i] : " );
                            for ( let i = 0 ; i < this.rows.length ; i++ ) {
                                console.log ( this.rows[i].status );
                                if ( this.rows[i].status === 'RESEND' ) {
                                    this.rows[i].status = 'RETURNED TO CUSTOMER';
                                }
                            }
                            this.rows.map(row => {
                                this.columns.forEach(column => row[column.key] = row[column.key]);
                            });
                            this.spinnerService.hide();
                    }
                    
                    
                }),
                (res: HttpResponse<any>) => {
                    this.toastr.error('Error processing request');
                    this.spinnerService.hide();
                }
            );
    }

    reDirectToScreening(submission: any) {
        if (this.isKYCSubmission) {
            this.router.navigateByUrl('/forms/submissions/' + submission.id);
        } else {
            this.router.navigateByUrl(`/forms/screening/${submission.id}`);
        }
    }

    screenSelected() {
        const selected = this.customGrid.getSelected();
        const data = {
            submittedIds: selected.map(sel => sel.id),
            screeningType: this.screeningType
        };
        if (selected.length) {
            this.service.batchScreening(data).subscribe(result => {
                this.toastr.success('Screening Completed');
            }, error => {
                console.log(error);
            });
        } else {
            this.toastr.warning('None selected');
        }
    }
    downloadFile(data: any) {
        let parsedResponse = '';

        (data as any[]).forEach(item => {
            parsedResponse += item + '\n';
        });
        const blob = new Blob([parsedResponse], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.href = url;
        a.download = this.form.title + '.csv';
        a.click();
        a.remove();
    }
    downloadCsvFromLink() {
        this.service.downLoadScreeningData(this.formId).subscribe(response => {
            this.downloadFile(response);
        });
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
        if (document.getElementById('SubmissionsInfo').innerHTML === '' ) {
            let valuesStatusArray = [];
            let valuesIdStatusArray = [];
            let infoDisplay: string  = '';
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

            this.submissions = [];
            this.spinnerService.show();
            this.service.getSubmissions(this.formId, {
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
                        this.rows = submissions.rows.map(row => {
                            const newRow2 = {};
                            this.columns.forEach(column => {
                                if (column.key === 'creationDate' || column.key === 'lastUpdate' || column.key === 'lastUpdated') {
                                    newRow2[column.key] = moment(row[column.key]).format('MMM DD, YYYY hh:mm');
                                } else if (row[column.key]) {
                                    newRow2[column.key] = row[column.key];
                                } else {
                                    newRow2[column.key] = row.data[column.key];
                                }
                            });
                            console.log( '***************************** newRow2 *****************************' );
                            document.getElementById('SubmissionsInfo').innerHTML = '';
                            // console.log( newRow2 );
                            for (var key in newRow2) {
                                // console.log( key );
                                if (newRow2.hasOwnProperty(key)) {
                                    if (valuesIdStatusArray.includes(newRow2['id']) === false ) {
                                        valuesIdStatusArray.push( newRow2['id'] );
                                        valuesStatusArray.push( newRow2['status'] );
                                    }
                                }
                            }
                            return newRow2;
                        });
                        document.getElementById('SubmissionsInfo').innerHTML = document.getElementById('SubmissionsInfo').innerHTML + valuesIdStatusArray.join('<br>') +'<hr>';
                        document.getElementById('SubmissionsInfo').innerHTML = document.getElementById('SubmissionsInfo').innerHTML + valuesStatusArray.join('<br>') +'<br>';


                        RETURNED_TO_CUSTOMER = APPROVED = PENDING_APPROVAL = DRAFT = SUBMITTED = ESCALATED = RESEND = REJECTED = 0;
                        for (let i = 0; i < valuesStatusArray.length; i++) {
                            if (valuesStatusArray[i] === 'RETURNED TO CUSTOMER') {
                                RETURNED_TO_CUSTOMER++;
                            }
                            if (valuesStatusArray[i] === 'APPROVED') {
                                APPROVED++;
                            }
                            if (valuesStatusArray[i] === 'PENDING_APPROVAL') {
                                PENDING_APPROVAL++;
                            }
                            if (valuesStatusArray[i] === 'DRAFT') {
                                DRAFT++;
                            }
                            if (valuesStatusArray[i] === 'SUBMITTED') {
                                SUBMITTED++;
                            }
                            if (valuesStatusArray[i] === 'ESCALATED') {
                                ESCALATED++;
                            }
                            if (valuesStatusArray[i] === 'RESEND') {
                                RESEND++;
                            }
                            if (valuesStatusArray[i] === 'REJECTED') {
                                REJECTED++;
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

                        infoDisplay = infoDisplay + "<font style='font-weight:bold;'>SUBMISSIONS</font><br><br>";
                        infoDisplay = infoDisplay + 'RETURNED TO CUSTOMER: ' + RETURNED_TO_CUSTOMER_STR + '<br>';
                        infoDisplay = infoDisplay + 'APPROVED: ' + APPROVED_STR + '<br>';
                        infoDisplay = infoDisplay + 'PENDING APPROVAL: ' + PENDING_APPROVAL_STR + '<br>';
                        infoDisplay = infoDisplay + 'DRAFT: ' + DRAFT_STR + '<br>';
                        infoDisplay = infoDisplay + 'SUBMITTED: ' + SUBMITTED_STR + '<br>';
                        infoDisplay = infoDisplay + 'ESCALATED: ' + ESCALATED_STR + '<br>';
                        infoDisplay = infoDisplay + 'RESEND TO CUSTOMER: ' + RESEND_STR + '<br>';
                        infoDisplay = infoDisplay + 'REJECTED: ' + REJECTED_STR + '<br>';
                        document.getElementById('SubmissionsInfo').innerHTML = infoDisplay;


                        /*
                        this.rows.map(row => {
                            this.columns.forEach(column => row[column.key] = row[column.key]);
                        });*/
                        // this.spinnerService.hide();
                    }),
                    (res: HttpResponse<any>) => {
                        this.toastr.error('Error processing request');
                        this.spinnerService.hide();
                    }
                );
        }
    }
    
    /*
    showDatasubmissionsInfoFilter2() {
        if (document.getElementById('SubmissionsInfo').innerHTML === '' ) {
            // var valuesStatus = "";
            // var valuesIdStatus = "";
            let valuesStatusArray = [];
            let valuesIdStatusArray = [];
            let infoDisplay: string  = '';
            let RETURNED_TO_CUSTOMER: number = 0;
            let APPROVED: number = 0;
            let PENDING_APPROVAL: number = 0;
            let DRAFT: number = 0;
            let SUBMITTED: number = 0;
            let ESCALATED: number = 0;
            let RESEND: number = 0;
            let REJECTED: number = 0;

            this.submissions = [];
            this.spinnerService.show();
            this.service.getSubmissions(this.formId, {
                    page: this.customGrid.page - 1,
                    size: this.customGrid.itemsPerPage,
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
                        this.rows = submissions.rows.map(row => {
                            const newRow2 = {};
                            this.columns.forEach(column => {
                                if (column.key === 'creationDate' || column.key === 'lastUpdate' || column.key === 'lastUpdated') {
                                    newRow2[column.key] = moment(row[column.key]).format('MMM DD, YYYY hh:mm');
                                } else if (row[column.key]) {
                                    newRow2[column.key] = row[column.key];
                                } else {
                                    newRow2[column.key] = row.data[column.key];
                                }
                            });
                            console.log( '***************************** newRow2 *****************************' );
                            document.getElementById('SubmissionsInfo').innerHTML = '';
                            // console.log( newRow2 );
                            for (var key in newRow2) {
                                // console.log( key );
                                if (newRow2.hasOwnProperty(key)) {
                                    // console.log( key + ' -> ' + newRow2[key] );
                                    // console.log( newRow2['status'] + " " + newRow2['id'] );
                                    // valuesStatus = valuesStatus + newRow2['status'];
                                    // valuesIdStatus = valuesIdStatus + newRow2['id'];

                                    if (valuesIdStatusArray.includes(newRow2['id']) === false ) {
                                        valuesIdStatusArray.push( newRow2['id'] );
                                        valuesStatusArray.push( newRow2['status'] );
                                    }
                                }
                            }
                            // console.log(values);
                            return newRow2;
                        });
                        // document.getElementById('SubmissionsInfo').innerHTML = document.getElementById('SubmissionsInfo').innerHTML + valuesStatus+ " "+ valuesIdStatus +'<br>';
                        document.getElementById('SubmissionsInfo').innerHTML = document.getElementById('SubmissionsInfo').innerHTML + valuesIdStatusArray.join('<br>') +'<hr>';
                        document.getElementById('SubmissionsInfo').innerHTML = document.getElementById('SubmissionsInfo').innerHTML + valuesStatusArray.join('<br>') +'<br>';


                        //valuesStatusArray.forEach(element => console.log(element));
                        for (let i = 0; i < valuesStatusArray.length; i++) {
                            if (valuesStatusArray[i] === 'RETURNED TO CUSTOMER') {
                                RETURNED_TO_CUSTOMER++;
                            }
                            if (valuesStatusArray[i] === 'APPROVED') {
                                APPROVED++;
                            }
                            if (valuesStatusArray[i] === 'PENDING APPROVAL') {
                                PENDING_APPROVAL++;
                            }
                            if (valuesStatusArray[i] === 'DRAFT') {
                                DRAFT++;
                            }
                            if (valuesStatusArray[i] === 'SUBMITTED') {
                                SUBMITTED++;
                            }
                            if (valuesStatusArray[i] === 'ESCALATED') {
                                ESCALATED++;
                            }
                            if (valuesStatusArray[i] === 'RESEND') {
                                RESEND++;
                            }
                            if (valuesStatusArray[i] === 'REJECTED') {
                                REJECTED++;
                            }
                        }

                        if ( RETURNED_TO_CUSTOMER > 0 ) {
                            RETURNED_TO_CUSTOMER = "<font style='font-weight:bold;font-style: italic;'>"+RETURNED_TO_CUSTOMER+"</font>";
                        }
                        if ( APPROVED > 0 ) {
                            APPROVED = "<font style='font-weight:bold;font-style: italic;'>"+APPROVED+"</font>";
                        }
                        if ( PENDING_APPROVAL > 0 ) {
                            PENDING_APPROVAL = "<font style='font-weight:bold;font-style: italic;'>"+PENDING_APPROVAL+"</font>";
                        }
                        if ( DRAFT > 0 ) {
                            DRAFT = "<font style='font-weight:bold;font-style: italic;'>"+DRAFT+"</font>";
                        }
                        if ( SUBMITTED > 0 ) {
                            SUBMITTED = "<font style='font-weight:bold;font-style: italic;'>"+SUBMITTED+"</font>";
                        }
                        if ( ESCALATED > 0 ) {
                            ESCALATED = "<font style='font-weight:bold;font-style: italic;'>"+ESCALATED+"</font>";
                        }
                        if ( RESEND > 0 ) {
                            RESEND = "<font style='font-weight:bold;font-style: italic;'>"+RESEND+"</font>";
                        }
                        if ( REJECTED > 0 ) {
                            REJECTED = "<font style='font-weight:bold;font-style: italic;'>"+REJECTED+"</font>";
                        }

                        infoDisplay = infoDisplay + "<font style='font-weight:bold;'>SUBMISSIONS</font><br><br>";
                        infoDisplay = infoDisplay + 'RETURNED TO CUSTOMER: ' + RETURNED_TO_CUSTOMER + '<br>';
                        infoDisplay = infoDisplay + 'APPROVED: ' + APPROVED + '<br>';
                        infoDisplay = infoDisplay + 'PENDING APPROVAL: ' + PENDING_APPROVAL + '<br>';
                        infoDisplay = infoDisplay + 'DRAFT: ' + DRAFT + '<br>';
                        infoDisplay = infoDisplay + 'SUBMITTED: ' + SUBMITTED + '<br>';
                        infoDisplay = infoDisplay + 'ESCALATED: ' + ESCALATED + '<br>';
                        infoDisplay = infoDisplay + 'RESEND: ' + RESEND + '<br>';
                        infoDisplay = infoDisplay + 'REJECTED: ' + REJECTED + '<br>';
                        document.getElementById('SubmissionsInfo').innerHTML = infoDisplay;


                        
                        //this.rows.map(row => {
                        //    this.columns.forEach(column => row[column.key] = row[column.key]);
                        //});
                        // this.spinnerService.hide();
                    }),
                    (res: HttpResponse<any>) => {
                        this.toastr.error('Error processing request');
                        this.spinnerService.hide();
                    }
                );

        }
    }
    */
}
