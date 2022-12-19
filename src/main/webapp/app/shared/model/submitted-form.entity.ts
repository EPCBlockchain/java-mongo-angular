export class SubmittedFormEntity {
    public id: String;
    public formId: String;
    public userFormId: String;
    public status: String;
    public data: any;
    public screeningData: any;
    public submissionReferenceId: String;
    public isScreeningForm: Boolean;
    public form: any;
    public submissionReference: SubmittedFormEntity;
    public submissionData: any;
    public screeningType: String;
    public remarks: string;
    public lastUpdatedBy: string;
    public dataFormio: any;
    public dataApi: any;
}
