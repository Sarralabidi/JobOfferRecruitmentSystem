import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HomeComponent } from './componnents/front_office/template/home/home.component';
import { HeaderComponent } from './componnents/front_office/template/header/header.component';
import { FooterComponent } from './componnents/front_office/template/footer/footer.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { HomeBackComponent } from './componnents/back_office/template/home-back/home-back.component';
import { HeaderBackComponent } from './componnents/back_office/template/header-back/header-back.component';
import { AppRoutingModule } from './app-routing.module';
import { EventComponent } from './componnents/back_office/event/event/event.component';
import { EventCategoryComponent } from './componnents/back_office/event/event-category/event-category.component';
import { UserEventComponent } from './componnents/front_office/event/user-event/user-event.component';
import { EventMapComponent } from './componnents/front_office/event/event-map/event-map.component';
import { VirtualMeetingComponent } from './componnents/front_office/event/virtual-meeting/virtual-meeting.component';
import { HttpClientModule } from '@angular/common/http';
import { ListJobOffersComponent } from './componnents/front_office/jobs/list-job-offers/list-job-offers.component';
import { ApplyJobComponent } from './componnents/front_office/jobs/apply-job/apply-job.component';
import { AdminCrudJobOffersComponent } from './componnents/back_office/jobs/admin-crud-job-offers/admin-crud-job-offers.component';
import { AdminDashlistapplicationsComponent } from './componnents/back_office/jobs/admin-dashlistapplications/admin-dashlistapplications.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    EventComponent,
    EventCategoryComponent,
    UserEventComponent,
    EventMapComponent,
    VirtualMeetingComponent,
    HomeComponent,
    HomeBackComponent,
    HeaderBackComponent,
    ListJobOffersComponent,
    ApplyJobComponent,
    AdminCrudJobOffersComponent,
    AdminDashlistapplicationsComponent,
    

   
  ],
  imports: [
    BrowserModule,
    FormsModule,
    RouterLink,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
