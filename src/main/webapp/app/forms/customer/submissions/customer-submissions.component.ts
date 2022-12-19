import {Component, OnInit, ViewChild} from '@angular/core';
import * as moment from 'moment';
import {ActivatedRoute, Router} from '@angular/router';
import {CustomerFormService} from 'app/forms/customer/customer-form.service';
import {HttpResponse} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {Principal} from 'app/core';
import {FormStatus} from 'app/shared/model';
import {CustomGridComponent} from 'app/shared/grid/custom-grid.component';

@Component({
    templateUrl: './customer-submissions.component.html',
    providers: [CustomerFormService],
    styles: [`
    .panel {
      margin:5px 15px 0px 15px;
      padding: 15px;
      border-radius: 5px;
      display: block;
      width: 100%;
      background-color: #eaeaea;
      overflow: hidden;
    }
`]
})
export class CustomerSubmissionsComponent implements OnInit {
    public forms: any;
    private currentAccount: any;
    public isFiltering = false;
    public rows: any[] = [];
    public columns: any[] = [];

    @ViewChild(CustomGridComponent, { static: true }) gridComponent: CustomGridComponent;

    constructor(
        public router: Router,
        public activated: ActivatedRoute,
        public service: CustomerFormService,
        private principal: Principal,
        private toastr: ToastrService
    ) {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            // this.fetchSubmissions();
        });
    }

    ngOnInit() {
        this.gridComponent.loadData = () => this.fetchSubmissions();
        this.gridComponent.loadData();
    }

    fetchSubmissions() {
        if (this.service) {
            this.service
                .getSubmittedList({
                    status: FormStatus.SUBMITTED,
                    page: this.gridComponent.page - 1,
                    size: this.gridComponent.itemsPerPage,
                    sort: this.gridComponent.sort(),
                    filters: this.gridComponent.filters
                })
                .subscribe(
                    (res: HttpResponse<any>) => {
                        if (this.columns.length === 0) {
                            this.columns = [];
                            this.columns.push({ label: 'ID', key: 'id' });
                            this.columns.push({ label: 'Title', key: 'title' });
                            this.columns.push({ label: 'Status', key: 'status' });
                            this.columns.push({ label: 'Created Date', key: 'creationDate' });
                            this.columns.push({ label: 'Last Updated', key: 'lastUpdate' });
                        }
                        const grid = [];
                        this.rows = res.body.rows;
                        this.rows.forEach(row => {
                           row['creationDate'] = moment(row['creationDate']).format('MMM DD, YYYY');
                           row['lastUpdate'] = moment(row['lastUpdate']).format('MMM DD, YYYY');
                           row['title'] = row['form']['title'];
                        });
                        res.body.rows.forEach((item: any) => {
                            grid.push({
                                id: item['id'],
                                form: item['form']['title'],
                                created: moment(item['creationDate']).format('MMM DD, YYYY')
                            });
                            this.forms = grid;
                        });
                    },
                    (res: HttpResponse<any>) => this.toastr.error('Error processing request')
                );
        }
    }

    toggleFilter = () => this.gridComponent.filterOptionsComponent.showAccordion();

    openRow(form) {
        this.router.navigate(['/forms/detail/', form.id]);
    }
}
