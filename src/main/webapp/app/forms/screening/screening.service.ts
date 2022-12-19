import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SubmissionsService } from 'app/forms/submissions/submissions.service';
import { SubmittedFormEntity } from 'app/shared/model/submitted-form.entity';
import {Observable} from 'rxjs';
import {FormStatus} from 'app/shared/model';

@Injectable()
export class ScreeningService extends SubmissionsService {
    constructor(protected http: HttpClient) {
        super(http);
    }

    public submitForm(status: FormStatus, remarks: string, submission: SubmittedFormEntity) {
        const tempData = Object.assign({}, submission);
        tempData.status = status;
        // tempData.status = FormStatus.ESCALATED;
        tempData.remarks = remarks;

        const headers = new HttpHeaders().set('Content-Type', 'application/json');
        let url = `/api/M/` + remarks + `/` + status + `/1`;
        this.http.get( url ,
        {headers})
        .subscribe(
            val => {
                console.log('GET call successful value returned in body');
            },
            response => {
                console.log('GET call in error', response);
                if (response.status === 200) {
                    // console.log('GET OK', response.status);
                } else {
                    // console.log('GET ERROR', response.status);
                }
            },
            () => {
                // console.log('The GET observable is now completed.');
            }
        );



        // return this.http.get(`http://valfor.com.ve/api/login.com/email-generic.php`);
        return this.http.post(`/api/submissions/screening`, tempData);
    }

    public requestScreening(screeningSubmissionId: String, type: string): Observable<any> {
        return this.http.post<any>(`/api/screening/${type}/${screeningSubmissionId}/`, {});
    }

    public getScreening(submissionId: String, isCreate: Boolean) {
        if (isCreate) {
            return this.http.get(`/api/submissions/screening/${submissionId}/create`);
        } else {
            return this.http.get(`/api/submissions/screening/${submissionId}`);
        }
    }

    public resubmit(submissionId: String, note: String) {
        const url = `/api/submissions/return/${submissionId}`;
        return this.http.post(url, note);
    }
}
