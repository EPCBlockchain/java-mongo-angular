import { Router, ActivatedRoute } from '@angular/router';
import { JhiParseLinks } from 'ng-jhipster';
import {Component, Input, OnInit, OnDestroy, Output, EventEmitter, OnChanges, ViewChild} from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { FilterOptionsComponent } from 'app/shared/grid/filter-options.component';
import { HostListener } from '@angular/core';
import * as moment from 'moment';

@Component({
    templateUrl: './custom-grid.component.html',
    selector: 'jhi-custom-grid',
    styles: [`
        input[type=checkbox] {
            width: 16px;
            height: 16px;
            transform: translateY(3px);
        }
        list-holder{
            margin-top:15px;
        }
        .dataList{
            margin:0;
            margin-bottom:0.1em;
            padding:15px 10px 10px 15px;
            border-radius:5px;
            background:#fafafa;
            border: thin solid #DDD;
            font-family: lato;
            list-style-type:none;
            font-size: 10pt;
        }
        label{
            margin:0;
        }
    `]
})
export class CustomGridComponent implements OnDestroy {
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
    gridLink = [];
    filters: any;
    screenHeight;
    screenWidth;
    mobile: 479;
    isMobile: Boolean;

    @Input()
    public columns: any[];

    @Input()
    public rows: any[];

    @Input()
    public selection = false;

    @Output()
    public onOpenRow: EventEmitter<any> = new EventEmitter<any>();

    @ViewChild(FilterOptionsComponent, { static: true }) filterOptionsComponent: FilterOptionsComponent;

    constructor(protected router: Router, private toastr: ToastrService, private parseLinks: JhiParseLinks, private activated: ActivatedRoute) {
        this.routeData = this.activated.data.subscribe(data => {
            this.setParams(data);
            this.loadData();
        });
        this.onResize();
    }

    @HostListener('window:resize', ['$event'])
    onResize(event?) {
      this.screenHeight = window.innerHeight;
      this.screenWidth = window.innerWidth;

      // console.log(this.screenHeight + ' ' + this.screenWidth);
      if (this.screenWidth <= 479) {
          // console.log(this.screenWidth);
          // console.log( 'mobile' );
          this.isMobile = true;
      } else {
          this.isMobile = false;
      }
    }

    private setParams(data) {
        this.page = data['pagingParams'].page;
        this.previousPage = data['pagingParams'].page;
        this.reverse = data['pagingParams'].ascending;
        this.predicate = data['pagingParams'].predicate;
    }

    ngOnDestroy() {
        this.routeData.unsubscribe();
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    select($event, row) {
        row.selected = $event.target.checked;
    }

    getSelected(): any[] {
        return this.rows.filter(row => row.selected);
    }

    addRowClass(item: any) {
        return '';
    }

    addTDClass(item: any) {
        return '';
    }

    transition() {
        this.router.navigate(this.gridLink, {
            queryParams: { page: this.page }
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

    public gridOnSuccess(res, callback: Function) {
        this.links = this.parseLinks.parse(res.headers.get('link'));
        this.totalItems = res.headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        callback(res.body);
    }

    public gridOnError(error) {
        this.toastr.error('Error processing request');
    }

    public openRow(row) {
        this.onOpenRow.emit(row);
    }

    public onFilterSubmissions(filterData: any[]) {
        //        this.filters = filterData;
        this.filters = {};
        filterData.filter(filter => filter['value']).forEach(map => {
            let value = map['value'];
            if (map.key === 'creationDate' || map.key === 'lastUpdate' || map.key === 'lastUpdated') {
                value = moment(map['value']).format('M/D/YY');
            }
            this.filters[map['key']] = value;
        });
        if (!Object.keys(this.filters).length) {
            this.filters = undefined;
        }
        this.loadData();
    }

    public setSort(param: { status: string }, asc: string) {

    }
}
