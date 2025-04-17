import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { JobApplicationService } from 'src/app/services/jobs/job-application.service';

@Component({
  selector: 'app-apply-job',
  templateUrl: './apply-job.component.html',
  styleUrls: ['./apply-job.component.css']
})
export class ApplyJobComponent {
  //jobTitle: string = '';
  //jobOfferId!: number;
  userId: number = 123; // Replace with actual logged-in user ID
  selectedFile: File | null = null;
  jobForm: FormGroup;
  status: string | undefined; 
  applicationDate: Date | undefined;

  
  @Input() jobTitle: string = '';
  @Input() jobOfferId!: number;
  @Output() closeModal = new EventEmitter<void>();


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
    // Form for adding a new job offer
         this.jobForm = new FormGroup({ title: new FormControl('',
           [ Validators.required, Validators.minLength(4) ]),
           fullName: new FormControl('', [Validators.required]), 
           email: new FormControl('', [ Validators.required ,Validators.email]), 
           coverLetter: new FormControl('', Validators.required)});
        
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
        this.closeModal.emit(); // close the modal

        this.router.navigate(['/listoffers']);
      },
      error: (error) => {
        alert('Error submitting application.');
        console.error(error);
      }
    });
  }
  goback(): void {
    this.router.navigate(['/listoffers']);
  }
  
}
