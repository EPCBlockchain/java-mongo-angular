import { NgModule } from '@angular/core';

import { KycSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent } from './';
import { FilterOptionsComponent } from './grid/filter-options.component';
import { CustomGridComponent } from './grid/custom-grid.component';

@NgModule({
    imports: [KycSharedLibsModule],
    declarations: [FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent],
    exports: [KycSharedLibsModule, FindLanguageFromKeyPipe, JhiAlertComponent, JhiAlertErrorComponent]
})
export class KycSharedCommonModule {}
