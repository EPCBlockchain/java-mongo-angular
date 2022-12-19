import {Component, OnInit, ViewChild} from '@angular/core'
import {CredentialsService} from 'app/credentials/credentials.service'
import {CustomGridComponent} from 'app/shared/grid/custom-grid.component'
import {HttpResponse} from '@angular/common/http'
import * as moment from 'moment'
import {NgxSpinnerService} from 'ngx-spinner'
import {ToastrService} from 'ngx-toastr'
import {Router} from '@angular/router'
import {CredentialEntity} from 'app/shared/model/credential.entity'

// import FileSaver = require('file-saver');
// import {FileSaver} from 'file-saver';
// declare var require: any
const FileSaver = require('file-saver');
// angular-file-saver

@Component({
    templateUrl: './list.component.html'
})
export class CredentialsListComponent implements OnInit {

    isHiddenId = true;
    isHiddenName = true;

    idFilter:string;
    name1Filter:string;
    name2Filter:string;
    name3Filter:string;
    statusFilter:string;
    creationDate:string;
    lastUpdated:string;
    filterAction = false;

    public fields: any[] = [];
    public rows: any[] = [];

    public columns = [{
        label: 'Icon',
        key: 'image',
        imageSrc: 'image',
        width: '70px'
    }, {
        label: 'ID',
        key: 'id',
        width: '250px',
        icon:[
            {   
                hide:this.isHiddenId,
                action:row => this.isHiddenId ? this.sortAscId(row) : this.sortDescId(row)
            }
        ]
    }, {
        label: 'Name',
        key: 'name',
        icon:[
            {   
                hide:this.isHiddenId
            }
        ]
    }, {
        label: 'Description',
        key: 'description',
        icon:[
            {   
                hide:this.isHiddenId
            }
        ]
    }, {
        label: 'Code',
        key: 'code',
        icon:[
            {   
                hide:this.isHiddenId
            }
        ]
    }, {
        label: 'Status',
        key: 'status',
        icon:[
            {   
                hide:this.isHiddenId
            }
        ]
    },
     {
        label: 'Date Created',
        key: 'creationDate',
        width: '300px',
        icon:[
            {   
                hide:this.isHiddenId
            }
        ]
    }];

    public credentials: CredentialEntity[] = [];


    @ViewChild(CustomGridComponent, { static: true }) customGrid: CustomGridComponent;

    constructor(private credentialsService: CredentialsService,
                private spinnerService: NgxSpinnerService,
                private router: Router,
                private toastr: ToastrService) { }


    /* filter() {
        this.customGrid.filterOptionsComponent.showAccordion();
    } */

    filter() {
        if ( document.getElementById('filterFormData').style.display === 'none' ) {
            document.getElementById('filterFormData').style.display = 'block';
        } else {
            document.getElementById('filterFormData').style.display = 'none';
        }
    }

    searchFilter() {
        this.filterAction = true;
        // this.queryOrganizationList();
        this.customGrid.loadData = () => this.getCredentials();
        this.customGrid.loadData();
    }

    ngOnInit(): void {
        this.filterAction = false; 
        this.customGrid.loadData = () => this.getCredentials();
        this.customGrid.loadData();
    }


    sortAscId(row){
        const sortedAsc = this.rows.sort(this.customGrid.sortAlphabetically('id'))
        this.rows = sortedAsc;
        this.isHiddenId = false;
    }

    sortDescId(row){
        const sortedDesc = this.rows.sort(this.customGrid.sortAlphabetically('-id'))
        this.rows = sortedDesc;
        this.isHiddenId = true;
    }

    sortAscName(row){
        const  sortAsc = this.rows.sort(this.customGrid.sortAlphabetically('name'))
        this.rows = sortAsc;
        this.isHiddenName = false;
    }

    sortDescName(row){
        const  sortDesc = this.rows.sort(this.customGrid.sortAlphabetically('-name'))
        this.rows = sortDesc;
        this.isHiddenName = false;
    }

    public getCredentials() {
        this.credentials = [];
        this.spinnerService.show();
        this.credentialsService.getCredentials({
            page: this.customGrid.page - 1,
            size: this.customGrid.itemsPerPage,
            filters: this.customGrid.filters,
            sort: this.customGrid.setSort({
                status: 'asc'
            }, 'asc')
        })
            .subscribe(
                (res: HttpResponse<any[]>) => this.customGrid.gridOnSuccess(res, (credentials: any) => {
                    this.rows = credentials.map(credential => {
                        credential.creationDate = moment(credential.creationDate).format('MMM DD, YYYY HH:mm');

                        return credential;
                    });
                    for ( let i=0 ; i < this.rows.length ; i++ ) {
                        this.rows[i].image = 'https://test.kyc.proximax.io/content/53ee32097333628b8683c9ff0826e474.png';                        
                    }

                    let ROWS = this.rows;
                    this.rows = [];
                    let pos = 0;
                    let n1 = 0;
                    let n2 = -1;
                    let n2_2 = 0;
                    let n2_3 = 0;
                    let n3 = 0;
                    let n4 = 0;
                    let n5 = 0;
                    console.clear();
                    console.log( "this.filterAction: "+ this.filterAction );
                    console.log( "this.idFilter: "+ this.idFilter );
                    console.log( "this.statusFilter: "+ this.statusFilter );
                    
                    if ( this.filterAction === true ) {
                        // this.orgs = [];
                        console.log( ROWS );
                        for ( let i = 0 ; i < ROWS.length ; i++ ) {
                            
                            n1 = 0;
                            n2 = -1;
                            n2_2 = -1;
                            n2_3 = -1;
                            n3 = 0;
                            n4 = 0;
                            n5 = 0;

                            // filter id
                            if ( this.idFilter === ROWS[i].id  ) {
                                n1 = 1;
                            }

                            // filter text
                            let str = ROWS[i].name; 
                            str = str.toLowerCase();
                            if ( this.name1Filter !== "" ) {
                                n2 = str.indexOf(this.name1Filter);
                            }

                            let str2 = ROWS[i].description; 
                            str2 = str2.toLowerCase();
                            if ( this.name2Filter !== "" ) {
                                n2_2 = str2.indexOf(this.name2Filter);
                            }

                            let str3 = ROWS[i].code; 
                            str3 = str3.toLowerCase();
                            if ( this.name2Filter !== "" ) {
                                n2_3 = str3.indexOf(this.name3Filter);
                            }


                            // filter status
                            if ( ROWS[i].status !== "" && this.statusFilter === ROWS[i].status ) {
                                n3 = 1;
                            }
                            if ( this.statusFilter === 'all') {
                                n3 = 1;
                            }

                            // filter creationDate
                            // if ( this.creationDate !=="" && data[i].creationDate!="" && this.formatDate(data[i].creationDate) === this.creationDate ) {
                            if ( this.formatDate(ROWS[i].creationDate) === this.creationDate ) {    
                                n4 = 1;   
                            }

                            // if filter found, set the rows to show
                            if ( n1 >= 1 || n2 >= 0 || n2_2 >= 0 || n2_3 >= 0 || n3 > 0 || n4 > 0 ) {
                                this.rows[pos++] = ROWS[i]; 
                            }
                        }
                        
                    } else {
                        this.rows = ROWS;
                    }


                    this.spinnerService.hide();
                }),
                (res: HttpResponse<any>) => {
                    this.toastr.error('Error processing request');
                    this.spinnerService.hide();
                }
            );
    }

    formatDate(date) {
        let d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;

        return [year, month, day].join('-');
    }

    public redirectToDetail($event) {
        this.router.navigateByUrl('/credentials/' + $event.id)
    }


    generateReportPDF(){
        this.credentialsService.generateReportPDF().subscribe(
            data => {
                // let saveAs = require('file-saver');
                const blob = new Blob([data], { type: 'text/csv' });
                const file = new File([blob], 'credentials' + '.pdf', { type: 'application/pdf' });
                const pdfUrl = './assets/sample.pdf';
                // FileSaver.saveAs(pdfUrl, file);
                FileSaver.saveAs(file);
                // saveAs(file);
            }
        )
    }

    generateReportExcel(){
        this.credentialsService.generateReportExcel().subscribe(
            data => {
                const blob = new Blob([data], { type: 'text/csv' });
                const file = new File([blob], 'credentials' + '.xlsx', { type: 'application/vnd.ms.excel' });
                // FileSaver.saveAs(file);
                FileSaver.saveAs(file);
                // saveAs(file);
            }
        )
    }
}


