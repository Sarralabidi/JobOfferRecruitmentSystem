import { Component } from '@angular/core';
import {Router} from "@angular/router";
import { AuthService } from 'src/app/services/user/auth.service';

@Component({
  selector: 'app-forgetpassword',
  templateUrl: './forgetpassword.component.html',
  styleUrls: ['./forgetpassword.component.css']
})
export class ForgetpasswordComponent {

  email: string = '';
  errorMessage: string = '';
  constructor(private authService: AuthService, private router: Router) {}

  submit() {
    this.errorMessage = ''; // Clear previous errors

    this.authService.forgotPassword(this.email).subscribe(
      () => {
        this.router.navigate(['/login']);
      },
      error => {
        this.errorMessage = error.error?.message || 'Email not found. Please try again.';
      }
    );
  }


}
