import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MainRoute } from 'app/main/main.route';
import { MasterLayoutComponent } from './master/master.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { NgJhipsterModule } from 'ng-jhipster';
import { FooterComponent } from 'app/main/footer/footer.component';
import { SidebarNavComponent } from 'app/main/sidebar/sidebar-nav.component';
import { MasterHeaderComponent } from 'app/main/header/master-header.component';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { ErrorComponent } from 'app/main/error/error.component';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
    declarations: [MasterLayoutComponent, ErrorComponent, FooterComponent, SidebarNavComponent, MasterHeaderComponent],
    imports: [
        RouterModule.forChild([MainRoute]),
        NgbModule,
        CommonModule,
        TranslateModule.forRoot(),
        NgJhipsterModule.forRoot({
            // set below to true to make alerts look like toast
            alertAsToast: false,
            i18nEnabled: true,
            defaultI18nLang: 'en'
        })
    ],
    exports: [RouterModule]
})
export class MainModule {}
