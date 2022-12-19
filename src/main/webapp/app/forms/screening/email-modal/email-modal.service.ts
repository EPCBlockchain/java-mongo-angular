import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ScreeningEmailModalComponent } from './email-modal.component';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ReturnCustomerDataModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal, private http: HttpClient) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(ScreeningEmailModalComponent, {
            size: 'lg',
            container: 'nav'
        });

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
