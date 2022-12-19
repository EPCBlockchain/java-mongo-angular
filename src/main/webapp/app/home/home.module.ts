import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HomeRoutes } from 'app/home/home.route';
import { ProfileComponent } from 'app/home/profile/profile.component';
import { ProfileService } from 'app/home/profile/profile.service';
import { NotFoundComponent } from 'app/home/not-found/not-found.component';
import { WelcomeComponent } from './welcome/welcome.component';

@NgModule({
    declarations: [ProfileComponent, NotFoundComponent, WelcomeComponent],
    imports: [RouterModule.forChild(HomeRoutes)],
    exports: [RouterModule],
    providers: [ProfileService]
})
export class HomeModule {}
