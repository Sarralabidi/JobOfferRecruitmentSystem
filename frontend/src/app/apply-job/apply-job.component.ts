import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JobApplicationService } from '../services/job-application.service';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-apply-job',
  templateUrl: './apply-job.component.html',
  styleUrls: ['./apply-job.component.css']
})
export class ApplyJobComponent {
  jobTitle: string = '';
  jobOfferId!: number;
  userId: number = 123; // Replace with actual logged-in user ID
  selectedFile: File | null = null;
  jobForm: FormGroup;
  status: string | undefined; 
  applicationDate: Date | undefined;
  constructor(
    private route: ActivatedRoute,
    private jobApplicationService: JobApplicationService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.jobForm = this.fb.group({
      fullName: [''],
      email: [''],
      coverLetter: ['']
    });
  }

  ngOnInit(): void {
    this.jobOfferId = +this.route.snapshot.paramMap.get('id')!;
    this.jobTitle = this.route.snapshot.paramMap.get('title')!;
    console.log('Job Offer ID:', this.jobOfferId);    console.log(this.jobOfferId);
    console.log(this.jobTitle);
   

  }

  // Handle file selection
  onFileSelected(event: any): void {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  // Submit job application
  applyNow(): void {
    const formData = new FormData();
    formData.append('fullName', this.jobForm.get('fullName')?.value);
    formData.append('email', this.jobForm.get('email')?.value);
    formData.append('coverLetter', this.jobForm.get('coverLetter')?.value);
    formData.append('jobOfferId', this.jobOfferId.toString());
    formData.append('userId', this.userId.toString());

    if (this.selectedFile) {
      formData.append('cvFile', this.selectedFile, this.selectedFile.name);
    }

    this.jobApplicationService.applyForJob(formData).subscribe({
      next: (response) => {
        alert('Application submitted successfully!');
        this.jobForm.reset();
        this.selectedFile = null;
        this.router.navigate(['/listoffers']);
      },
      error: (error) => {
        alert('Error submitting application.');
        console.error(error);
      }
    });
  }
}
