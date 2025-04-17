import { Component, OnInit } from '@angular/core';
import { Event,EventService } from 'src/app/services/event/event.service';

declare var bootstrap: any; // For Bootstrap 5 modal integration

@Component({
  selector: 'app-user-event',
  templateUrl: './user-event.component.html'
})
export class UserEventComponent implements OnInit {
  events: Event[] = [];

  searchTerm: string = '';
  currentPageVirtual = 1;
  currentPagePhysical = 1;
  itemsPerPage = 5;

  selectedEventId: number | null = null;
  selectedEventTitle: string = '';
  formData = {
    username: '',
    email: ''
  };
  responseMessage: string = '';

  modal: any;

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getEvents().subscribe((events: Event[]) => {
      this.events = events;
    });
  }

  get filteredVirtualEvents(): Event[] {
    return this.events
      .filter(ev => ev.isVirtual && this.matchSearch(ev))
      .slice((this.currentPageVirtual - 1) * this.itemsPerPage, this.currentPageVirtual * this.itemsPerPage);
  }

  get filteredPhysicalEvents(): Event[] {
    return this.events
      .filter(ev => !ev.isVirtual && this.matchSearch(ev))
      .slice((this.currentPagePhysical - 1) * this.itemsPerPage, this.currentPagePhysical * this.itemsPerPage);
  }

  matchSearch(ev: Event): boolean {
    const term = this.searchTerm.toLowerCase();
    return ev.title.toLowerCase().includes(term) || ev.description.toLowerCase().includes(term);
  }

  get totalPagesVirtual(): number {
    return Math.ceil(
      this.events.filter(ev => ev.isVirtual && this.matchSearch(ev)).length / this.itemsPerPage
    );
  }

  get totalPagesPhysical(): number {
    return Math.ceil(
      this.events.filter(ev => !ev.isVirtual && this.matchSearch(ev)).length / this.itemsPerPage
    );
  }

  changePageVirtual(page: number): void {
    this.currentPageVirtual = page;
  }

  changePagePhysical(page: number): void {
    this.currentPagePhysical = page;
  }

  openParticipationModal(event: Event): void {
    this.selectedEventId = event.eventId!;
    this.selectedEventTitle = event.title;
    this.formData = { username: '', email: '' };
    this.responseMessage = '';

    // Bootstrap Modal integration fix
    this.modal = new bootstrap.Modal(document.getElementById('participationModal'));
    this.modal.show();
  }

  closeModal(): void {
    this.modal.hide();
  }

  submitParticipation(): void {
    if (!this.selectedEventId || !this.formData.username.trim() || !this.formData.email.trim()) {
      this.responseMessage = 'Please fill all fields.';
      return;
    }

    this.eventService.subscribeToEvent(this.selectedEventId, this.formData).subscribe({
      next: (res: string) => {
        this.responseMessage = res;
        setTimeout(() => this.closeModal(), 2000); // close after 2 seconds
      },
      error: (err: any) => {
        this.responseMessage = err.error || 'Subscription failed.';
      }
    });
  }
}
