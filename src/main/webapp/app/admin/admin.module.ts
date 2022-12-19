import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageHelper } from 'app/core';
import { KycSharedModule } from 'app/shared';
import { JhiLanguageService } from 'ng-jhipster';
import { QuillModule } from 'ngx-quill';
import { CustomGridModule } from '../shared/grid/custom-grid.module';
import { adminState, UserMgmtComponent, UserMgmtDeleteDialogComponent, UserMgmtDetailComponent, UserMgmtUpdateComponent } from './';
import { AppSettingsModule } from './app-settings/app-settings.module';
import { InterceptorModule } from '../blocks/interceptor/interceptor.module';
import { UserManagementDetailComponent } from './user-management/detail.component';

@NgModule({
    imports: [
        InterceptorModule,
        QuillModule.forRoot(),
        KycSharedModule,
        CustomGridModule,
        AppSettingsModule,
        RouterModule.forChild(adminState)
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KycAdminModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
