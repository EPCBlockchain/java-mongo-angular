import { Component, OnInit,ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { FormEntity, FormType } from 'app/shared/model';
import { CustomGridComponent } from 'app/shared/grid/custom-grid.component';
import { OrganizationEntity } from 'app/shared/model';
import { HttpResponse } from '@angular/common/http';
import { ITEMS_PER_PAGE } from 'app/shared';
import { OrganizationService } from 'app/admin/organizations/organizations.service';
import * as moment from 'moment';
// import FileSaver = require('file-saver');
// import FileSaver from 'file-saver';
const FileSaver = require('file-saver');

@Component({
    templateUrl: './list.component.html',
    styles: [],
    providers: [OrganizationService]
})
export class OrganizationsComponent implements OnInit {
    public FormType = FormType;
    public form: FormEntity = null;
    public orgs: OrganizationEntity[] = [];
    isHidden = true;
    filterAction = false;
    routeData: any;
    error: any;
    success: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    isHiddenId = true;
    isHiddenDate=true;
    filename:string;
    idFilter:string;
    name1Filter:string;
    statusFilter:string;
    creationDate:string;
    lastUpdated:string;

    @ViewChild(CustomGridComponent, { static: true }) customGrid: CustomGridComponent;

    status = [
        {
            stat:'active'
        },
        {
            stat:'inactive'
        },
        {
            stat:'all'
        }
    ]


    constructor(
        private service: OrganizationService,
        private router: Router,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private activatedRoute: ActivatedRoute
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe(data => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    ngOnInit() {
        this.filterAction = false; 
        this.queryOrganizationList();
    }

    filter() {
        if ( document.getElementById('filterFormData').style.display === 'none' ) {
            document.getElementById('filterFormData').style.display = 'block';
        } else {
            document.getElementById('filterFormData').style.display = 'none';
        }
    }

    searchFilter() {
        this.filterAction = true;
        this.queryOrganizationList();
    }


    selectedStatus(event:any){
        const selectedStatus = event.target.value;
        if(selectedStatus === 'active') {
            this.queryOrganizationListActiveStatus();
        } else if(selectedStatus === 'inactive'){
            this.queryOrganizationListInActiveStatus()
        } else if(selectedStatus === 'all'){
            this.queryOrganizationList();
        }
    }


    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    public queryOrganizationList() {
        this.service
            .queryOrganizations({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<OrganizationEntity[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    public queryOrganizationListActiveStatus() {
        this.service
            .queryOrganizations({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<OrganizationEntity[]>) => 
                this.onSuccessActive(res.body, res.headers)
            )
    }


    public queryOrganizationListInActiveStatus() {
        this.service
            .queryOrganizations({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<OrganizationEntity[]>) => 
                this.onSuccessInActive(res.body, res.headers)
            )
    }


    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/admin/orgs/list'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.queryOrganizationList();
    }

    public redirectToDetail(org: OrganizationEntity) {
        this.service.currentData = org;
        console.log(this.service.currentData);
        this.router.navigateByUrl(`/admin/orgs/${org.id}`);
    }

    public redirectToCreate() {
        this.router.navigateByUrl(`/admin/orgs/create`);
    }

    public changeStatus(org: OrganizationEntity) {
        this.service.update(org).subscribe(response => {
            console.log('disable/enable', response);
        });
    }

     onSuccess(data: any[], headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        data.forEach(org => {
            org.creationDate = moment(org.creationDate).format('MMM DD, YYYY');
        });

        let pos = 0;
        let n1 = 0;
        let n2 = -1;
        let n3 = 0;
        let n4 = 0;
        let n5 = 0;
        console.clear();
        console.log( "this.filterAction: "+ this.filterAction );
        console.log( "this.idFilter: "+ this.idFilter );
        console.log( "this.statusFilter: "+ this.statusFilter );
        
        if ( this.filterAction === true ) {
            this.orgs = [];
            for ( let i = 0 ; i < data.length ; i++ ) {
                
                n1 = 0;
                n2 = -1;
                n3 = 0;
                n4 = 0;
                n5 = 0;

                // filter id
                if ( this.idFilter === data[i].id  ) {
                    n1 = 1;
                }

                // filter text
                let str = data[i].name; 
                str = str.toLowerCase();
                if ( this.name1Filter !== "" ) {
                    n2 = str.indexOf(this.name1Filter);
                }

                // filter status
                if ( data[i].status !== "" && this.statusFilter === data[i].status ) {
                    n3 = 1;
                }
                if ( this.statusFilter === 'all') {
                    n3 = 1;
                }

                // filter creationDate
                // if ( this.creationDate !=="" && data[i].creationDate!="" && this.formatDate(data[i].creationDate) === this.creationDate ) {
                if ( this.formatDate(data[i].creationDate) === this.creationDate ) {    
                    n4 = 1;   
                }


                // filter lastUpdated
                // if ( this.lastUpdated !=="" && data[i].lastUpdated!="" && this.formatDate(data[i].lastUpdated) === this.lastUpdated ) {
                /*
                if ( this.formatDate(data[i].lastUpdated) === this.lastUpdated ) {
                    n5 = 1;   
                }*/
                 

                // if filter found, set the rows to show
                if ( n1 >= 1 || n2 >= 0 || n3 > 0 || n4 > 0 ) {
                    this.orgs[pos++] = data[i]; 
                }
            }
        } else {
            this.orgs = data;
        }
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


    onSuccessActive(data: any[], headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        data.forEach(org => {
            org.creationDate = moment(org.creationDate).format('MMM DD, YYYY');
        });
        const arr = data.filter(x => x.status === 'active')
        this.orgs = arr;
    }



    onSuccessInActive(data: any[], headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        data.forEach(org => {
            org.creationDate = moment(org.creationDate).format('MMM DD, YYYY');
        });
        const arr = data.filter(x => x.status === 'inactive')
        this.orgs = arr;
    }




        
    sortAlphabetically(property) {
        var sortOrder = 1;
    
        if(property[0] === "-") {
            sortOrder = -1;
            property = property.substr(1);
        }
    
        return function (a,b) {
            if(sortOrder == -1){
                return b[property].localeCompare(a[property]);
            }else{
                return a[property].localeCompare(b[property]);
            }        
        }
    }


    generatePDF(){
        this.service.generateReportPDF().subscribe(
            data => {
                console.log(data);
                this.downloadFilePDf(data);
            }
        )
    }

    downloadFilePDf(data: any) {
        const blob = new Blob([data], { type: 'text/csv' });
        const file = new File([blob], 'organizations' + '.pdf', { type: 'application/pdf' });
        FileSaver.saveAs(file);
        // saveAs(file);
      }

      downloadFileExcel(data: any) {
        const blob = new Blob([data], { type: 'text/csv' });
        const file = new File([blob], 'organizations' + '.xlsx', { type: 'application/vnd.ms.excel' });
        FileSaver.saveAs(file);
        // saveAs(file);
      }


      generateReportExcel(){
        this.service.generateReportExcel().subscribe(
            data => {
                this.downloadFileExcel(data);
            }
        )
   }





    sortIdAscending(){
        const arr = this.orgs.sort(this.sortAlphabetically('id'));
        this.orgs = arr;
        this.isHiddenId = false;
        
    }

    sortIdDescending(){
        const arr = this.orgs.sort(this.sortAlphabetically('-id'));
        this.orgs = arr;
        this.isHiddenId = true;
        
    }

    sortDateAscending(){
        const sortedActivities = this.orgs.slice().sort((a:any, b:any) => new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime());
        this.orgs = sortedActivities;
        this.isHiddenDate = true;
    }

    sortDateDescending(){
        const sortedActivities = this.orgs.slice().sort((a:any, b:any) => new Date(b.creationDate).getTime() - new Date(a.creationDate).getTime());
        this.orgs = sortedActivities;
        this.isHiddenDate = false;
    }


    sortArrayAscending(){
        const arr = this.orgs.sort(this.sortAlphabetically('name'));
        this.orgs = arr;
        this.isHidden = false;

    }

    sortArrayDescending(){
        const arr = this.orgs.sort(this.sortAlphabetically('-name'));
        this.orgs = arr;
        this.isHidden = true;

    }

    
    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }
}
