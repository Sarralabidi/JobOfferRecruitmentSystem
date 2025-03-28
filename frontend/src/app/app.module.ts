import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ListJobOffersComponent } from './list-job-offers/list-job-offers.component';
import{    HttpClientModule} from '@angular/common/http';
import { ApplyJobComponent } from './apply-job/apply-job.component';
import { AdminDashlistapplicationsComponent } from './admin-dashlistapplications/admin-dashlistapplications.component'
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdminCrudJobOffersComponent } from './admin-crud-job-offers/admin-crud-job-offers.component';

@NgModule({
  declarations: [
    AppComponent,
    ListJobOffersComponent,
    ApplyJobComponent,
    AdminDashlistapplicationsComponent,
    AdminCrudJobOffersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
