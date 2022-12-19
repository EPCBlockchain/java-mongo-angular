import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';

@Component({
    selector: 'jhi-submission-modal',
    templateUrl: './submission-modal.component.html'
})
export class SubmissionModalComponent {
    public note: string;
    constructor(public activeModal: NgbActiveModal, private toastrService: ToastrService, private service: FormService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    onEnter(event) {
        console.log(event);
        if (event.keyCode === 13) {
            this.addRemarks();
        }
    }

    addRemarks() {
        this.activeModal.close(this.note);
    }
}
