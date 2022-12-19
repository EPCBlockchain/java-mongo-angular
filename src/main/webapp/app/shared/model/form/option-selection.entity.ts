export class OptionSelection {
    public checked = false;
    public text: string;
    public value: string;

    public static map(json: any): OptionSelection {
        const model = new OptionSelection();

        model.checked = json['checked'] || false;
        model.text = json['key'] || json['name'] || json['text'];
        model.value = json['value'];

        return model;
    }

    constructor() {}
}
