import { Component, OnInit, AfterViewInit, Renderer, ElementRef, ComponentFactoryResolver, ViewChildren, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared';
import { Register } from './register.service';
import { ToastrService } from 'ngx-toastr';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'jhi-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, AfterViewInit {
    confirmPassword: string;
    doNotMatch: string;
    error: string;
    errorEmailExists: string;
    errorUserExists: string;
    registerAccount: any;
    success: boolean;
    modalRef: NgbModalRef;

    @ViewChild('registerForm', { static: false }) registerForm: NgForm;

    constructor(
        private languageService: JhiLanguageService,
        private registerService: Register,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private toastr: ToastrService
    ) {}

    ngOnInit() {
        this.success = false;
        this.registerAccount = {};
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
    }

    register() {
        if (this.registerAccount.password !== this.confirmPassword) {
            this.doNotMatch = 'ERROR';
            this.toastr.warning("Passwords don't match");
        } else {
            this.doNotMatch = null;
            this.error = null;
            this.errorUserExists = null;
            this.errorEmailExists = null;
            this.languageService.getCurrent().then(key => {
                this.registerAccount.langKey = key;
                this.registerService.save(this.registerAccount).subscribe(
                    () => {
                        this.toastr.success('Registration successful.');
                        this.success = true;
                    },
                    response => this.processError(response)
                );
            });
        }
    }

    private processError(response: HttpErrorResponse) {
        this.success = null;
        if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
            this.error = 'Login already used';
        } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
            this.error = 'Email already used';
        } else {
            this.error = 'Error processing request';
        }
        this.toastr.error(this.error);
    }
}
