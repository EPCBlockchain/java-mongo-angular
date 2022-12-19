import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { createRequestOption } from 'app/shared';
import { OrganizationEntity } from 'app/shared/model';

@Injectable()
export class ListOrgsService {
    constructor(private http: HttpClient) {}
    
}
