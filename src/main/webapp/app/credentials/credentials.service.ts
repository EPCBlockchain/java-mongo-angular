import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {createRequestOption} from 'app/shared'
import {CredentialEntity} from "app/shared/model/credential.entity"

@Injectable()
export class CredentialsService {
    constructor(private httpClient: HttpClient) { }

    getCredentials(request: any) {
        const options = createRequestOption(request);
        return this.httpClient.get('/api/credentials', { params: options, observe: 'response' });
    }

    getById(id: string) {
        return this.httpClient.get('/api/credentials/' + id);
    }

    save(entity: CredentialEntity) {
        return this.httpClient.post('/api/credentials', entity);
    }

    generateReportPDF(){
        return this.httpClient.get('/api/credentials/generate-report-pdf',{responseType:'blob'})
    }

    generateReportExcel(){
        return this.httpClient.get('/api/credentials/generate-report-excel',{responseType:'blob'})
    }

}
