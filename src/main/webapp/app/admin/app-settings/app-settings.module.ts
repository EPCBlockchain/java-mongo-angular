import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Injectable, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRouteSnapshot, Resolve, RouterModule, Routes } from '@angular/router';
import { AppSettingsComponent } from './app-settings.component';
import { AppSettingsService } from './app-settings.service';
import { EmailTemplateDetailComponent } from './email-template/email-templates-detail.component';

@Injectable({ providedIn: 'root' })
export class EmailTemplateResolver implements Resolve<any> {
    constructor(private service: AppSettingsService) {}

    resolve(route: ActivatedRouteSnapshot) {
        return this.service.emailTemplate;
    }
}

const routes: Routes = [
    {
        path: '',
        component: AppSettingsComponent
    },
    {
        path: 'email',
        component: EmailTemplateDetailComponent
    }
];
@NgModule({
    imports: [FormsModule, ReactiveFormsModule, CommonModule, HttpClientModule, RouterModule.forChild(routes)],
    declarations: [AppSettingsComponent, EmailTemplateDetailComponent],
    providers: [AppSettingsService]
})
export class AppSettingsModule {}
