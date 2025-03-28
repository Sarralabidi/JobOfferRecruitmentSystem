import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListJobOffersComponent } from './list-job-offers/list-job-offers.component';
import { ApplyJobComponent } from './apply-job/apply-job.component';
import { AdminDashlistapplicationsComponent } from './admin-dashlistapplications/admin-dashlistapplications.component';
import { AdminCrudJobOffersComponent } from './admin-crud-job-offers/admin-crud-job-offers.component';

const routes: Routes = [
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
