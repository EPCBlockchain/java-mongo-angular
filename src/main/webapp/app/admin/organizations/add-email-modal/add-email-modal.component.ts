import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'jhi-email-modal',
    templateUrl: './add-email-modal.component.html'
})
export class AddEmailModalComponent {
    emailToAdd = { email: '', approve: true, reject: true, submission: true };
    email = new FormControl('', [Validators.email, Validators.required]);
    constructor(public activeModal: NgbActiveModal, private toastr: ToastrService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    addEmail() {
        if (this.email.valid) {
            this.emailToAdd.email = this.email.value;
            this.activeModal.close(this.emailToAdd);
        } else {
            console.log(this.emailToAdd);
            this.toastr.error('Invalid Email');
        }
    }
}
