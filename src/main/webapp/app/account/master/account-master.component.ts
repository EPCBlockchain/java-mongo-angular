import { Component } from '@angular/core';
import { GlobalConfig } from '../../core/config';

@Component({
    templateUrl: './account-master.component.html',
    styleUrls: ['./account-master.component.scss']
})
export class AccountMasterComponent {
    version = GlobalConfig.VERSION_NUMBER;
}
