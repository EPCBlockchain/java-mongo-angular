import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';

@Injectable()
export class ProfileService {
    constructor(private http: HttpClient) {}

    uploadProfileImage(formData: FormData): Observable<any> {
        const url = SERVER_API_URL + 'api/formios/submit';
        return this.http.post(url, formData);
    }
}
