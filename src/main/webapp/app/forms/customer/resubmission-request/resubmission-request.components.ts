import { Component } from '@angular/core';
import { CustomerFormService } from 'app/forms/customer/customer-form.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    template: ''
})
export class ResubmissionRequestComponent {
    constructor(private service: CustomerFormService, private activatedRoute: ActivatedRoute, private router: Router) {
        this.activatedRoute.params.subscribe(param => {
            this.service.resubmissionRequest(param['orgId'], param['formId']).subscribe(response => {
                console.log(response);
            });
        });
    }
}
