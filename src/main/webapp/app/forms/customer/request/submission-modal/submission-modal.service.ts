import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';
import {SubmissionModalComponent} from 'app/forms/customer/request/submission-modal/submission-modal.component';

@Injectable()
export class SubmissionModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal, private http: HttpClient) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(SubmissionModalComponent, {
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
