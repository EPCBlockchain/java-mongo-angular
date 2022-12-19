import { FieldEntity } from './field.entity';
import ObjectID from 'bson-objectid';

export class TabEntity {
    public id = '';
    public name = '';
    public index;
    public fields: FieldEntity[] = [];

    // UI Only
    public delete = false;

    public static map(json: any): TabEntity {
        const model = new TabEntity();
        model.id = json['id'];
        model.name = json['name'];
        model.index = json['index'];
        if (json['fields']) {
            model.fields = (json['fields'] as any[]).map(field => FieldEntity.map(field)).sort((a, b) => {
                if (a.properties.sort < b.properties.sort) {
                    return -1;
                }
                if (a.properties.sort > b.properties.sort) {
                    return 1;
                }
                return 0;
            });
        }
        return model;
    }

    constructor() {
        this.id = new ObjectID().str;
    }
}
