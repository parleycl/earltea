import { Component } from '@angular/core';
/**
* This class represents the main application component.
*/
@Component({
	moduleId: module.id,
	selector: 'sd-index',
	templateUrl: 'index.component.html',
	styleUrls: ['index.component.css'],
})
export class IndexComponent {
	private sidebar:boolean = false;
	/* CONSTRUCTOR */
	constructor() {
	}
	private hideSiderbar(){
		this.sidebar = !this.sidebar;
	}
}