import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { FooterComponent } from './footer/footer.component';
import { LoadingComponent } from './loading/loading.component';
import { AlertsComponent } from './alerts/alerts.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from './alerts/alert.service';

/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
  imports: [CommonModule, RouterModule,NgbModule],
  declarations: [NavbarComponent, SidebarComponent, FooterComponent,LoadingComponent, AlertsComponent],
  exports: [CommonModule, FormsModule, RouterModule,
  NavbarComponent, SidebarComponent, FooterComponent,LoadingComponent, AlertsComponent],
  providers: [AlertService],
})
export class SharedModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SharedModule,
      providers: []
    };
  }
}
