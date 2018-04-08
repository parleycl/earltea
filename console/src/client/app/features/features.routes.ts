import { Route } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { GuardService } from '../services/user/guard.service';
import { ClusterComponent } from './cluster/cluster.component';
import { ConfiguracionesComponent } from './configuraciones/configuraciones.component';
import { ControladoresComponent } from './controladores/controladores.component';
import { CronjobsComponent } from './cronjobs/cronjobs.component';
import { LogsComponent } from './logs/logs.component';
import { ModelosComponent } from './modelos/modelos.component';
import { PropertiesComponent } from './properties/properties.component';
import { RutasComponent } from './rutas/rutas.component';
import { LoginComponent } from './login/login.component';
import { IndexComponent } from './index/index.component';

export const FeaturesRoutes: Route[] = [
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'dashboard'
	},
	{
		path: '',
		component: IndexComponent,
		children: [
			{ path: 'dashboard', component: HomeComponent },
      { path: 'cluster', component: ClusterComponent },
      { path: 'config', component: ConfiguracionesComponent },
      { path: 'controllers', component: ControladoresComponent },
      { path: 'cronjobs', component: CronjobsComponent },
      { path: 'logs', component: LogsComponent },
      { path: 'models', component: ModelosComponent },
      { path: 'properties', component: PropertiesComponent },
      { path: 'routes', component: RutasComponent }
		],
		//ROUTER CHILDREN LIMIT (NOT REMOVE - CLI COMPONENT)
		canActivate: [GuardService],
  },
  {
    path: 'login',
    component: LoginComponent
  }
];