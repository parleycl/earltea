import { Injectable} from '@angular/core';
import { Config } from '../../shared/config/env.config';
import {Http, Response, Headers} from '@angular/http';
import { AlertsComponent } from './alerts.component';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';

@Injectable()
export class AlertService {
	alerts:AlertsComponent;
	//construimos los metodos
	constructor() {
	}
	reg_alert(alert_box:AlertsComponent){
		this.alerts = alert_box;
	}
	success(message:string){
		this.alerts.success(message);
	}
	info(message:string){
		this.alerts.info(message);
	}
	warning(message:string){
		this.alerts.warning(message);
	}
	error(message:string){
		this.alerts.error(message);
	}
}
