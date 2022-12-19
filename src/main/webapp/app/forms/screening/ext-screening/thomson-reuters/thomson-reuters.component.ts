import { Component, OnChanges, Input } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
    templateUrl: './thomson-reuters.component.html',
    styleUrls: ['./thomson-reuters.component.scss'],
    selector: 'jhi-thomson-reuters-screening'
})
export class ThomsonReutersScreeningComponent implements OnChanges {
    @Input() public inputResult: any;

    public thomsonResults: any = [];
    public thomsonResultsForView: any = [];

    public referenceIds: any = [];
    public thomsonDetail: any = null;
    public thomsonTotalItems;
    public thomsonPage;
    public thomsonItemsPerPage = 10;

    constructor(private http: HttpClient) {

    }

    ngOnChanges() {
        console.log(this.inputResult);
        if (this.inputResult) {
            this.referenceIds = [];
            this.thomsonResults = [];
            this.thomsonPage = 1;
            this.thomsonTotalItems = this.inputResult.length;

            for (let i = 0; i < this.inputResult.length; i++) {
                this.referenceIds.push({
                    label: this.inputResult[i].referenceId + ' - [' + this.inputResult[i].matchedTerm + ']',
                    value: i
                });

                this.thomsonResults.push({
                    id: i, // this key is for testing only
                    referenceId: this.inputResult[i].referenceId,
                    categories: this.inputResult[i].categories,
                    matchStrength: this.inputResult[i].matchStrength,
                    matchedTerm: this.inputResult[i].matchedTerm
                });
            }

            this.loadThomsonTable(this.thomsonPage);
        }
    }

    public test(id) {
        this.http.get(`/api/forms/external-screening/thomson/${id}`).subscribe(test => {
            console.log(test);
        });
    }

    public loadThomsonTable(page) {
        this.thomsonResultsForView = [];

        for (let i = 0; i < this.thomsonItemsPerPage; i++) {
            const j = (page - 1) * this.thomsonItemsPerPage + i;

            if (j >= this.thomsonTotalItems) {
                break;
            }

            this.thomsonResultsForView.push(this.thomsonResults[j]);
        }
    }

    public setThomsonItemsPerPage(total) {
        this.thomsonItemsPerPage = total;
        this.loadThomsonTable(this.thomsonPage);
    }
}
