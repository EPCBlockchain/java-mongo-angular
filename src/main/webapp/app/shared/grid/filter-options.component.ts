import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';

@Component({
    templateUrl: './filter-options.component.html',
    selector: 'jhi-filter-options',
    styles: [
        `
            .panel {
                margin: 5px 15px 0px 15px;
                padding: 15px;
                border-radius: 5px;
                display: block;
                width: 100%;
                background-color: #f6f6f6;
                overflow: hidden;
            }
        `
    ]
})
export class FilterOptionsComponent implements OnChanges {
    public isFiltering = false;

    public filters: any[];

    @Input()
    public gridColumns: any[];

    @Output()
    public onFilter: EventEmitter<any> = new EventEmitter<any>();

    constructor(private toastr: ToastrService) {}

    ngOnChanges(event) {
        this.filters = this.gridColumns
            .filter(column => !column.buttons)
            .map(column => {
                column['value'] = null;
                return column;
            });
    }

    showAccordion() {
        this.isFiltering = !this.isFiltering;
    }

    onChange(filter, value) {
        filter['value'] = value;
    }

    onEnter(event) {
        if (event.keyCode === 13) {
            this.onFilter.emit(this.filters);
        }
    }

    filterSubmissions() {
        const creationDate = this.filters.find(filter => filter.key === 'creationDate');
        const lastUpdated = this.filters.find(filter => filter.key === 'lastUpdate' || filter.key === 'lastUpdated');
        const dateNow = Date.now();

        if (creationDate || lastUpdated) {
            const creationDateValid = moment(creationDate.value).isValid();
            const lastUpdatedValid = moment(lastUpdated.value).isValid();

            if (
                (!creationDateValid && creationDate.value) ||
                (!lastUpdatedValid && lastUpdated.value)
            ) {
                this.toastr.error('Invalid Date');
                return;
            } else if (lastUpdated.value || creationDate.value) {
                if (new Date(creationDate.value).getTime() > dateNow || new Date(lastUpdated.value).getTime() > dateNow) {
                    this.toastr.error('Invalid Date');
                    return;
                }
            }
        }
        this.onFilter.emit(this.filters);
    }

    clearFilters() {
        this.filters.forEach(filter => {
            filter['value'] = '';
        });
    }
}

// 5ee2aa20f43cff55c55194c4
