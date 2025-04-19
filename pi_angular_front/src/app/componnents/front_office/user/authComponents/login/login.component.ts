import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthResponseDTO } from 'src/app/models/user/auth-response.dto';
import { AuthService } from 'src/app/services/user/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.errorMessage = '';

    this.authService.login(this.username, this.password).subscribe(
      (response: AuthResponseDTO) => {
        this.authService.saveToken(response.token);

        this.router.navigate(['/Home']);
      },
      error => {
        this.errorMessage = 'Login failed! Check your credentials.';
      }
    );
  }

  loginWithGitHub() {
    window.location.href = 'http://localhost:9090/project_war_exploded/oauth2/authorization/github';
  }

}
