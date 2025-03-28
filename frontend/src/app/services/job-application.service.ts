import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JobApplication} from '../models/JobApplication';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class JobApplicationService {
  private apiUrl = 'http://localhost:9090/api/applications';

  constructor(private http: HttpClient) { }
 // Add a new job application
 applyForJob(jobApplication: FormData): Observable<any> {
  return this.http.post(`${this.apiUrl}/create`, jobApplication);
}
getJobApplications(): Observable<any[]> {
  return this.http.get<JobApplication[]>(`${this.apiUrl}`);
}


}
