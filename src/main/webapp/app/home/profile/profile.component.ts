import { Component, ViewChild, ElementRef } from '@angular/core';
import { ProfileService } from 'app/home/profile/profile.service';

@Component({
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
    @ViewChild('uploadInput', { static: true }) uploadInput: ElementRef;

    public firstName: String;
    public lastName: String;
    public address: String;
    public file: any;

    constructor(private profileService: ProfileService) {}

    triggerInputClick() {
        const el: HTMLElement = this.uploadInput.nativeElement as HTMLElement;
        el.click();
    }

    onUploadChange(event) {
        this.file = event.target.files;
    }

    save() {
        const data = new FormData();
        data.append('file', this.file, this.file.name);
        data.append('model', JSON.stringify({ firstName: this.firstName, lastName: this.lastName, address: this.address }));
        this.profileService.uploadProfileImage(data).subscribe(test => {
            console.log(test);
        });
    }
}
