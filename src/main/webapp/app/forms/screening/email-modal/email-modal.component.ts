import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';

@Component({
    selector: 'jhi-email-modal',
    templateUrl: './email-modal.component.html'
})
export class ScreeningEmailModalComponent {
    public note = '';
    constructor(public activeModal: NgbActiveModal, private toastrService: ToastrService, private service: FormService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    onEnter(event) {
        if (event.keyCode === 13) {
            this.sendToCustomer();
        }
    }

    sendToCustomer() {
        console.log(this.note.trim());
        if (!this.note.trim()) {
            this.toastrService.error('Remarks is required');
        } else {
            this.activeModal.close(this.note);
        }
        // console.log(this.note);
    }
}
