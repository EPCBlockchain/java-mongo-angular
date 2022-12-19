import {AfterViewInit, Component, EventEmitter, Input, Output} from '@angular/core'
import {Observable} from 'rxjs';
import {debounceTime, map} from 'rxjs/operators';

@Component({
    templateUrl: './typeahead.component.html',
    selector: 'jhi-typeahead-wrapper'
})
export class CustomTypeaheadComponent implements AfterViewInit {

    @Input() options: {
        id: string,
        label: string
    }[] = []

    @Input() selectedId: string;
    @Input() selectedLabel: string;

    @Input() label: string;

    @Output() onChange: EventEmitter<any> = new EventEmitter<any>();

    constructor() { }

    search = (text$: Observable<string>) =>
        text$.pipe(
            debounceTime(200),
            map(term => term === '' ? []
                : this.options.filter(v => v.label.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10))
        )

    formatter = (x: {label: string}) => x.label;

    selectItem(event) {
        this.onChange.emit(event.item)
        this.selectedId = event.item.id;
        this.selectedLabel = event.item.label;
    }

    ngAfterViewInit(): void {
        this.selectedLabel = this.options.find(option => option.id === this.selectedId).label;
    }
}
