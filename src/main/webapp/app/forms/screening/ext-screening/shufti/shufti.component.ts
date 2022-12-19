import {AfterViewInit, Component, Input, OnChanges} from '@angular/core';

@Component({
    templateUrl: './shufti.component.html',
    styleUrls: ['./shufti.component.scss'],
    selector: 'jhi-shufti-screening'
})
export class ShuftiScreeningComponent implements AfterViewInit {
    @Input()
    public inputResult: any;

    ngAfterViewInit(): void {
        console.log(this.inputResult);
    }
}
