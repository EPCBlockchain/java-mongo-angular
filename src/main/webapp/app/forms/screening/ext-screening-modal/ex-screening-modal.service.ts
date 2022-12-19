import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ExternalScreeningModalComponent } from 'app/forms/screening/ext-screening-modal/ex-screening-modal.component';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ExternalScreeningModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(ExternalScreeningModalComponent, {
            size: 'sm',
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
