import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { FormService } from 'app/forms/form.service';
import { ScreeningType } from 'app/shared/model/enums/screening-type.enum';

@Component({
    selector: 'jhi-ext-screening-modal',
    templateUrl: './ex-screening-modal.component.html'
})
export class ExternalScreeningModalComponent {
    public screeningType = ScreeningType.SHUFTI_PRO;
    public ScreeningType = ScreeningType;
    constructor(public activeModal: NgbActiveModal, private toastrService: ToastrService, private service: FormService) {}

    cancel() {
        this.activeModal.dismiss('cancel');
    }

    selectScreeningType() {
        this.activeModal.close(this.screeningType);
    }
}
