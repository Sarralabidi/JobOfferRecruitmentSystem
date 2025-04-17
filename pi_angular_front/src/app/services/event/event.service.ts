import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { EventCategory } from './event-category.service';

export interface Event {
  eventId?: number;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  category: EventCategory | null;
  isVirtual: boolean;
  location?: string; 
}


@Injectable({ providedIn: 'root' })
export class EventService {
  private baseUrl = 'http://localhost:8080/api/events';

  constructor(private http: HttpClient) {}

  getEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(this.baseUrl).pipe(catchError(this.handleError));
  }

  create(event: Event): Observable<Event> {
    return this.http.post<Event>(this.baseUrl, event).pipe(catchError(this.handleError));
  }

  update(id: number, event: Event): Observable<Event> {
    return this.http.put<Event>(`${this.baseUrl}/${id}`, event).pipe(catchError(this.handleError));
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { responseType: 'text' }).pipe(catchError(this.handleError));
  }

  participateInEvent(userId: number, eventId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${eventId}/participate/${userId}`, {}).pipe(catchError(this.handleError));
  }

  subscribeToEvent(eventId: number, formData: { email: string; username: string }): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/${eventId}/subscribe`,
      formData,
      { responseType: 'text' }
    );
  }

  private handleError(error: any): Observable<never> {
    console.error('API error:', error);
    return throwError(() => error);
  }

  
  
}
