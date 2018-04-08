import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { APP_BASE_HREF } from '@angular/common';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { SharedModule } from './shared/shared.module';
import { ServicesModule } from './services/services.module';
import { FeaturesModule } from './features/features.module';
import { PipesModule } from './pipes/pipes.module';

declare var BASE_LOCATION_HREF:any;

@NgModule({
  imports: [BrowserModule, HttpModule, NgbModule.forRoot(), AppRoutingModule, SharedModule.forRoot(),ServicesModule, FeaturesModule, PipesModule],
  declarations: [AppComponent],
  providers: [{
    provide: APP_BASE_HREF,
    useValue: BASE_LOCATION_HREF
  }],
  bootstrap: [AppComponent]

})
export class AppModule { 
	constructor(){
	}
}
