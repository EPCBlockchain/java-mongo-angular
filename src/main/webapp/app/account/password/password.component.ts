import { Component, OnInit } from '@angular/core';

import { Principal } from 'app/core';
import { PasswordService } from './password.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'jhi-password',
    templateUrl: './password.component.html'
})
export class PasswordComponent implements OnInit {
    doNotMatch: string;
    error: string;
    success: string;
    account: any;
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;

    constructor(private passwordService: PasswordService, private principal: Principal, private toastrService: ToastrService) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
    }

    changePassword() {
        if (this.newPassword !== this.confirmPassword) {
            this.error = null;
            this.success = null;
            this.doNotMatch = 'ERROR';
            this.toastrService.error('Passwords not match');
        } else {
            this.doNotMatch = null;
            this.passwordService.save(this.newPassword, this.currentPassword).subscribe(
                () => {
                    this.error = null;
                    this.success = 'OK';
                },
                () => {
                    this.success = null;
                    this.error = 'ERROR';
                    if (this.doNotMatch) {
                        this.toastrService.error('Passwords not match');
                    }
                }
            );
        }
    }
}
