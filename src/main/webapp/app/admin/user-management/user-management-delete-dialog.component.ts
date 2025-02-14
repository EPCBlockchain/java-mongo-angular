import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { User, UserService } from 'app/core';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'jhi-user-mgmt-delete-dialog',
    templateUrl: './user-management-delete-dialog.component.html'
})
export class UserMgmtDeleteDialogComponent {
    user: User;

    constructor(
        private userService: UserService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager,
        private toastr: ToastrService
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(login) {
        this.userService.delete(login).subscribe(response => {
            console.log(response);
            this.eventManager.broadcast({
                name: 'userListModification',
                content: 'Deleted a user'
            });
            this.activeModal.dismiss(true);
            this.toastr.success('User Deleted');
        });
    }
}
