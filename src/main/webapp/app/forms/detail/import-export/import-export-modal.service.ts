import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ImportExportModalComponent } from './import-export-modal.component';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ImportExportModalService {
    private isOpen = false;
    constructor(private modalService: NgbModal, private http: HttpClient) {}

    open(json: any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        const modalRef = this.modalService.open(ImportExportModalComponent, {
            size: 'lg',
            container: 'nav'
        });

        modalRef.componentInstance.jsonString = json;
        modalRef.componentInstance.isExport = !!json;

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
