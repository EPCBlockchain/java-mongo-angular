import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';

@Component({
    selector: 'jhi-remarks-modal',
    templateUrl: './remarks-modal.component.html'
})
export class RemarksModalComponent {
    public note = '';
    constructor(public activeModal: NgbActiveModal, private toastrService: ToastrService, private service: FormService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    onKeyUp(event) {
        if (event.keyCode === 13) {
            this.addRemarks();
            return;
        }
        this.note = event.target.value;
    }

    addRemarks() {
        if (!this.note.trim()) {
            this.toastrService.error('Remarks is required');
        } else {
            this.activeModal.close(this.note);
        }
    }
}
