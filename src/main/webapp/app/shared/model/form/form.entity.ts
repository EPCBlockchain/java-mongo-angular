import {AccessSetting, ComponentOptions, FormioForm, ValidateOptions} from 'angular-formio';

export class FormEntity implements FormioForm {
    title?: string;
    display = 'form';
    name?: string;
    path?: string;
    type?: string;
    project?: string;
    template?: string;
    components?: Array<ComponentOptions<any, ValidateOptions>>;
    tags?: string[];
    access?: AccessSetting[];
    submissionAccess?: AccessSetting[];
    screeningFormId: string;
    organizationId: string;
    isScreeningForm: boolean;
    externalAccessLink: string;
    lastUpdatedBy: string;
    id: string;
    credentialId: string;
    postBackURL = '';
    uniqueIdAuthenticationURL = '';
}
