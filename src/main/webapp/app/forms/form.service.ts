import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { FormioForm, FormioLoaderComponent } from 'angular-formio';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';
import {FormEntity} from 'app/shared/model';

@Injectable()
export class FormService {
    constructor(protected http: HttpClient) {}

    public createForm(form: FormioForm) {
        return this.http.post('/api/forms', form);
    }

    public saveForm(form: FormEntity) {
        if (form.id) {
            return this.http.post('/api/forms', form);
        }
        return this.http.put('/api/forms', form);
    }

    public getForm(formId: String): Observable<FormEntity> {
        return this.http.get<FormEntity>('/api/forms/' + formId);
    }

    public getSubmittedList(req: any): Observable<HttpResponse<FormioForm[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('/api/forms/submitted', { params: options, observe: 'response' });
    }

    public getScreeningForms2(): Observable<FormioForm[]> {
        const req = {
            filters: { tags: 'screening' }
        };
        const options = createRequestOption(req);
        return this.http.get<FormioForm[]>('/api/forms', { params: options });
    }

    public getScreeningForms(organizationName: string) {
        const params = createRequestOption({
            filters: { tags: 'screening', organizationName }
        });
        return this.http.get('/api/forms', { params });
    }

    public getApprovedForms(req: any): Observable<HttpResponse<FormioForm[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('/api/forms/approved', { params: options, observe: 'response' });
    }

    public getRejectedForms(req: any): Observable<HttpResponse<FormioForm[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('/api/forms/rejected', { params: options, observe: 'response' });
    }

    public getSubmittedForm(formId: String): Observable<FormioForm> {
        return this.http.get<FormioForm>('/api/forms/submitted/' + formId);
    }

    public getScreenedForm(formId: String): Observable<FormioForm> {
        return this.http.get<FormioForm>('/api/forms/submitted/' + formId);
    }

    public getScreeningForm(formId: String): Observable<FormioForm> {
        return this.http.get<FormioForm>('/api/forms/submitted/' + formId);
    }

    public getOBTeamForm() {
        return this.http.get('/api/forms/detail/screeningAndChecksFormId');
    }

    public saveSubmission(form: any): Observable<any> {
        form.data = JSON.stringify(form.data);
        return this.http.post('/api/forms/' + form.userId + '/' + form.formId, form);
    }

    public sendToScreening(form: any): Observable<any> {
        // form.data = JSON.stringify(form.data);
        form.apiKeyData = JSON.stringify(form.apiKeyData);
        const formId = form.formId;
        delete form.formId;

        return this.http.post('/api/forms/submitted/' + formId + '/screening', form);
    }

    public sendForm(customerEmail: string, formId: string): Observable<Object> {
        const url = `/api/submissions/request/${formId}/send`;
        return this.http.post(url, customerEmail);
    }

    public submitScreeningFormData(any: any): Observable<any> {
        return null;
    }

    public deleteForm(formId: String) {
        return this.http.delete(`/api/forms/${formId}`);
    }

    public getThomsonResultDetail(referenceId: string): Observable<Object> {
        return this.http.get('/api/forms/screening/detail/' + referenceId);
    }

    public updateSubmittedForm(submittedForm: any) {
        const url = `/api/forms/submitted`;
        return this.http.post(url, submittedForm);
    }

    getCredentials() {
        return this.http.get('/api/credentials/auto-complete');
    }
}
