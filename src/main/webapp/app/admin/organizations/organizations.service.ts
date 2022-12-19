import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../shared';
import { HttpClient } from '@angular/common/http';
import { OrganizationEntity } from '../../shared/model';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';

@Injectable()
export class OrganizationService {
    currentData: OrganizationEntity;
    constructor(private http: HttpClient) {}

    fileType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
    fileExtension = '.xlsx';

    public queryOrganizations(req: any = {}): Observable<any> {
        const url = '/api/organizations/';
        const options = createRequestOption(req);
        return this.http.get<any>(url, { params: options, observe: 'response' });
    }

    public update(org: OrganizationEntity) {
        const url = '/api/organizations';
        return this.http.put<OrganizationEntity>(url, org);
    }

    public create(org: any): Observable<Object> {
        const url = 'api/organizations';
        return this.http.post(url, org);
    }

    public get(orgId: string): Observable<OrganizationEntity> {
        const url = 'api/organizations/' + orgId;
        return this.http.get<OrganizationEntity>(url);
    }

    generateReportPDF(){
        return this.http.get('/api/organizations/generate-report-pdf',{responseType:'blob'})
    }

    generateReportExcel(){
        return this.http.get('/api/organizations/generate-report-excel',{responseType:'blob'})
    }

    // public exportExcel(jsonData: any[], fileName: string): void {

    //     const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(jsonData);
    //     const wb: XLSX.WorkBook = { Sheets: { 'data': ws }, SheetNames: ['data'] };
    //     const excelBuffer: any = XLSX.write(wb, { bookType: 'xlsx', type: 'array' });
    //     this.saveExcelFile(excelBuffer, fileName);
    //   }
    
    //   private saveExcelFile(buffer: any, fileName: string): void {
    //     const data: Blob = new Blob([buffer], {type: this.fileType});
    //     FileSaver.saveAs(data, fileName + this.fileExtension);
    //   }



}
