import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';

@Component({
    selector: 'jhi-email-modal',
    templateUrl: './import-export-modal.component.html'
})
export class ImportExportModalComponent {
    public jsonString: string;
    public formId: string;
    public isExport = false;
    constructor(public activeModal: NgbActiveModal, private toastrService: ToastrService, private service: FormService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    onEnter(event) {
        if (event.keyCode === 13) {
            this.import();
        }
    }

    import() {
        try {
            if (!this.isExport) {
                const jsonForm = JSON.parse(this.jsonString);
                this.activeModal.close(jsonForm);
            }
        } catch (ex) {
            this.toastrService.error('Invalid JSON Format');
        }
    }
}
