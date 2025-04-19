import { Component, EventEmitter, Output } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core';

import interactionPlugin from '@fullcalendar/interaction';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
@Component({
  selector: 'app-scheduleinterviewadmin',
  templateUrl: './scheduleinterviewadmin.component.html',
  styleUrls: ['./scheduleinterviewadmin.component.css']
})
export class ScheduleinterviewadminComponent {
  @Output() closeModal = new EventEmitter<void>();
  @Output() slotsSelected = new EventEmitter<any[]>();

  selectedSlots: any[] = [];
  selectedEvents: any[] = [];

  calendarOptions: CalendarOptions = {
    plugins: [interactionPlugin, dayGridPlugin, timeGridPlugin],

    initialView: 'timeGridWeek',
    selectable: true,
    selectMirror: true,
    select: this.handleDateSelect.bind(this), // Callback on select
    unselectAuto: false,
    events: this.selectedEvents, // to display selected slots
    editable: false,
    nowIndicator: true,
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'timeGridDay,timeGridWeek,dayGridMonth'
    }
    
  };

  submitSlots() {
    this.slotsSelected.emit(this.selectedSlots);
    this.closeModal.emit();
  }

  cancel() {
    this.closeModal.emit();
  }

  
handleDateSelect(selectInfo: any) {
  const calendarApi = selectInfo.view.calendar;
  calendarApi.unselect(); // Clear selection visually

  const newEvent = {
    title: 'Available',
    start: selectInfo.start,
    end: selectInfo.end,
    allDay: selectInfo.allDay
  };

  this.selectedEvents = [...this.selectedEvents, newEvent];

  // Refresh calendar with updated events
  calendarApi.addEvent(newEvent);

  console.log('Selected slots:', this.selectedEvents);
}



}
