import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { AppSettingsService } from './app-settings.service';
import { AppSettingsEntity, SecuritySettingsEntity } from '../../shared/model/app-settings';
import { EmailTemplateTypeEnum } from '../../shared/model/enums/email-template-type.enum';
import { Router } from '@angular/router';
import { EmailTemplateEntity } from '../../shared/model/email-template.entity';
import { ToastrService } from 'ngx-toastr';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

@Component({
    templateUrl: './app-settings.component.html'
})
export class AppSettingsComponent implements OnInit, AfterViewInit {
    EmailTemplateTypeEnum = EmailTemplateTypeEnum;
    appSettings = new AppSettingsEntity();

    templates: any[] = [];
    dataApplicationGeneral = {};
    dataApplicationGeneralLoadBabd = false;
    _idGlobal_ = '';
    private rejectionModalRef: NgbModalRef;

    constructor(
        private appSettingsService: AppSettingsService,
        private router: Router,
        private toastr: ToastrService,
        protected http: HttpClient) {
        // super(http);
    }

    ngOnInit() {
        this.loadData();
    }

    ngAfterViewInit() {
        // Its works
    }

    loadData() {
        const headers = new HttpHeaders().set('Content-Type', 'application/json');
        this.http.get(`/api/AG/`,
        {headers})
        .subscribe(
            val => {
                console.log('GET call successful value returned in body');
                this.dataApplicationGeneral = val;

                // Set all data from dataBase to localStorage
                if (this.dataApplicationGeneralLoadBabd === false) {
                    let _value_ = '';
                    console.log(' ******** Fill localStorage.getItem buttonActivelocalStorage_ ******** ');
                    for (let i = 0; i < Object.keys(this.dataApplicationGeneral).length; i++ ) {
                        _value_ = '0';
                        if ( this.dataApplicationGeneral[i]['active'] === 'true' ) {
                            _value_ = '1';
                        }
                        localStorage.setItem( 'buttonActivelocalStorage_' + this.dataApplicationGeneral[i]['type'] , _value_ );
                    }
                    this.dataApplicationGeneralLoadBabd = true;
                }
            },
            response => {
                console.log('GET call in error', response);
                if (response.status === 200) {
                    // console.log('GET OK', response.status);
                } else {
                    // console.log('GET ERROR', response.status);
                }
            },
            () => {
                // console.log('The GET observable is now completed.');
            }
        );

        this.appSettingsService.get().subscribe((result: AppSettingsEntity) => {
            if (!result.securitySettings) {
                result.securitySettings = new SecuritySettingsEntity();
            }
            /*
            //for (var key in result['emailSettings']) {
            for (var key in result) {
                // check if the property/key is defined in the object itself, not in parent
                if (result.hasOwnProperty(key)) {
                    //console.log('for: ', key, result[key]);
                    console.log(`${key}: ${result[key]}`);
                }
            }
            */
            // console.log('loadData Application Settings: ' + JSON.stringify(result));

            this.loadTemplates(result);
            // this.buttonsActiveslocalLabels();
        });
    }

    buttonsActiveslocalLabels() {
        let btn;
        btn = 'buttonActivelocalStorage_ACTIVATION_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('ACTIVATION_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('ACTIVATION_EMAIL').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_ACTIVATION_EMAIL_SUCCESS';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('ACTIVATION_EMAIL_SUCCESS').innerHTML = 'Active';
        } else {
            document.getElementById('ACTIVATION_EMAIL_SUCCESS').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_APPROVAL_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('APPROVAL_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('APPROVAL_EMAIL').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_CREATION_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('CREATION_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('CREATION_EMAIL').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_FORM_REQUEST_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('FORM_REQUEST_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('FORM_REQUEST_EMAIL').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_FORM_REQUEST_EMAIL_EDIT';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('FORM_REQUEST_EMAIL_EDIT').innerHTML = 'Active';
        } else {
            document.getElementById('FORM_REQUEST_EMAIL_EDIT').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_PASSWORD_RESET_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('PASSWORD_RESET_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('PASSWORD_RESET_EMAIL').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_REJECTION_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('REJECTION_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('REJECTION_EMAIL').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_STATUS_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('STATUS_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('STATUS_EMAIL').innerHTML = 'Inactive';
        }

        btn = 'buttonActivelocalStorage_SUBMISSION_RECEIVED_EMAIL';
        if (localStorage.getItem(btn) === '1') {
            document.getElementById('SUBMISSION_RECEIVED_EMAIL').innerHTML = 'Active';
        } else {
            document.getElementById('SUBMISSION_RECEIVED_EMAIL').innerHTML = 'Inactive';
        }
    }

    loadTemplates(result: AppSettingsEntity) {
        this.templates = [
            {
                template: result.emailSettings.creationEmail || new EmailTemplateEntity(),
                label: 'User Creation',
                type: EmailTemplateTypeEnum.CREATION_EMAIL
            },
            {
                template: result.emailSettings.activationEmail || new EmailTemplateEntity(),
                label: 'Activation',
                type: EmailTemplateTypeEnum.ACTIVATION_EMAIL
            },
            {
                template: result.emailSettings.activationEmailSuccess || new EmailTemplateEntity(),
                label: 'Activation Succes',
                type: EmailTemplateTypeEnum.ACTIVATION_EMAIL_SUCCESS
            },
            {
                template: result.emailSettings.passwordResetEmail || new EmailTemplateEntity(),
                label: 'Password Reset',
                type: EmailTemplateTypeEnum.PASSWORD_RESET_EMAIL
            },
            {
                template: result.emailSettings.submissionReceivedEmail || new EmailTemplateEntity(),
                label: 'Submission Received',
                type: EmailTemplateTypeEnum.SUBMISSION_RECEIVED_EMAIL
            },
            {
                template: result.emailSettings.formRequestEmail || new EmailTemplateEntity(),
                label: 'Form Request',
                type: EmailTemplateTypeEnum.FORM_REQUEST_EMAIL
            },
            {
                template: result.emailSettings.approvalEmail || new EmailTemplateEntity(),
                label: 'Approval',
                type: EmailTemplateTypeEnum.APPROVAL_EMAIL
            },
            {
                template: result.emailSettings.formRequestEmailEdit || new EmailTemplateEntity(),
                label: 'Return to Customer',
                type: EmailTemplateTypeEnum.FORM_REQUEST_EMAIL_EDIT
            },
            {
                template: result.emailSettings.rejectionEmail || new EmailTemplateEntity(),
                label: 'Form Reject',
                type: EmailTemplateTypeEnum.REJECTION_EMAIL
            },
            {
                template: result.emailSettings.statusEmail || new EmailTemplateEntity(),
                label: 'Submission Status Changed',
                type: EmailTemplateTypeEnum.STATUS_EMAIL
            }
        ];
        this.appSettings = result;
    }

    updateTemplate(template: EmailTemplateEntity, type: EmailTemplateTypeEnum) {
        if (!template.subject) {
            template.type = type;
        }
        this.appSettingsService.emailTemplate = template;
        this.router.navigateByUrl('/admin/app-settings/email');
    }

    bdApplicationGeneral(_id_, active, type) {
        console.log( 'bdApplicationGeneral: _id_: ' + _id_ + ' - active: ' + active + ' - type: ' + type );
        const headers = new HttpHeaders().set('Content-Type', 'application/json');
        this.http.put( `/api/AG/` + _id_,
        {
            'action': '1',
            'type': type,
            'active': active
        },
        {headers})
        .subscribe(
            val => {
                console.log('PUT call successful value returned in body', val);
            },
            response => {
                console.log('PUT call in error', response);
                if (response.status === 200) {
                    console.log('PUT OK', response.status);
                } else {
                    console.log('PUT ERROR', response.status);
                }
            },
            () => {
                console.log('The PUT observable is now completed. ');
            }
        );
    }

    activeApplicationSettingsACTIVE(type) {
        for ( let i = 0; i < Object.keys(this.dataApplicationGeneral).length; i++ ) {
            document.getElementById('proceed_'+this.dataApplicationGeneral[i]['type']).style.display = 'none';
        }
        document.getElementById('proceed_'+type).style.display = 'block';

        document.getElementById('div1').style.display = 'none';
        document.getElementById('div2').style.display = 'none';
        document.getElementById('modalNewActive').style.display = 'block';
        document.getElementById('main-content').style.backgroundColor = '#a7a7a7';
    }

    activeApplicationSettingsINACTIVE(type) {
        for ( let i = 0; i < Object.keys(this.dataApplicationGeneral).length; i++ ) {
            document.getElementById('proceedI_'+this.dataApplicationGeneral[i]['type']).style.display = 'none';
        }
        document.getElementById('proceedI_'+type).style.display = 'block';

        document.getElementById('div1').style.display = 'none';
        document.getElementById('div2').style.display = 'none';
        document.getElementById('modalNewActive2').style.display = 'block';
        document.getElementById('main-content').style.backgroundColor = '#a7a7a7';
    }

    activeApplicationSettingsNew(type, acc) {

        console.log( "activeApplicationSettingsNew: " + type + " - " + acc);
        
        if ( acc === 1 ) {
            console.log( "activeApplicationSettingsNew: 1111111111111111111111");

            localStorage.setItem('buttonActivelocalStorage_'+type , '1');
            document.getElementById(type).innerHTML = 'Active';
            document.getElementById(type).style.backgroundColor = 'green';
            document.getElementById(type).style.color = '#ffffff';
            this.toastr.success(type + ' is Active');
            this.bdApplicationGeneral(this._idGlobal_, 'true', type);
        } else {
            console.log( "activeApplicationSettingsNew: 0000000000000000000000");

            localStorage.setItem('buttonActivelocalStorage_'+type , '0');
            document.getElementById(type).innerHTML = 'Inactive';
            document.getElementById(type).style.backgroundColor = '#dcdbdb';
            document.getElementById(type).style.color = '#4b4a4a';
            this.toastr.success(type + ' is Active');
            this.bdApplicationGeneral(this._idGlobal_, 'false', type);
        }
        /*
        if ( localStorage.getItem('buttonActivelocalStorage_REJECTION_EMAIL') === '1' ) {
            localStorage.setItem('buttonActivelocalStorage_R', '0');
            document.getElementById('REJECTION_EMAIL').innerHTML = 'Inactive';
            document.getElementById('REJECTION_EMAIL').style.backgroundColor = '#dcdbdb';
            document.getElementById('REJECTION_EMAIL').style.color = '#4b4a4a';
            this.toastr.success('REJECTION_EMAIL' + ' is Inactive');
            this.bdApplicationGeneral(this._idGlobal_, 'false', 'REJECTION_EMAIL');
        } else {
            localStorage.setItem('buttonActivelocalStorage_'+type , '1');
            document.getElementById(type).innerHTML = 'Active';
            document.getElementById(type).style.backgroundColor = 'green';
            document.getElementById(type).style.color = '#ffffff';
            this.toastr.success(type + ' is Active');
            this.bdApplicationGeneral(this._idGlobal_, 'true', type);
        }*/
        this.cancelModalNew();
    }

    activeApplicationSettings(template: EmailTemplateEntity, type: EmailTemplateTypeEnum) {
        let _id_ = '';
        // this.buttonsActiveslocalLabels();
        for ( let i = 0; i < Object.keys(this.dataApplicationGeneral).length; i++ ) {
            if ( this.dataApplicationGeneral[i]['type'] === type ) {
                _id_ = this.dataApplicationGeneral[i]['_id'];
                this._idGlobal_ = this.dataApplicationGeneral[i]['_id'];
                break;
            }
        }

        if ( localStorage.getItem('buttonActivelocalStorage_' + type) === '1' ) {      
            this.activeApplicationSettingsACTIVE(type);            
            // localStorage.setItem('buttonActivelocalStorage_' + type, '0');
            // document.getElementById(type).innerHTML = 'Inactive';
            // document.getElementById(type).style.backgroundColor = '#dcdbdb';
            // document.getElementById(type).style.color = '#4b4a4a';
            // this.toastr.success(type + ' is Inactive');
            // this.bdApplicationGeneral(_id_, 'false', type);
        } else {
            this.activeApplicationSettingsINACTIVE(type);
        }
        /*
        if (type !== 'REJECTION_EMAIL') {
            if ( localStorage.getItem('buttonActivelocalStorage_' + type) === '1' ) {      
                localStorage.setItem('buttonActivelocalStorage_' + type, '0');
                document.getElementById(type).innerHTML = 'Inactive';
                document.getElementById(type).style.backgroundColor = '#dcdbdb';
                document.getElementById(type).style.color = '#4b4a4a';
                this.toastr.success(type + ' is Inactive');
                this.bdApplicationGeneral(_id_, 'false', type);

            } else {
                localStorage.setItem('buttonActivelocalStorage_' + type, '1');
                document.getElementById(type).innerHTML = 'Active';
                document.getElementById(type).style.backgroundColor = 'green';
                document.getElementById(type).style.color = '#ffffff';
                this.toastr.success(type + ' is Active');
                this.bdApplicationGeneral(_id_, 'true', type);
            }
        } else if (type === 'REJECTION_EMAIL') {
            if ( localStorage.getItem('buttonActivelocalStorage_' + type) === '1' ) {
                document.getElementById('div1').style.display = 'none';
                document.getElementById('div2').style.display = 'none';
                document.getElementById('modal').style.display = 'block';
                document.getElementById('main-content').style.backgroundColor = '#a7a7a7';
            } else {
                this.activeApplicationSettingsREJECTION_EMAIL();
            }
        }*/
    }

    activeApplicationSettingsORI(template: EmailTemplateEntity, type: EmailTemplateTypeEnum) {
        let _id_ = '';
        // this.buttonsActiveslocalLabels();
        for ( let i = 0; i < Object.keys(this.dataApplicationGeneral).length; i++ ) {
            if ( this.dataApplicationGeneral[i]['type'] === type ) {
                _id_ = this.dataApplicationGeneral[i]['_id'];
                this._idGlobal_ = this.dataApplicationGeneral[i]['_id'];
                break;
            }
        }

        if (type !== 'REJECTION_EMAIL') {
                if ( localStorage.getItem('buttonActivelocalStorage_' + type) === '1' ) {
                    localStorage.setItem('buttonActivelocalStorage_' + type, '0');
                    document.getElementById(type).innerHTML = 'Inactive';
                    document.getElementById(type).style.backgroundColor = '#dcdbdb';
                    document.getElementById(type).style.color = '#4b4a4a';
                    this.toastr.success(type + ' is Inactive');
                    this.bdApplicationGeneral(_id_, 'false', type);
                } else {
                    localStorage.setItem('buttonActivelocalStorage_' + type, '1');
                    document.getElementById(type).innerHTML = 'Active';
                    document.getElementById(type).style.backgroundColor = 'green';
                    document.getElementById(type).style.color = '#ffffff';
                    this.toastr.success(type + ' is Active');
                    this.bdApplicationGeneral(_id_, 'true', type);
                }
        } else if (type === 'REJECTION_EMAIL') {
            if ( localStorage.getItem('buttonActivelocalStorage_' + type) === '1' ) {
                document.getElementById('div1').style.display = 'none';
                document.getElementById('div2').style.display = 'none';
                document.getElementById('modal').style.display = 'block';
                document.getElementById('main-content').style.backgroundColor = '#a7a7a7';
            } else {
                this.activeApplicationSettingsREJECTION_EMAIL();
            }
        }
    }

    activeApplicationSettingsREJECTION_EMAIL() {
        if ( localStorage.getItem('buttonActivelocalStorage_REJECTION_EMAIL') === '1' ) {
            localStorage.setItem('buttonActivelocalStorage_REJECTION_EMAIL', '0');
            document.getElementById('REJECTION_EMAIL').innerHTML = 'Inactive';
            document.getElementById('REJECTION_EMAIL').style.backgroundColor = '#dcdbdb';
            document.getElementById('REJECTION_EMAIL').style.color = '#4b4a4a';
            this.toastr.success('REJECTION_EMAIL' + ' is Inactive');
            this.bdApplicationGeneral(this._idGlobal_, 'false', 'REJECTION_EMAIL');
        } else {
            localStorage.setItem('buttonActivelocalStorage_REJECTION_EMAIL' , '1');
            document.getElementById('REJECTION_EMAIL').innerHTML = 'Active';
            document.getElementById('REJECTION_EMAIL').style.backgroundColor = 'green';
            document.getElementById('REJECTION_EMAIL').style.color = '#ffffff';
            this.toastr.success('REJECTION_EMAIL' + ' is Active');
            this.bdApplicationGeneral(this._idGlobal_, 'true', 'REJECTION_EMAIL');
        }
        this.cancelModal();
    }

    cancelModal() {
        document.getElementById('main-content').style.backgroundColor = '#ffffff';
        document.getElementById('div1').style.display = 'block';
        document.getElementById('div2').style.display = 'block';
        document.getElementById('modal').style.display = 'none';
    }

    cancelModalNew() {
        document.getElementById('main-content').style.backgroundColor = '#ffffff';
        document.getElementById('div1').style.display = 'block';
        document.getElementById('div2').style.display = 'block';
        document.getElementById('modalNewActive').style.display = 'none';
        document.getElementById('modalNewActive2').style.display = 'none';
    }

    updateSecuritySettings() {
        this.appSettingsService.updateSecuritSettings(this.appSettings.securitySettings).subscribe(result => {
            this.toastr.success('Form Settings has been updated');
        });
    }
}
