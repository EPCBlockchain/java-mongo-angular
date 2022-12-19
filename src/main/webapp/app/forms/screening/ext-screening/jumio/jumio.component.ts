import { Component, Input, OnChanges } from '@angular/core';

@Component({
    templateUrl: './jumio.component.html',
    styleUrls: ['./jumio.component.scss'],
    selector: 'jhi-jumio-screening'
})
export class JumioScreeningComponent implements OnChanges {
    @Input() public inputResult: any;

    public data: any;

    ngOnChanges() {
        if (this.inputResult) {
            console.log(this.inputResult);
        }
    }
}
