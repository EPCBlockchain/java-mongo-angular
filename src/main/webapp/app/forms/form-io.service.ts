import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormioService } from 'angular-formio';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';

@Injectable()
export class FormIOService extends FormioService {
    private uri = 'http://159.69.46.152:4001/#/';
    constructor(private http: HttpClient) {
        super('');
    }

    loadForm(formId = '5bd3053bcabb07539939ff3f'): Observable<any> {
        this.uri = 'http://159.69.46.152:4001/#/';
        return this.http.get(this.uri + 'form/' + formId);
    }

    saveSubmission(form: any): Observable<any> {
        form.data = JSON.stringify(form.data);
        return this.http.post('/api/forms/' + form.userId + '/' + form.formId, form);
    }

    getForms(): Observable<any> {

        return this.http.get(this.uri + 'form');
    }

    getForm(formId): Observable<any> {
        return this.http.get(this.uri + 'form/' + formId);
    }

    getDataById(submittedDataId): Observable<any> {
        return this.http.get('/api/forms/' + submittedDataId);
    }

    sendForm(customerEmail: string, formId: string): Observable<Object> {
        const url = `/api/forms/${formId}/send`;
        return this.http.post(url, customerEmail);
    }

    getSubmittedList(query: any) {
        const options = createRequestOption(query);
        return this.http.get<any[]>('/api/forms/submitted', { params: options, observe: 'response' });
    }

    getSubmittedForm(id: String) {
        return this.http.get('/api/forms/' + id);
    }

    getProcessedForms(query: any) {
        const options = createRequestOption(query);
        return this.http.get<any[]>('/api/forms/screened', { params: options, observe: 'response' });
    }

    getProcessedForm(id: String) {
        return this.http.get('/api/forms/screened/' + id);
    }

    getOBTeamForm() {
        return this.http.get(this.uri + 'form/5b8371684d0a97003ce08ca6');
    }

    sendCheckForm(formData: any) {
        console.log('hereee is obform', formData);

        return this.http.post('/api/forms/obteam/5b8371684d0a97003ce08ca6/screening', formData);
    }
}
