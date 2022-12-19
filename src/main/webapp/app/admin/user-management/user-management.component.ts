import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITEMS_PER_PAGE } from 'app/shared';
import { Principal, UserService, User, IUser } from 'app/core';
import { UserMgmtDeleteDialogComponent } from './user-management-delete-dialog.component';
import { ToastrService } from 'ngx-toastr';

import { HttpClient, HttpHeaders } from '@angular/common/http';


@Component({
    selector: 'jhi-user-mgmt',
    templateUrl: './user-management.component.html',
    styles: [
        `
            .panel {
                margin: 5px 15px 0px 15px;
                padding: 15px;
                border-radius: 5px;
                display: block;
                width: 100%;
                background-color: #eaeaea;
                overflow: hidden;
            }
        `
    ]
})
export class UserMgmtComponent implements OnInit, OnDestroy {

    username: string;
    email: string;
    role: string;
    id: any;

    currentAccount: any;
    screeningSubmissionsData = {};
    statusOri = ['', '', 'Returned to Customer', 'Pending Approval', 'Escalated', 'Approved', 'Rejected', 'Submitted', 'Draft', 'Resend'];
    statusI = [];
    status = [];
    users: User[];
    users2: User[];
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    bandInfoLenght: boolean = false;
    timeLeft: number = 60; 
    interval;
    public isFiltering = false;

    idFilter:string;
    name1Filter:string;
    name2Filter:string;
    name3Filter:string;
    name4Filter:string;
    statusFilter:string;
    creationDate:string;
    lastUpdated:string;
    filterAction = false;

    startTimer() {
        if ( this.bandInfoLenght === false ) {
            this.interval = setInterval(() => {
                if ( this.bandInfoLenght === true ) {
                    this.pauseTimer();
                }
                if(this.timeLeft > 0) {
                    this.timeLeft--;
                } else {
                    this.timeLeft = 60;
                }
                // Verify screeningSubmissionsData have data
                if ( Object.keys(this.screeningSubmissionsData).length > 0 ) {
                    this.loadAll();
                    this.bandInfoLenght = true;
                    this.pauseTimer();
                } else {
                    console.log( 'startTimer :: ' + Object.keys(this.screeningSubmissionsData).length );
                }
            },1000);    
        }
    }

    pauseTimer() {
        clearInterval(this.interval);
    }

    constructor(
        private userService: UserService,
        private alertService: JhiAlertService,
        private principal: Principal,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService,
        private eventManager: JhiEventManager,
        private modalService: NgbModal,
        protected http: HttpClient
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
        const headers = new HttpHeaders().set('Content-Type', 'application/json');
        this.http.get(`/api/SS/`,
        {headers})
        .subscribe(
            val => {
                // console.log('GET call successful value returned in body');
                this.screeningSubmissionsData = val;
            },
            response => {
                console.log('GET call in error', response);
                if (response.status === 200) {

                    // if ( Object.keys(this.screeningSubmissionsData).length === 0 ) {
                    console.log( '********* Object.keys ******* : ' + Object.keys(this.screeningSubmissionsData).length );
                    // } else 

                    console.log('GET OK', response.status);
                } else {
                    // console.log('GET ERROR', response.status);
                }
            },
            () => {
                // console.log('The GET observable is now completed.');
            }
        );

        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInUsers();
        });

        if ( Object.keys(this.screeningSubmissionsData).length === 0 ) {
            this.startTimer();
        }
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
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
        
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.loadAll();
            this.registerChangeInUsers();
        });

        // this.queryOrganizationList();
    }

    registerChangeInUsers() {
        this.eventManager.subscribe('userListModification', response => this.loadAll());
    }

    setActive(user, isActivated) {
        user.activated = isActivated;

        this.userService.update(user).subscribe(response => {
            if (response.status === 200) {
                this.error = null;
                this.success = 'OK';
                this.loadAll();
            } else {
                this.success = null;
                this.error = 'ERROR';
            }
        });
    }

    loadAll() {
        this.userService
            .query({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
            );
    }

    trackIdentity(index, item: User) {
        return item.id;
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/admin/user-management'], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    deleteUser(user: User) {
        const modalRef = this.modalService.open(UserMgmtDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
        modalRef.componentInstance.user = user;
        modalRef.result.then(
            result => {
                this.toastr.success('User Deleted');
            },
            reason => {
                // Left blank intentionally, nothing to do here
            }
        );
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        
        this.users2 = data;
        this.users = data;
        let pos = 0;
        if ( this.currentAccount.authorities[0] !== 'ROLE_ADMIN' ) {
            for ( let i = 0; i < this.users2.length; i++ ) {
                if ( this.users2[i]['organizationId'] === String(this.currentAccount.organizationId) ) {
                    this.users[pos] = this.users2[i];
                    pos++;
                }
            }
        } else {
            this.users = data;
        }
        

        let statusList = '';
        let statusListArray = [];
        let date_i = '';
        let infoDate_i = '';
        let table_i = '';
        for ( let i = 0; i < this.users.length ; i++ ) {
            statusList = '';
            statusListArray = [];
            table_i = '';
            for ( let j = 0 ; j < Object.keys(this.screeningSubmissionsData).length ;  j++ ) {
                if ( this.users[i]['id'] === this.screeningSubmissionsData[j]['user_id'] )  {
                    statusList = this.screeningSubmissionsData[j]['status'];
                    date_i = this.screeningSubmissionsData[j]['last_update'];

                    if ( String(date_i['hourOfDay']).length === 1 ) {
                        date_i['hourOfDay'] = '0' + date_i['hourOfDay'];
                    }
                    if ( String(date_i['secondOfMinute']).length === 1 ) {
                        date_i['secondOfMinute'] = '0' + date_i['secondOfMinute'];
                    }
                    if ( String(date_i['hourOfDay']).length === 1 ) {
                        date_i['hourOfDay'] = '0' + date_i['hourOfDay'];
                    }
                    if ( String(date_i['monthOfYear']).length === 1 ) {
                        date_i['monthOfYear'] = '0' + date_i['monthOfYear'];
                    }
                    if ( String(date_i['dayOfMonth']).length === 1 ) {
                        date_i['dayOfMonth'] = '0' + date_i['dayOfMonth'];
                    }

                    infoDate_i = date_i['yearOfEra'] + '-' + date_i['monthOfYear'] + '-' + date_i['dayOfMonth'] + ' ';
                    infoDate_i = infoDate_i + date_i['hourOfDay'] + ':' + date_i['minuteOfHour'] + ':' +  date_i['secondOfMinute'];
                    table_i = '<tr><td>' + this.screeningSubmissionsData[j]['status'] + '</td><td>&nbsp;' + infoDate_i + '</td><td>&nbsp;';
                    table_i = table_i + this.searchUserId(this.screeningSubmissionsData[j]['last_update_by']) + '&nbsp;</td><td>&nbsp;';
                    table_i = table_i + this.screeningSubmissionsData[j]['remarks'] + '</td></tr>';
                    statusListArray.push( table_i );
                }
            }
            this.users[i]['status'] = statusList;
            if ( statusListArray.length > 0 ) {
                let infoTable: any;
                infoTable = "<table border='1' style='padding:10px' ><tr style='background-color:#b0afaf;font-weight:bold;' >";
                infoTable = infoTable + '<td>Status</td><td>Date / Time</td><td>Last update by</td><td>Remarks</td></tr>';
                infoTable = infoTable + statusListArray.join() + '</table>';
                this.users[i]['statusAllArray'] = infoTable;
            } else {
                this.users[i]['statusAllArray'] = '';
            }
            /*
            console.log( "******** this.users ********" );
            console.log( this.currentAccount );
            console.log( this.currentAccount.authorities[0] );
            console.log( this.users );
            console.log( "******** this.users ********" );*/
        }



        // ************************************ Filter ***************************************
        let USERS = this.users;
        this.users = [];
        pos = 0;
        let n1 = 0;
        let n2 = -1;
        let n2_2 = -1;
        let n2_3 = -1;
        let n2_4 = -1;
        let n3 = 0;
        let n4 = 0;
        let n5 = 0;
        console.clear();
        console.log( "this.filterAction: "+ this.filterAction );
        console.log( "this.idFilter: "+ this.idFilter );
        console.log( "this.statusFilter: "+ this.statusFilter );
         
        console.log( USERS );
        if ( this.filterAction === true ) {
            for ( let i = 0 ; i < USERS.length ; i++ ) {    
                n1 = 0;
                n2 = -1;
                n2_2 = -1;
                n2_3 = -1;
                n3 = 0;
                n4 = 0;
                n5 = 0;

                // filter id
                if ( this.idFilter === USERS[i].id  ) {
                    n1 = 1;
                }

                // filter text
                let str = USERS[i].login; 
                str = str.toLowerCase();
                if ( this.name1Filter !== "" ) {
                    n2 = str.indexOf(this.name1Filter);
                }

                let str2 = USERS[i].firstName; 
                str2 = str2.toLowerCase();
                if ( this.name2Filter !== "" ) {
                    n2_2 = str2.indexOf(this.name2Filter);
                }

                let str3 = USERS[i].lastName; 
                str3 = str3.toLowerCase();
                if ( this.name1Filter !== "" ) {
                    n2_3 = str3.indexOf(this.name3Filter);
                }

                let str4 = USERS[i].email; 
                str4 = str4.toLowerCase();
                if ( this.name4Filter !== "" ) {
                    n2_4 = str4.indexOf(this.name4Filter);
                }

                // filter status
                if ( this.statusFilter === 'true' && true === USERS[i].activated ) {
                    n3 = 1;
                }
                if ( this.statusFilter === 'false' && false === USERS[i].activated ) {
                    n3 = 1;
                }
                if ( this.statusFilter === 'all') {
                    n3 = 1;
                }

                // filter creationDate
                // if ( this.creationDate !=="" && data[i].creationDate!="" && this.formatDate(data[i].creationDate) === this.creationDate ) {
                let dateString = String(USERS[i].createdDate); // 2020-06-16T08:13:37.467Z
                var dateStringArray = dateString.split("T");
                console.log( dateStringArray[0] + " === " + this.creationDate );
                if ( dateStringArray[0] === this.creationDate ) {    
                    n4 = 1;   
                }
                 

                // if filter found, set the rows to show
                if ( n1 >= 1 || n2 >= 0  || n2_2 >= 0 || n2_3 >= 0 || n2_4 >= 0 || n3 > 0 || n4 > 0 ) {
                    this.users[pos++] = USERS[i]; 
                }
            }
        } else {
            this.users = USERS;
        } 


    }

    private searchUserId(idUser) {
        for ( let i = 0; i < this.users.length ; i++ ) {
            if ( idUser === this.users[i]['id'] ) {
                return this.users[i]['email'];
            }
        }
    }

    public infoHistoryStatusJs(pos) {
        document.getElementById('infoTable1').style.display = 'none';
        document.getElementById('infoTable2').style.display = 'none';
        let infoHtml = '';
        const infoArray = this.users[pos]['statusAllArray'];
        infoHtml = '<h2>Id: ' + this.users[pos]['id'] + '<br>';
        infoHtml = infoHtml + 'Login: ' + this.users[pos]['login'] + '<br>';
        infoHtml = infoHtml + 'Email: ' + this.users[pos]['email'] + '</h2>';
        document.getElementById('modal').style.display = 'block';
        document.getElementById('infoHistoryStatus').innerHTML = infoHtml + infoArray;
    }

    public cancelModal() {
        document.getElementById('infoTable1').style.display = 'block';
        document.getElementById('infoTable2').style.display = 'block';
        document.getElementById('modal').style.display = 'none';
    }

    private onError(error) {
        this.alertService.error(error.error, error.message, null);
    }

    showAccordion() {
        this.isFiltering = !this.isFiltering;
    }

    navigate(user: IUser) {
        this.router.navigate(['/admin/user-management', user.login, 'view']);
    }
}
