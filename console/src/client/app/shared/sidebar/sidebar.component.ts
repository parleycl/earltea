import { Component, Input } from '@angular/core';

/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-sidebar',
  templateUrl: 'sidebar.component.html',
  styleUrls: ['sidebar.component.css'],
})
export class SidebarComponent {
	@Input() hidden:boolean;
  	constructor() {
    
  	}
}
