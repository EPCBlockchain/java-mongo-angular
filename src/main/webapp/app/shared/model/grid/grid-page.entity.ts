import { ITEMS_PER_PAGE } from 'app/shared';

export class GridPageEntity {
    public error: any;
    public success: any;
    public routeData: any;
    public links: any;
    public totalItems: any;
    public queryCount: any;
    public itemsPerPage = ITEMS_PER_PAGE;
    public page: any;
    public predicate: any;
    public previousPage: any;
    public reverse: any;

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }
}
