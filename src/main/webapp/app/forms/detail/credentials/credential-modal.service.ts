import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CredentialModalComponent } from './credential-modal.component';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class CredentialModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal, private http: HttpClient) {}

    open(credentials: any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(CredentialModalComponent, {
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

        modalRef.componentInstance.credentials = credentials;
        return modalRef;
    }
}
