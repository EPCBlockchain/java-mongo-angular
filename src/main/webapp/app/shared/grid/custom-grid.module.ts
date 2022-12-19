import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CustomGridComponent } from './custom-grid.component';
import { FilterOptionsComponent } from './filter-options.component';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BootstrapIconsModule } from 'ng-bootstrap-icons';
import { allIcons } from 'ng-bootstrap-icons/icons';

@NgModule({
    imports: [CommonModule, NgbModule,BootstrapIconsModule.pick(allIcons)],
    declarations: [CustomGridComponent, FilterOptionsComponent],
    exports: [CustomGridComponent, FilterOptionsComponent,BootstrapIconsModule],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CustomGridModule {}
