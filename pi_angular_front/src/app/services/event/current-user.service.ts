import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {
  private currentUser = {
    id: 1,
    username: 'demoUser',
    role: 'USER' // or 'ADMIN'
  };

  getCurrentUser() {
    return this.currentUser;
  }

  getUserId(): number {
    return this.currentUser.id;
  }

  isAdmin(): boolean {
    return this.currentUser.role === 'ADMIN';
  }

  isUser(): boolean {
    return this.currentUser.role === 'USER';
  }
}
