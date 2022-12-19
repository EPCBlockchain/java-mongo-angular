import { FieldPropertyEntity } from 'app/shared/model/form/field-property.entity';
import ObjectID from 'bson-objectid';

export class FieldEntity {
    public id = '';
    public name = '';
    public value = '';
    public optionValues: string[] = [];
    public questionGroupValues: FieldEntity[]; // TODO: make model in Java
    public properties: FieldPropertyEntity = new FieldPropertyEntity();

    // UI Only
    public delete = false;

    public static map(json: any): FieldEntity {
        const model = new FieldEntity();

        model.id = json['id'];
        model.name = json['name'];
        model.value = json['value'];
        model.optionValues = json['optionValues'];

        if (json['properties']) {
            model.properties = FieldPropertyEntity.map(json['properties']);
        }

        return model;
    }

    constructor() {
        this.id = new ObjectID().str;
    }
}
