import { Component, OnInit } from '@angular/core';
import { EventCategory, EventCategoryService } from 'src/app/services/event/event-category.service';
import { EventService, Event as AppEvent } from 'src/app/services/event/event.service';


@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
  events: AppEvent[] = [];
  categories: EventCategory[] = [];

  newEvent: AppEvent = {
    title: '',
    description: '',
    startDate: '',
    endDate: '',
    category: null,
    isVirtual: false,
    location: ''
  };

  selectedEvent: AppEvent | null = null;

  constructor(
    private eventService: EventService,
    private categoryService: EventCategoryService
  ) {}

  ngOnInit(): void {
    this.getAllEvents();
    this.getAllCategories();
  }

  getAllEvents(): void {
    this.eventService.getEvents().subscribe(data => {
      this.events = data;
    });
  }

  getAllCategories(): void {
    this.categoryService.getEventCategories().subscribe(categories => {
      this.categories = categories;
    });
  }

  addEvent(): void {
    if (!this.validateEvent(this.newEvent)) return;

    const payload: AppEvent = {
      ...this.newEvent,
      category: { id: this.newEvent.category!.id, name: '' }
    };

    this.eventService.create(payload).subscribe(() => {
      this.getAllEvents();
      this.resetForm();
    });
  }

  editEvent(event: AppEvent): void {
    this.selectedEvent = { ...event };
  }

  updateEvent(): void {
    if (this.selectedEvent && this.validateEvent(this.selectedEvent)) {
      const payload: AppEvent = {
        ...this.selectedEvent,
        title: this.selectedEvent.title,
        description: this.selectedEvent.description,
        startDate: this.selectedEvent.startDate,
        endDate: this.selectedEvent.endDate,
        category: this.selectedEvent.category,
        isVirtual: this.selectedEvent.isVirtual,
        location: this.selectedEvent.location
      };

      this.eventService.update(this.selectedEvent.eventId!, payload).subscribe(() => {
        this.getAllEvents();
        this.cancelEdit();
      });
    }
  }

  deleteEvent(id: number): void {
    this.eventService.delete(id).subscribe(() => {
      this.getAllEvents();
    });
  }

  cancelEdit(): void {
    this.selectedEvent = null;
  }

  resetForm(): void {
    this.newEvent = {
      title: '',
      description: '',
      startDate: '',
      endDate: '',
      category: null,
      isVirtual: false,
      location: ''
    };
  }

  validateEvent(event: AppEvent): boolean {
    if (!event.title.trim() || !event.description.trim()) {
      alert('Title and description are required.');
      return false;
    }

    if (!event.startDate || !event.endDate) {
      alert('Start date and end date are required.');
      return false;
    }

    if (new Date(event.startDate) > new Date(event.endDate)) {
      alert('Start date must be before end date.');
      return false;
    }

    if (!event.category || !event.category.id) {
      alert('Please select a category.');
      return false;
    }

    if (!event.isVirtual && (!event.location || !event.location.trim())) {
      alert('Please specify a location for physical events.');
      return false;
    }

    return true;
  }
}
