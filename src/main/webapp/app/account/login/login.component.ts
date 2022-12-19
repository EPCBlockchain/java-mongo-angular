import { Component, ElementRef, Renderer, AfterViewInit } from '@angular/core';
import { LoginService } from './login.service';
import { JhiEventManager } from 'ng-jhipster';
import { StateStorageService, Principal } from 'app/core';
import {ActivatedRoute, Router} from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {activateRoute} from 'app/account';
import { GlobalConfig } from '../../core/config';

@Component({
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements AfterViewInit {
    authenticationError: boolean;
    password: string;
    rememberMe: boolean;
    username: string;
    credentials: any;
    redirectUrl: string;
    auth: string;
    canRegister = GlobalConfig.CAN_REGISTER;

    constructor(
        private eventManager: JhiEventManager,
        private loginService: LoginService,
        private stateStorageService: StateStorageService,
        private elementRef: ElementRef,
        private renderer: Renderer,
        private router: Router,
        private toastr: ToastrService,
        private activatedRoute: ActivatedRoute
    ) {
        this.credentials = {};
        activatedRoute.queryParams.subscribe(queryParams => {
            console.log(queryParams);
           if (queryParams['redirect']) {
               this.redirectUrl = Buffer.from(queryParams['redirect'], 'hex').toString('utf-8');
               this.auth = queryParams['auth'];
           }
        });
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []);
    }

    login() {
        var error = 0;

        document.getElementById("valUserInput").style.display = "none";
        document.getElementById("valPassInput").style.display = "none";

        console.log ("this.username: ("+this.username+")");
        console.log ("this.password: ("+this.password+")");

        if ( this.username === "" || this.username === undefined ) {
            document.getElementById("valUserInput").style.display = "block";
            error++;
        }
        if ( this.password === "" || this.password === undefined ) {
            document.getElementById("valPassInput").style.display = "block";
            error++;
        }

        if ( error === 0 ) {
            this.loginService
            .login({
                username: this.username,
                password: this.password,
                rememberMe: true
            })
            .then(() => {
                this.authenticationError = false;

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });

                // // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // // since login is succesful, go to stored previousState and clear previousState

                if (this.auth && this.redirectUrl) {
                    const authToken = localStorage.getItem('authenticationToken');
                    const nQueryParams = 'token=' + encodeURI(JSON.parse(authToken)['_value']);
                    this.redirectUrl = this.redirectUrl + ((this.redirectUrl.indexOf('?') > 0) ? '&' : '?') + nQueryParams;
                }
                if (this.redirectUrl) {
                    this.router.navigate([
                        this.redirectUrl
                    ]);
                    return;
                }
                const redirect =  this.stateStorageService.getUrl();
                localStorage.setItem('username', this.username);
                if (redirect) {
                    this.stateStorageService.storeUrl(null);
                    this.router.navigate([redirect]);
                } else {
                    this.router.navigate(['']);
                }
            })
            .catch(err => {
                console.log(err);
                this.authenticationError = true;
                this.toastr.error('Username and password not match. Please try again');
            });
        }
    }

    requestResetPassword() {
        this.router.navigate(['/account', 'reset', 'request']);
    }

    valUser(value) {
        if (value.length==0) {
            console.log("Empty");
        } else {
            console.log( value );    
        }
        // console.log(  document.getElementById("username").value );
    }

    valPass(value) {
        document.getElementById("valUserInput").style.display = "none";
        document.getElementById("valPassInput").style.display = "none";

        if ( this.username==="" ) {
            document.getElementById("valUserInput").style.display = "block";
        }
    }


}
