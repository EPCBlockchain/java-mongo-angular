import { Component, Input, OnChanges } from '@angular/core';

@Component({
    templateUrl: './identity-mind.component.html',
    styleUrls: ['./identity-mind.component.scss'],
    selector: 'jhi-identity-mind-screening'
})
export class IdentityMindScreeningComponent implements OnChanges {
    @Input() public inputResult: any;

    public data: any = {
        result: {
            details: []
        },
        idResults: []
    };

    ngOnChanges() {
        if (this.inputResult) {
            this.data.result = { details : [] };
            this.data.idResults = [];
            this.data.result['name'] = this.inputResult['reportedRule'].name;
            this.data.result['resultCode'] = this.inputResult['reportedRule'].resultCode;
            this.data.result['description'] = this.inputResult['reportedRule'].description;

            (this.inputResult.reportedRule.testResults as any[]).forEach((result: any) => {
                const data = { };
                if (result.fired) {
                    result.details = result.details.substring(8);
                    data['value'] = result.details;
                    data['fired'] = result.fired;
                }
                if (!this.data.result.details.some(a => a.value === data['value'])) {
                    this.data.result.details.push(data);
                }
            });

            if (this.inputResult.firedRules) {
                for (let i = 0; i < this.inputResult.firedRules.length; i++) {
                    const rule = this.inputResult.firedRules[i];

                    this.data.idResults.push({
                        id: i,
                        ruleId: rule.ruleId,
                        name: rule.name,
                        resultCode: rule.resultCode,
                        description: rule.description,
                        details: rule.details
                    });
                }
            }
            console.log(this.data);
        }

        console.log(this.data.idResults);
    }
}
