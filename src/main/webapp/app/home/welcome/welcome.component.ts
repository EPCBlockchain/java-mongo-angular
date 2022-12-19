import { Component, OnInit } from '@angular/core';
import { Principal, UserService, User } from 'app/core';

@Component({
  selector: 'jhi-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  currentAccount: any;
  username: string;
  email: string;
  role: string;
  id: any;

  constructor(
    private userService: UserService,
    private principal: Principal,
  ) { }

  ngOnInit() {
    this.principal.identity().then(account => {
      this.currentAccount = account;
      this.username = this.currentAccount['firstName'];
      this.email = this.currentAccount['email'];
      this.role = this.currentAccount['authorities'];
      this.id = this.currentAccount['id'];
    });
  }
}
