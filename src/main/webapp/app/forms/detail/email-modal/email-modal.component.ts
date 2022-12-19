import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';

@Component({
    selector: 'jhi-email-modal',
    templateUrl: './email-modal.component.html'
})
export class EmailModalComponent {
    public customerEmail: string;
    public formId: string;
    constructor(public activeModal: NgbActiveModal, private toastrService: ToastrService, private service: FormService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    onEnter(event) {
        console.log(event);
        if (event.keyCode === 13) {
            this.sendToCustomer();
        }
    }

    sendToCustomer() {
        this.service.sendForm(this.customerEmail, this.formId).subscribe(response => {
            console.log(response);
            this.activeModal.close();
            this.toastrService.success('Email request sent to user');
        });
    }
}
