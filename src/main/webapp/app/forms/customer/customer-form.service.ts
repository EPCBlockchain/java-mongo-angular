import { Injectable } from '@angular/core';
import { FormioService } from 'angular-formio';
import { HttpClient } from '@angular/common/http';
import { Principal } from 'app/core';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';

@Injectable({ providedIn: 'root' })
export class CustomerFormService extends FormioService {
    public data = {};
    constructor(private http: HttpClient, private principal: Principal) {
        super('');
    }

    loadForm(submissionId: String): Observable<any> {
        return this.http.get('/api/submissions/user/' + submissionId);
    }

    saveSubmission(form: any): Observable<any> {
        console.log(form);
        return this.http.post('/api/submissions/request/' + form.submissionId, form);
    }

    getSubmittedList(query: any): Observable<any> {
        const options = createRequestOption(query);
        return this.http.get('/api/submissions/user', { params: options, observe: 'response' });
    }

    loadRequestForm(orgId: String, formId: String): Observable<any> {
        return this.http.get('/api/submissions/request/' + orgId + '/' + formId);
    }

    resubmissionRequest(orgId: String, formId: String) {
        return this.http.get(`/api/forms/resubmission-request/${orgId}/${formId}`);
    }
}
