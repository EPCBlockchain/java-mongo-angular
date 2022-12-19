import { FormioForm } from 'angular-formio';

export interface FormInterface extends FormioForm {
    screeningFormId;
    organizationId;
    isScreeningForm;
    externalAccessLink;
    id;
    postBackURL;
    uniqueIdAuthenticationURL;
}
