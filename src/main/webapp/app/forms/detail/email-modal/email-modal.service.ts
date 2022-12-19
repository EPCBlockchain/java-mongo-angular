import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EmailModalComponent } from './email-modal.component';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class EmailModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal, private http: HttpClient) {}

    open(formId: String): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(EmailModalComponent, {
            size: 'sm',
            container: 'nav'
        });

        modalRef.componentInstance.formId = formId;

        modalRef.result.then(
            result => {
                this.isOpen = false;
            },
            reason => {
                this.isOpen = false;
            }
        );
        return modalRef;
    }
}
