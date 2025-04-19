import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { ProfileUpdateDTO } from 'src/app/models/user/profile-update.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:9090/api/user';
  

  constructor(private http: HttpClient) {}

  updateProfileWithFile(profileData: ProfileUpdateDTO, file?: null | File): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      return throwError(() => new Error('No authentication token found'));
    }

    // Create FormData for multipart upload
    const formData = new FormData();
    formData.append('profileData', JSON.stringify(profileData));

    if (file) {
      formData.append('file', file, file.name);
    }

    // Note: Don't set Content-Type header - let browser set it with boundary
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
      // 'Content-Type' is omitted intentionally for FormData!
    });

    return this.http.put(`${this.apiUrl}/update-profile`, formData, { headers }).pipe(
      catchError(error => {
        console.error('Error updating profile:', error);
        return throwError(() => error);
      })
    );
  }

  // Keep existing JSON-only method for backward compatibility
  updateProfile(profileUpdate: ProfileUpdateDTO): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      return throwError(() => new Error('No authentication token found'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.http.put(`${this.apiUrl}/update-profile`, profileUpdate, { headers }).pipe(
      catchError(error => {
        console.error('Error updating profile:', error);
        return throwError(() => error);
      })
    );
  }

  deleteProfile(username: string): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    if (!token) {
      return throwError(() => new Error('No authentication token found'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.delete(`${this.apiUrl}/profile/delete/@${username}`, { headers });
  }}
