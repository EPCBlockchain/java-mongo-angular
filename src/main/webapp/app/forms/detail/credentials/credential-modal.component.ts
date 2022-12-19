import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';

@Component({
    selector: 'jhi-email-modal',
    templateUrl: './credential-modal.component.html'
})
export class CredentialModalComponent {
    public credentials: any[];
    constructor(public activeModal: NgbActiveModal) {

    }
}
