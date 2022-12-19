import { Component } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';

@Component({
    // templateUrl: './main.component.html
    selector: 'jhi-main',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss']
})
export class MainComponent {
    constructor(private activatedRoute: ActivatedRoute, private router: Router) {
        // console.log(activatedRoute);
        // console.log(router.routerState);
    }
}
