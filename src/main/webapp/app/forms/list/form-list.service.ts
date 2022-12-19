import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { FormioForm } from 'angular-formio';
import { Observable } from 'rxjs';
import { FormService } from 'app/forms/form.service';
import { createRequestOption } from 'app/shared';

@Injectable()
export class FormListService extends FormService {
    constructor(protected http: HttpClient) {
        super(http);
    }

    public getForms(req: any): Observable<HttpResponse<FormioForm[]>> {
        const options = createRequestOption(req);
        return this.http.get<FormioForm[]>('/api/forms', { params: options, observe: 'response' });
    }

    public getScreeningForms(req: any): Observable<HttpResponse<FormioForm[]>> {
        const params = createRequestOption(req);
        return this.http.get<FormioForm[]>('/api/forms/screening', { params, observe: 'response' });
    }

    public getCustomerForms(req: any): Observable<HttpResponse<FormioForm[]>> {
        const params = createRequestOption(req);
        return this.http.get<FormioForm[]>('/api/forms/kyc', { params , observe: 'response' });
    }
    public updateForm(form: FormioForm): Observable<FormioForm> {
        return this.http.put('/api/forms/', form);
    }

    public setAsScreening(formId: String) {
        const url = `/api/forms/setscreeningform`;
        return this.http.post(url, formId);
    }
    
    public getSubmissions(formId: string, req: any): Observable<HttpResponse<FormioForm[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('/api/submissions/formall/' + formId, { params: options, observe: 'response' });
    }
    
}
