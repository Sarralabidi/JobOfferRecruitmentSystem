import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/user/auth.service';
@Component({
  selector: 'app-schedule-interview-candidate',
  templateUrl: './schedule-interview-candidate.component.html',
  styleUrls: ['./schedule-interview-candidate.component.css']
})
export class ScheduleInterviewCandidateComponent {
  proposedSlots: string[] = [];
  selectedSlot: string = '';
  candidateId: number = 0; // or get from route params or localstorage
  currentusername: string | null | undefined;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private authservice : AuthService
  ) {}

  ngOnInit() {
    //l'id du personne qui a fait le job application
    this.candidateId = +this.route.snapshot.paramMap.get('id')!; 
    
    console.log("id is "+this.candidateId);
    this.fetchProposedSlots();
  }

  fetchProposedSlots() {
    this.http.get<string[]>(`http://localhost:9090/api/interviews/proposed-slots/${this.candidateId}`)
      .subscribe(slots => {
        this.proposedSlots = slots;
      });
  }

  submitSelectedSlot() {
    if (!this.selectedSlot) return;

    const payload = {
      candidateId: this.candidateId,
      selectedSlot: this.selectedSlot
    };

    this.http.post('http://localhost:9090/api/interviews/confirm-slot', payload)
      .subscribe(response => {
        alert('✅ Interview slot confirmed! We will be in touch.');
        this.router.navigate(['/thank-you-page']); // or wherever you want
      }, error => {
        alert('❌ Error confirming slot. Please try again later.');
      });
  }
}
