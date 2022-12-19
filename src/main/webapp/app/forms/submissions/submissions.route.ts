import { Route } from '@angular/router';
import { FormSubmissionsComponent } from 'app/forms/submissions/submissions.component';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { SubmissionDetailComponent } from 'app/forms/submissions/detail/submission-detail.component';

export const submissionsRoute: Route[] = [
    {
        path: ':formId/submissions',
        component: FormSubmissionsComponent,
        data: {
            defaultSort: 'status,asc',
            authorities: ['ROLE_OBTEAM', 'ROLE_ORGANIZATION_ADMIN']
        },
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'submissions/:submissionId',
        component: SubmissionDetailComponent,
        data: {
            authorities: ['ROLE_OBTEAM', 'ROLE_ORGANIZATION_ADMIN']
        },
        canActivate: [UserRouteAccessService]
    }
];
