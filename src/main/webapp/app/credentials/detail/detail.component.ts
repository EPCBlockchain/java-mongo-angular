import {Component} from '@angular/core';
import {CredentialsService} from 'app/credentials/credentials.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CredentialEntity} from 'app/shared/model/credential.entity';
import {NgxSpinnerService} from 'ngx-spinner';
import {ToastrService} from 'ngx-toastr';

@Component({
    templateUrl: 'detail.component.html'
})
export class CredentialDetailComponent {
    pageTitle: string;
    isSaving = false;
    entity: CredentialEntity;
    imageFile = '';

    constructor(private credentialsService: CredentialsService, private router: Router,
                private toastrService: ToastrService,
                private activatedRoute: ActivatedRoute, private spinnerService: NgxSpinnerService) {
        this.entity = activatedRoute.snapshot.data.details || new CredentialEntity();
        this.imageFile = this.entity.image;
        this.pageTitle = this.entity.id ? `Credential ID : ${this.entity.id}` : 'New Credential';
    }

    save() {
        this.isSaving = true;
        if (!this.entity.image && !this.entity.id) {
            this.toastrService.error('Icon File is Required');
            return;
        }

        this.spinnerService.show();
        const entity = Object.assign({}, this.entity);
        this.credentialsService.save(entity).subscribe((credential: CredentialEntity) => {
            if (!this.entity.id) {
                this.router.navigateByUrl('/credentials/' + credential.id);
            } else {
                this.entity = credential;
                this.entity.imageFile = null;
                // this.imageFile = this.entity.image;
            }
            this.toastrService.success('Credential Updated');
            this.spinnerService.hide();
            this.isSaving = false;
        }, () => {
            this.toastrService.error('Error Processing Request');
        });
    }

    validateImage() {

    }

    handleFileInput(event: any) {
        const files = event.target.files;
        const file = files[0];

        if (files && file) {
            const reader = new FileReader();

            reader.onload = (readerEvt: any) => {
                const binaryString = readerEvt.target.result;
                this.entity.imageFile =  `data:${file.type};base64,` + btoa(binaryString);
                this.imageFile = this.entity.imageFile;
            };

            reader.readAsBinaryString(file);
        }
    }
}
