import { EmailTemplateTypeEnum, EmailTemplateDefaultsEnum } from '../../../shared/model/enums/email-template-type.enum';

const submissionLink = {
    label: 'Submission Link',
    value: `${location.origin}/#/forms/submissions/[{${EmailTemplateDefaultsEnum.SUBMISSION_ID}}]`
};

const activationLink = {
    label: 'Activation Link',
    value: `${location.origin}/#/account/activate?key=[{${EmailTemplateDefaultsEnum.ACTIVATION_KEY}}]`
};

const accountLogin = {
    label: 'Account Login',
    value: `[{${EmailTemplateDefaultsEnum.ACCOUNT_LOGIN}}]`
};

const passwordReset = {
    label: 'Password Reset Link',
    value: `${location.origin}/#/account/reset/finish?key=[{${EmailTemplateDefaultsEnum.PASSWORD_RESET_KEY}}]`
}

export const templateTypeDefaults = [
    {
        type: EmailTemplateTypeEnum.ACTIVATION_EMAIL,
        defaults: [accountLogin, activationLink]
    },
    {
        type: EmailTemplateTypeEnum.ACTIVATION_EMAIL_SUCCESS,
        defaults: [accountLogin]
    },
    {
        type: EmailTemplateTypeEnum.APPROVAL_EMAIL,
        defaults: [
            accountLogin,
            {
                label: 'Submission Link',
                value: `${location.origin}/#/forms/detail/[{${EmailTemplateDefaultsEnum.SUBMISSION_ID}}]`
            }
        ]
    },
    {
        type: EmailTemplateTypeEnum.CREATION_EMAIL,
        defaults: [accountLogin, passwordReset]
    },
    {
        type: EmailTemplateTypeEnum.FORM_REQUEST_EMAIL_EDIT,
        defaults: [
            {
                label: 'Note',
                value: `[{${EmailTemplateDefaultsEnum.NOTE}}]`
            },
            {
                label: 'Form Request Link',
                value: `${location.origin}/#/forms/detail/[{${EmailTemplateDefaultsEnum.SUBMISSION_ID}}]`
            }
        ]
    },
    {
        type: EmailTemplateTypeEnum.FORM_REQUEST_EMAIL,
        defaults: [
            {
                label: 'Form Request Link',
                value: `${location.origin}/#/forms/request/[{${EmailTemplateDefaultsEnum.ORGANIZATION_ID}}]/[{${EmailTemplateDefaultsEnum.FORM_ID}}]`
            }
        ]
    },
    {
        type: EmailTemplateTypeEnum.PASSWORD_RESET_EMAIL,
        defaults: [
            accountLogin,
            passwordReset
        ]
    },
    {
        type: EmailTemplateTypeEnum.REJECTION_EMAIL,
        defaults: [
            accountLogin,
            {
                label: 'Note',
                value: `[{${EmailTemplateDefaultsEnum.NOTE}}]`
            },
            {
                label: 'Submission Link',
                value: `${location.origin}/#/forms/detail/[{${EmailTemplateDefaultsEnum.SUBMISSION_ID}}]`
            }
        ]
    },
    {
        type: EmailTemplateTypeEnum.SUBMISSION_RECEIVED_EMAIL,
        defaults: [submissionLink]
    },
    {
        type: EmailTemplateTypeEnum.STATUS_EMAIL,
        defaults: [
            {
                label: 'Status Email',
                value: `${location.origin}/#/forms/screening/[{${EmailTemplateDefaultsEnum.SUBMISSION_ID}}]`
            }
        ]
    }
];
