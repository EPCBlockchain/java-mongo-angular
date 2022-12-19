import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';
import {RemarksModalComponent} from 'app/forms/screening/remarks-modal/remarks-modal.component';

@Injectable()
export class RemarksModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal, private http: HttpClient) {}

    open(): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(RemarksModalComponent, {
            size: 'lg',
            container: 'nav'
        });

        modalRef.result.then(
            result => {
                this.isOpen = false;
                return result;
            },
            reason => {
                this.isOpen = false;
                return reason;
            }
        );
        return modalRef;
    }
}
