import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';
import { FormioForm } from 'angular-formio';
import { FormService } from 'app/forms/form.service';

@Injectable()
export class SubmissionsService extends FormService {
    constructor(protected http: HttpClient) {
        super(http);
    }

    public getFormSubmissionById(submissionId: String): Observable<any> {
        return this.http.get(`/api/submissions/kyc/${submissionId}`);
    }

    public getSubmissions(formId: string, req: any): Observable<HttpResponse<FormioForm[]>> {
        const options = createRequestOption(req);
        return this.http.get<any[]>('/api/submissions/form/' + formId, { params: options, observe: 'response' });
    }

    public downLoadScreeningData(formId: string): Observable<any>  {
       return this.http.get('/api/form-submission/' + formId + '/export');
    }

    public batchScreening(data): Observable<any> {
        return this.http.post('/api/forms/batch-screening', data);
    }
}
