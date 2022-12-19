import { Injectable } from '@angular/core';
import { SessionStorageService, LocalStorageService } from 'angular-web-storage';

@Injectable({ providedIn: 'root' })
export class StateStorageService {
    constructor(private $localStorage: LocalStorageService) {}

    getPreviousState() {
        return this.$localStorage.get('previousState');
    }

    resetPreviousState() {
        this.$localStorage.remove('previousState');
    }

    storePreviousState(previousStateName, previousStateParams) {
        const previousState = { name: previousStateName, params: previousStateParams };
        this.$localStorage.set('previousState', previousState);
    }

    getDestinationState() {
        return this.$localStorage.get('destinationState');
    }

    storeUrl(url: string) {
        this.$localStorage.set('previousUrl', url);
    }

    getUrl() {
        return this.$localStorage.get('previousUrl');
    }

    storeDestinationState(destinationState, destinationStateParams, fromState) {
        const destinationInfo = {
            destination: {
                name: destinationState.name,
                data: destinationState.data
            },
            params: destinationStateParams,
            from: {
                name: fromState.name
            }
        };
        this.$localStorage.set('destinationState', destinationInfo);
    }
}
