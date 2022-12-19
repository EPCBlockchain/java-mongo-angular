import { OptionSelection } from './option-selection.entity';

export class FieldPropertyEntity {
    public fieldType = '';
    public accountType: string;
    public required = false;
    public visible = false;
    public pattern = '';
    public dependencyId = '';
    public dependencyCondition = '';
    public dependencyValue = '';
    public mask = '';
    public minLength;
    public maxLength;
    public label = '';
    public note = '';
    public sort;
    public options: string[] = [];
    public authorities: string[];
    public industries: string[];
    public privateFor: string;
    public fileId: string;
    public fileContent; // byte[]
    public fileExtension: string;
    public optionsSelections: OptionSelection[];

    public static map(json: any): FieldPropertyEntity {
        const model = new FieldPropertyEntity();
        model.accountType = json['accountType'];
        model.fieldType = json['fieldType'] || '';
        model.required = json['required'] || false;
        model.visible = json['visible'] || '';
        model.pattern = json['pattern'] || '';
        model.mask = json['mask'] || '';
        model.note = json['note'] || '';
        model.dependencyId = json['dependencyId'] || '';
        model.dependencyCondition = json['dependencyCondition'] || '';
        model.dependencyValue = json['dependencyValue'] || '';
        model.minLength = json['minLength'] || 0;
        model.maxLength = json['maxLength'] || 0;
        model.label = json['label'] || '';
        model.sort = json['sort'] ? parseInt(json['sort'], 10) : 0;
        model.options = json['options'] || [];
        model.authorities = json['authorities'] || '';
        model.industries = json['industries'] || '';
        model.privateFor = json['privateFor'] || '';
        model.fileId = json['fileId'] || '';
        model.fileContent = json['fileContent'] || '';
        model.fileExtension = json['fileExtension'] || '';
        if (model.options) {
            model.optionsSelections = model.options.map(option => OptionSelection.map({ key: option, value: option }));
        }
        return model;
    }

    constructor() {}
}
