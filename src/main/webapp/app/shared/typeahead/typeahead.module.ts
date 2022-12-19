import {NgModule} from "@angular/core"
import {CustomTypeaheadComponent} from "app/shared/typeahead/typeahead.component"
import {NgbTypeaheadModule} from "@ng-bootstrap/ng-bootstrap"
import {FormsModule} from "@angular/forms"

@NgModule({
    declarations: [
        CustomTypeaheadComponent
    ],
    imports: [
        NgbTypeaheadModule,
        FormsModule
    ],
    exports: [
        CustomTypeaheadComponent
    ]
})
export class CustomTypeAheadModule {}
