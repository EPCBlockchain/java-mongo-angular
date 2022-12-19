import { Component, AfterViewInit } from '@angular/core';
import { UserRoles, FormStatus } from 'app/shared/model';
import { Principal } from 'app/core';
import { HostListener } from '@angular/core';

@Component({
    templateUrl: './sidebar-nav.component.html',
    styleUrls: ['./sidebar-nav.component.scss'],
    selector: 'jhi-sidebar-nav'
})
export class SidebarNavComponent implements AfterViewInit {
    public FormStatus = FormStatus;
    public UserRoles = UserRoles;
    screenHeight;
    screenWidth;
    mobile: 479;
    mtablet: 767;
    ltablet: 1024;
    desktop: 1920;
    close: Boolean;
    organizationId: string;

    constructor(private principal: Principal) {
        this.organizationId = this.principal.getIdentity().organizationId;
    }

    @HostListener('window:resize', ['$event'])
    onResize(event?) {
      this.screenHeight = window.innerHeight;
      this.screenWidth = window.innerWidth;

      // console.log(this.screenHeight + ' ' + this.screenWidth);
      if (this.screenWidth <= this.mobile) {
        this.toggle(true);
      }
    }

    toggle(close = false) {
        const classList = document.getElementById('sidebar-wrapper').classList;
        if (close) {
            classList.remove('active');
        } else if (classList.contains('active')) {
            classList.remove('active');
        } else {
            classList.add('active');
        }
    }

    ngAfterViewInit() {
        this.toggle(false);
    }

    visibleIf(authorities: UserRoles[]) {
        return authorities ? this.principal.hasAnyAuthorityDirect(authorities) : true;
    }
}
