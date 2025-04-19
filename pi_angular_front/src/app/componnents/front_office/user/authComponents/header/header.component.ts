import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/user/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  username: string | null = null;
  role: string | null = null;
  userPhotoUrl: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  isDropdownOpen = false;

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }


  ngOnInit() {
    this.authService.authStatus$.subscribe((status: boolean) => {
      this.isLoggedIn = status;
      if (this.isLoggedIn) {
        this.username = this.authService.getCurrentUserUsername();  // Replace with actual field in the token
        this.role = this.authService.getRole();          // Replace with actual field in the token
        this.userPhotoUrl = this.authService.getCurrentUserPicture();
      }

    });
  }

  logout() {
    this.authService.logout();
    this.username = null;
    this.role = null;
    this.router.navigate(['/login']);
  }
}
