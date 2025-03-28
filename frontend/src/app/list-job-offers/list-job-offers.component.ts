import { Component } from '@angular/core';
import { JobOffer } from '../models/JobOffer';
import { JobOfferService } from '../services/job-offer.service';

@Component({
  selector: 'app-list-job-offers',
  templateUrl: './list-job-offers.component.html',
  styleUrls: ['./list-job-offers.component.css']
})
export class ListJobOffersComponent {
applyForJob(arg0: number|undefined) {
throw new Error('Method not implemented.');
}
  jobOffers: JobOffer[] = [];
  errorMessage: string = '';


  constructor(private jobOfferService: JobOfferService) {}

  ngOnInit(): void {
    this.fetchJobOffers();
  }

  fetchJobOffers(): void {
    this.jobOfferService.getJobOffers().subscribe((data: JobOffer[]) => {
      this.jobOffers = data;
      console.log(this.jobOffers);
    });
  }

}
