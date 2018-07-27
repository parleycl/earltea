import { NgModule, ModuleWithProviders, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserService } from './user/user.service';
import { GuardService } from './user/guard.service';
import { RestService } from './rest/rest.service';
import { LoadingService } from './loading/loading.service';
import { DatasourcesService } from './datasources/datasources.service';
import { PropertiesService } from './properties/properties.service';
import { RoutesService } from './routes/routes.service';
import { ControllersService } from './controllers/controllers.service';
import { ModelsService } from './models/models.service';
import { Observable } from 'rxjs/Observable';
import { ConfigService } from './config/config.service';
import { ClusterService } from './cluster/cluster.service';
import { CacheService } from './cache/cache.service';
/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
  imports: [],
  declarations: [],
  providers:[RestService, UserService, GuardService, LoadingService, DatasourcesService, PropertiesService, RoutesService, 
    ControllersService, ModelsService, ConfigService, ClusterService, CacheService],
  exports: []
})
export class ServicesModule {
}
