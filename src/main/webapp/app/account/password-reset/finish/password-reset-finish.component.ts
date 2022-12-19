import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';

import { PasswordResetFinishService } from './password-reset-finish.service';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'jhi-password-reset-finish',
    templateUrl: './password-reset-finish.component.html',
    styleUrls: ['../password-reset.scss']
})
export class PasswordResetFinishComponent implements OnInit, AfterViewInit {
    confirmPassword: string;
    doNotMatch: string;
    error: string;
    keyMissing: boolean;
    resetAccount: any;
    success: string;
    modalRef: NgbModalRef;
    key: string;

    constructor(
        private passwordResetFinishService: PasswordResetFinishService,
        private route: ActivatedRoute,
        private toastrService: ToastrService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private toastr: ToastrService
    ) {}

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
            this.key = params['key'];
            this.resetAccount = {};
            this.keyMissing = !this.key;
            if (this.keyMissing) {
                this.toastr.error('Reset password key missing.');
            }
        });
    }

    ngAfterViewInit() {
        if (this.elementRef.nativeElement.querySelector('#password') != null) {
            this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#password'), 'focus', []);
        }
    }

    finishReset() {
        this.doNotMatch = null;
        this.error = null;
        if (this.resetAccount.password !== this.confirmPassword) {
            this.doNotMatch = 'ERROR';
            this.toastrService.error('Passwords not match');
        } else {
            this.passwordResetFinishService.save({ key: this.key, newPassword: this.resetAccount.password }).subscribe(
                () => {
                    this.success = 'OK';
                    this.toastr.success('Your password has been reset.');
                },
                () => {
                    this.success = null;
                    this.error = 'ERROR';
                    this.toastr.error('Reset password link expired. Please request new key.');
                }
            );
            console.log(this.success);
        }
    }
}
