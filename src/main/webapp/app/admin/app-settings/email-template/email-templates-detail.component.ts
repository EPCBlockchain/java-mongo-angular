import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EmailTemplateEntity } from '../../../shared/model/email-template.entity';
import { EmailTemplateTypeEnum } from '../../../shared/model/enums/email-template-type.enum';
import { templateTypeDefaults } from './template-types-defaults';
import * as Quill from 'quill';
import { ClipboardUtil } from '../../../shared/util/clipboard.util';
import { AppSettingsService } from '../app-settings.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    templateUrl: './email-templates-detail.component.html',
    selector: 'jhi-email-template-detail',
    providers: [],
    styleUrls: ['./email-template.scss']
})
export class EmailTemplateDetailComponent implements AfterViewInit {
    pageTitle: string;
    template = new EmailTemplateEntity();
    quill: any;
    quillContainer: any;
    quillTemplate: string;
    statusSource = Object.keys(EmailTemplateTypeEnum).filter(key => isNaN(+key));

    constructor(private router: Router, private appSettingsService: AppSettingsService, private toastr: ToastrService) {}

    back = () => history.back();

    save = () => {
        this.template.template = this.quillContainer.children[0].innerHTML;
        this.appSettingsService.updateEmailTemplate(this.template).subscribe(() => {
            this.toastr.success(this.template.type.replace(/_/gi, ' ') + ' has been updated');
            this.router.navigateByUrl('/admin/app-settings');
        });
    };

    ngAfterViewInit() {
        const template = this.appSettingsService.emailTemplate;
        if (!template) {
            this.router.navigateByUrl('/admin/app-settings');
            return;
        }
        this.quillTemplate = template.template;
        this.template = template;
        this.pageTitle = template.type.replace(/_/gi, ' ');
        console.log(this.pageTitle);
        setTimeout(() => {
            this.renderEditor();
        });
    }

    renderEditor() {
        this.quillContainer = document.getElementById('editor');
        this.quill = new Quill(this.quillContainer, {
            theme: 'snow',
            placeholder: 'Compose an email...'
        });
    }

    templateTypeDefaults() {
        const templateDefault = templateTypeDefaults.find(typeDefault => typeDefault.type === this.template.type);
        return (this.template.type && templateDefault.defaults) || [];
    }

    onClick(event) {
        ClipboardUtil.copy(event.value);
    }
}
