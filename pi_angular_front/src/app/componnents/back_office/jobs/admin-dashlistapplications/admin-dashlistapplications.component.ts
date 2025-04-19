import { Component } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { JobApplicationService } from 'src/app/services/jobs/job-application.service';
import { DomSanitizer } from '@angular/platform-browser';
import { saveAs } from 'file-saver'; // make sure file-saver is installed
import { JobOffer } from 'src/app/models/jobs/JobOffer';
import { JobOfferService } from 'src/app/services/jobs/job-offer.service';
@Component({
  selector: 'app-admin-dashlistapplications',
  templateUrl: './admin-dashlistapplications.component.html',
  styleUrls: ['./admin-dashlistapplications.component.css']
})
export class AdminDashlistapplicationsComponent {
scheduleinterview() {
throw new Error('Method not implemented.');
}


  jobApplications: any[] = [];
  showScheduleModal: boolean = false;


  
// Add these variables:
currentPage: number = 1;
itemsPerPage: number = 5; // Change as needed
paginatedApplications: any[] = [];
totalPages: number[] = [];


  constructor(private jobApplicationService: JobApplicationService,private cdRef: ChangeDetectorRef,private sanitizer: DomSanitizer,private jobOfferService: 
    JobOfferService

  ) {


  }

  ngOnInit(): void {
    this.fetchJobApplications();
    this.cdRef.detectChanges(); // Manually trigger change detection
    this.updatePaginatedApplications();//added this

  }

  onStatusChange(application: any, newStatus: string): void {
    if (application.status !== newStatus) {
      application.status = newStatus;
      this.jobApplicationService.updateApplicationStatus(application.id, newStatus).subscribe({
        next: () => {
          console.log(`Status updated to ${newStatus}`);
          // Optionally trigger a toast or UI update here
        },
        error: err => {
          console.error('Failed to update status', err);
        }
      });
    }
  }
  
  
// i aded those 2 methods
  updatePaginatedApplications(): void {
    const total = Math.ceil(this.jobApplications.length / this.itemsPerPage);
    this.totalPages = Array.from({ length: total }, (_, i) => i + 1);

    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.paginatedApplications = this.jobApplications.slice(start, end);
  }
  
  changePage(page: number): void {
    if (page < 1 || page > this.totalPages.length) return;
    this.currentPage = page;
    this.updatePaginatedApplications();
  }





  fetchJobApplications(): void {
    this.jobApplicationService.getJobApplications().subscribe({
      next: (data: any[]) => {
        this.jobApplications = data;
        console.log("Fetched Applications:", this.jobApplications);

        this.updatePaginatedApplications();// i added this line
        

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
viewCV(id: number): void {
  console.log("Fetching CV for ID:", id); //  Add this

  this.jobApplicationService.getCVById(id).subscribe(
    (blob: Blob) => {
      const file = new Blob([blob], { type: 'application/pdf' });
      const fileURL = URL.createObjectURL(file);
      window.open(fileURL); // open in new tab
      // OR download:
      // saveAs(file, "cv.pdf");
    },
    (error: any) => {
      console.error('Error fetching CV', error);
    }
  );
  
}


scheduleInterview() {
  this.showScheduleModal = true;
}

closeScheduleModal() {
  this.showScheduleModal = false;
}

handleAdminSlots(slots: any[]) {
  console.log("Selected time slots by admin:", slots);
  // TODO: send to backend or trigger email logic
}



}
