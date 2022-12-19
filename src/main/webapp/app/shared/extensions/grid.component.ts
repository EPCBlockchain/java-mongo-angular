import { Router, ActivatedRoute } from '@angular/router';
import { JhiParseLinks } from 'ng-jhipster';

export class GridExtension {
    error: any;
    success: any;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage = 10;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(protected router: Router, public gridLink: String, protected activated: ActivatedRoute, public service: any) {
        // this.routeData = this.activated.data.subscribe(data => {
        //     this.page = data['pagingParams'].page;
        //     this.previousPage = data['pagingParams'].page;
        //     this.reverse = data['pagingParams'].ascending;
        //     this.predicate = data['pagingParams'].predicate;
        //     this.loadData();
        // });
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    transition() {
        this.router.navigate([this.gridLink], {
            queryParams: {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadData();
    }

    loadData() {}

    trackIdentity(index, item: any) {
        return item.id;
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    protected gridOnSuccess(res, callback: Function) {
        // this.links = this.parseLinks.parse(res.headers.get('link'));
        this.totalItems = res.headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        callback(res.body);
    }

    protected gridOnError(error) {
        console.log(error);
    }
}
