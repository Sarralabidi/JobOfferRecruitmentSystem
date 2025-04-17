import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './componnents/front_office/template/home/home.component';
import { HomeBackComponent } from './componnents/back_office/template/home-back/home-back.component';
import { HeaderBackComponent } from './componnents/back_office/template/header-back/header-back.component';
import { EventMapComponent } from './componnents/front_office/event/event-map/event-map.component';
import { UserEventComponent } from './componnents/front_office/event/user-event/user-event.component';
import { VirtualMeetingComponent } from './componnents/front_office/event/virtual-meeting/virtual-meeting.component';
import { EventComponent } from './componnents/back_office/event/event/event.component';
import { EventCategoryComponent } from './componnents/back_office/event/event-category/event-category.component';
import { AdminCrudJobOffersComponent } from './componnents/back_office/jobs/admin-crud-job-offers/admin-crud-job-offers.component';
import { AdminDashlistapplicationsComponent } from './componnents/back_office/jobs/admin-dashlistapplications/admin-dashlistapplications.component';
import { ApplyJobComponent } from './componnents/front_office/jobs/apply-job/apply-job.component';
import { ListJobOffersComponent } from './componnents/front_office/jobs/list-job-offers/list-job-offers.component';

const routes: Routes = [
    {path : '', component : HomeComponent},
    {path : 'Back', component : HeaderBackComponent},
  { path: 'categories', component: EventCategoryComponent },
  { path: 'events', component: EventComponent },
  { path: 'user-events', component: UserEventComponent },
  { path: 'event-map/:id', component: EventMapComponent },
  { path: 'virtual-meeting/:id', component: VirtualMeetingComponent },
  //{ path: '**', redirectTo: '' } // page not found fallback
  {path:'listoffers',component:ListJobOffersComponent},
  { path: 'applyjob/:id/:title', component: ApplyJobComponent },
  {path:'admin/applications',component:AdminDashlistapplicationsComponent},
  {path:'admin/offers',component:AdminCrudJobOffersComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
