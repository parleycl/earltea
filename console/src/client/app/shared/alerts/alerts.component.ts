import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AlertService } from './alert.service';

/**
*	This class represents the lazy loaded LoginComponent.
*/

@Component({
	moduleId: module.id,
	selector: 'alert-cmp',
	templateUrl: 'alerts.component.html',
	styleUrls: ['alerts.component.css']
})

export class AlertsComponent {
	private view:string;
	private message:string;
	constructor(private alerts:AlertService, router:Router) {
		this.register();
	}
	register(){
		this.alerts.reg_alert(this);
	}
	success(message:string){
		this.view = 'success';
		this.message = message;
		setTimeout(() => {
			this.view = '';
		}, 2000);
	}
	info(message:string){
		this.view = 'info';
		this.message = message;
		setTimeout(() => {
			this.view = '';
		}, 3000);
	}
	warning(message:string){
		this.view = 'warning';
		this.message = message;
		setTimeout(() => {
			this.view = '';
		}, 5000);
	}
	error(message:string){
		this.view = 'error';
		this.message = message;
		setTimeout(() => {
			this.view = '';
		}, 5000);
	}
}
