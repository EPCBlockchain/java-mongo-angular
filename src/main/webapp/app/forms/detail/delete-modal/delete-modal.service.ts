import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DeleteModalComponent } from './delete-modal.component';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class DeleteModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal, private http: HttpClient) {}

    open(formId: String): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(DeleteModalComponent, {
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
