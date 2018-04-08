import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { UserService } from './user.service';

@Injectable()
export class GuardService implements CanActivate {
  constructor(private user: UserService) {}

  canActivate() {
    return this.user.isLoggedReady();
  }
}
