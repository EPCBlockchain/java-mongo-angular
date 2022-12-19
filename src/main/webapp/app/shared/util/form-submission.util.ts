import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';

@Injectable()
export class FormSubmissionUtil {

    private images: any[] = [];

    constructor(private http: HttpClient, private toastr: ToastrService) {}

    public fetchAndReplaceImages(submission: any): void {
        this.images = [];
        if (submission) {
            Object.keys(submission).forEach( key => {
               if (typeof submission[key] === typeof {} && submission[key].hashUrl) {
                   this.images.push({ key, image : submission[key] });
                   submission[key] = null;
               }
            });
        }
    }

    public startLazyLoadImages(data: any, callback: Function) {
        let tostr = null;
        if (data && this.images.length) {
            tostr = this.toastr.info('Loading Images');
        }
        let isDone = false;
        this.images.forEach(imageField => {
            imageField.isDoneLoading = false;
            this.http.get(`/api/data/image/${imageField.image.hashUrl}`).subscribe(image => {
                imageField.isDoneLoading = true;
                isDone = !this.images.some(img => !img.isDoneLoading);
                data[imageField.key] = image;
                if (isDone) {
                    callback(data);
                    if (tostr) {
                        this.toastr.clear(tostr.toastId);
                    }
                }
            });
        });
    }

}
