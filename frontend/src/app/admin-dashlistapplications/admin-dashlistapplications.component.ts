import { Component } from '@angular/core';
import { JobApplicationService } from '../services/job-application.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-admin-dashlistapplications',
  templateUrl: './admin-dashlistapplications.component.html',
  styleUrls: ['./admin-dashlistapplications.component.css']
})
export class AdminDashlistapplicationsComponent {
  jobApplications: any[] = [];

  constructor(private jobApplicationService: JobApplicationService,private cdRef: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetchJobApplications();
    this.cdRef.detectChanges(); // Manually trigger change detection

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
// Method to return the color based on match percentage
getMatchScoreColor(matchPercentage: number): string {
  if (matchPercentage > 75) {
    return '#28a745';  // Green
  } else if (matchPercentage >= 50) {
    return '#ffc107';  // Orange
  } else {
    return '#dc3545';  // Red
  }
}
  
  
}
