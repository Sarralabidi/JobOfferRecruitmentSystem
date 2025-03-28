import { Component } from '@angular/core';
import { JobApplicationService } from '../services/job-application.service';

@Component({
  selector: 'app-admin-dashlistapplications',
  templateUrl: './admin-dashlistapplications.component.html',
  styleUrls: ['./admin-dashlistapplications.component.css']
})
export class AdminDashlistapplicationsComponent {
  jobApplications: any[] = [];

  constructor(private jobApplicationService: JobApplicationService) {}

  ngOnInit(): void {
    this.fetchJobApplications();
  }

  fetchJobApplications(): void {
    this.jobApplicationService.getJobApplications().subscribe({
      next: (data: any[]) => {
        this.jobApplications = data;
      },
      error: (error: any) => {
        console.error('Error fetching job applications:', error);
      }
    });
  }

  
  
}
