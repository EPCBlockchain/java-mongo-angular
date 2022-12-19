import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormioService } from 'angular-formio';
import { Observable } from 'rxjs';
import { Principal } from 'app/core';

import { createRequestOption } from 'app/shared';

@Injectable()
export class FormIOUserService extends FormioService {
    // implements OnInit {
    private uri = 'http://159.69.46.152:4001/#/';
    private currentAccount;

    constructor(private http: HttpClient, private principal: Principal) {
        super('');

        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    loadForm(formId: String): Observable<any> {
        return this.http.get(this.uri + 'form/' + formId);
    }

    saveSubmission(form: any): Observable<any> {
        form.data = JSON.stringify(form.data);
        console.log('saveSubmission FormIOUserService', form);

        return this.http.post('/api/forms/a/' + form.userId + '/' + form.formId + '/', form);
    }

    getSubmittedList(query: any): Observable<any> {
        const userId = query.userId;
        delete query.userId;
        const options = createRequestOption(query);

        return this.http.get('/api/forms/' + userId + '/userforms', { params: options, observe: 'response' });
    }

    private createOptions(options: any) {
        return '';
    }
}
