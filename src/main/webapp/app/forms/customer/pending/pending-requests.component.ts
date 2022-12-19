import { Component } from '@angular/core';
import * as moment from 'moment';
import { GridExtension } from 'app/shared/extensions/grid.component';
import { ActivatedRoute, Router } from '@angular/router';
import { Principal } from 'app/core';
import { ToastrService } from 'ngx-toastr';
import { HttpResponse } from '@angular/common/http';
import { CustomerFormService } from 'app/forms/customer/customer-form.service';
import { FormStatus } from 'app/shared/model';

@Component({
    templateUrl: './pending-requests.component.html',
    providers: [CustomerFormService]
})
export class PendingRequestsComponent extends GridExtension {
    private currentAccount: any;
    public forms: any;

    constructor(
        public router: Router,
        public activated: ActivatedRoute,
        public service: CustomerFormService,
        private principal: Principal,
        private toastr: ToastrService
    ) {
        super(router, '', activated, service);
        this.loadData();
    }

    loadData() {
        if (this.service) {
            this.service
                .getSubmittedList({
                    status: FormStatus.DRAFT,
                    page: this.page - 1,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<any>) => {
                        console.log(res.body);
                        const grid = [];
                        (res.body.rows).forEach((item: any) => {
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

    openRow(form) {
        this.router.navigate(['/forms/detail/', form.id]);
    }
}
