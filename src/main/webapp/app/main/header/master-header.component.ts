import { Component, OnInit } from '@angular/core';
import { LoginService } from 'app/account/login/login.service';
import { Router } from '@angular/router';
import { AuthServerProvider } from 'app/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { HostListener } from '@angular/core';
import { UserRoles, FormStatus } from 'app/shared/model';
import { Principal } from 'app/core';
import { UserService, User } from 'app/core';

@Component({
    templateUrl: './master-header.component.html',
    styleUrls: ['./master-header.component.scss'],
    selector: 'jhi-master-header'
})
export class MasterHeaderComponent implements OnInit {
    public FormStatus = FormStatus;
    public UserRoles = UserRoles;
    currentAccount: any;
    username: string;
    screenHeight;
    screenWidth;
    mobile: 479;
    isMobile = false;
    mobHeight: any;
    mobWidth: any;
    isSomething = false;

    constructor(
        private userService: UserService,
        private loginService: LoginService,
        private router: Router,
        private spinner: NgxSpinnerService,
        private principal: Principal
    ) {
        this.onResize();
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
            this.username = this.currentAccount['email'];
        });
    }

    @HostListener('window:resize', ['$event'])
    onResize(event?) {
        this.screenHeight = window.innerHeight;
        this.screenWidth = window.innerWidth;

        if (this.screenWidth <= 479) {
            this.isMobile = true;
        } else {
            this.isMobile = false;
        }
    }

    logout() {
        this.loginService.logout().subscribe(() => {
            this.router.navigateByUrl('/account/login');
        });
    }

    visibleIf(authorities: UserRoles[]) {
        return authorities ? this.principal.hasAnyAuthorityDirect(authorities) : true;
    }
}
