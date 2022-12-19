import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {FormEntity} from 'app/shared/model';
import { createRequestOption } from 'app/shared';

@Injectable()
export class ExternalFormService {
    public formId: any;
    public uniqueId: string;
    public validationId: string;
    public viewOnly = false;
    constructor(private httpClient: HttpClient) {}

    public getForm(formId: String): Observable<FormEntity> {
        return this.httpClient.get<FormEntity>('/api/forms/' + formId);
    }

    public saveSubmission(submission: any): Observable<{}> {
        if (!this.viewOnly) {
            let query = '';
            if (this.uniqueId) {
                query = '?uniqueId=' + this.uniqueId;
            }
            if (this.validationId) {
                query = query + (query.includes('?') ? '&' : '&') + 'validationId=' + this.validationId;
            }
            return this.httpClient.post<any>('/api/submissions/external/' + this.formId + query, submission);
        }
        return new Observable((observer) => {
            observer.next(submission);
        })
    }

    public getFormData(formId: any) {
        const req = { };
        if (this.validationId) {
            req['validationId'] = this.validationId;
        }
        if (this.uniqueId) {
            req['uniqueId'] = this.uniqueId;
        }
        const params = createRequestOption(req);
        const url = '/api/submissions/external/form-data/' + this.formId;
        return this.httpClient.get<any>(url, { params });
    }
}
