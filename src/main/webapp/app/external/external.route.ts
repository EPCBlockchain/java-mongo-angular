import {Route, Routes} from '@angular/router'
import {ExternalFormComponent} from 'app/external/external.component';

export const externalFormRoute: Routes = [{
    path: 'form-view/:formId',
    component: ExternalFormComponent,
    data: {
        external: true,
        viewOnly: true
    }
}, {
    path: ':formId',
    component: ExternalFormComponent,
    data: {
        external: true
    }
}
];
