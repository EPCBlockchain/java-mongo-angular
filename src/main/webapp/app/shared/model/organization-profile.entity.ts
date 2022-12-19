export class OrganizationProfileEntity {
    public description = '';
    public street = '';
    public state = '';
    public country = '';
    public zipCode = '';
    public phone = '';
    public email = '';
    public website = '';

    public static map(json: any): OrganizationProfileEntity {
        const model = new OrganizationProfileEntity();
        model.description = json['description'] || '';
        model.street = json['street'] || '';
        model.state = json['state'] || '';
        model.country = json['country'] || '';
        model.phone = json['phone'] || '';
        model.zipCode = json['zipCode'] || '';
        model.email = json['email'] || '';
        model.website = json['website'] || '';
        return model;
    }

    constructor() {}
}
