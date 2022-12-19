export class IDMScreeningEntity {
    public Id = '';
    public firstName = '';
    public lastName = '';
    public middleName = '';
    public national = '';
    public dob = '';
    public passportId = '';
    public idType = '';
    public scanData = '';
    public backsideImageData = '';
    public faceImages: string[] = [];

    public static map(json: any) {
        const model = new IDMScreeningEntity();
        model.Id = json['Id'];
        model.firstName = json['firstName'];
        model.lastName = json['lastName'];
        model.middleName = json['middleName'];
        model.national = json['national'];
        model.dob = json['dob'];
        model.passportId = json['passportId'];
        model.idType = json['idType'];
        if (json['scanData'][0]) {
            model.scanData = json['scanData'][0].url.substring(5);
        }
        if (json['backsideImageData'][0]) {
            model.backsideImageData = json['backsideImageData'][0].url.substring(5);
        }
        if (json['faceImages'][0]) {
            json['faceImages'].forEach(obj => {
                model.faceImages.push(obj.url.substring(5));
            });
        }
        return model;
    }

    constructor() {}
}
