import { Component } from '@angular/core';
import { GlobalConfig } from '../../core/config';

@Component({
    selector: 'jhi-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
    versionNumber = GlobalConfig.VERSION_NUMBER;
}
