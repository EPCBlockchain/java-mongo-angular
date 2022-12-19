import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';
import {FormListService} from 'app/forms/list/form-list.service';

@Component({
    selector: 'jhi-delete-modal',
    templateUrl: './delete-modal.component.html'
})
export class DeleteModalComponent {
    public customerEmail: string;
    public formId: string;
    constructor(public activeModal: NgbActiveModal, private toastrService: ToastrService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    deleteForm() {
        this.activeModal.close(true);
    }
}
