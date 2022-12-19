import { Component, AfterViewInit } from '@angular/core';
import { Router, NavigationStart, NavigationEnd, NavigationCancel } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { HostListener } from '@angular/core';

@Component({
    templateUrl: './master.component.html',
    styleUrls: ['./master.component.scss']
})
export class MasterLayoutComponent implements AfterViewInit {
    screenHeight;
    screenWidth;
    mobile: 479;
    isMobile: Boolean;

    constructor(private router: Router, private spinner: NgxSpinnerService) {
        this.router.events.subscribe(event => {
            // if (event instanceof NavigationStart) {
            //     this.spinner.show();
            // } else if (event instanceof NavigationEnd || event instanceof NavigationCancel) {
            //     this.spinner.hide();
            // }
        });
        this.onResize();
    }

   @HostListener('window:resize', ['$event'])
    onResize(event?) {
      this.screenHeight = window.innerHeight;
      this.screenWidth = window.innerWidth;

      // console.log(this.screenHeight + ' ' + this.screenWidth);
      if (this.screenWidth <= 479) {
          // console.log(this.screenWidth);
          // console.log( 'mobile' );
          this.isMobile = true;
      } else {
          this.isMobile = false;
      }
    }

    ngAfterViewInit() {
    }
}
