import { Component, Output, EventEmitter } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Config } from '../../shared/config/env.config';
/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-navbar',
  templateUrl: 'navbar.component.html',
  styleUrls: ['navbar.component.css'],
})
export class NavbarComponent {
  private api:string;
	private menu:boolean = false;
	@Output() sidebar = new EventEmitter();
  	constructor(private sanitizer: DomSanitizer) {
      this.api = window.location.pathname;
			this.api = this.api.substring(0,this.api.indexOf("console/"))
			if(Config.ENV == 'DEV'){
				this.api = window.location.protocol+"//"+window.location.host+"/"+this.api;
			} else {
				this.api = window.location.protocol+"//"+window.location.host+"/"+this.api+"console/";
			}
  	}
  	private togleMenu():void{
  		this.menu = !this.menu;
  	}
  	private togleSidebar():void{
  		this.sidebar.emit();
		}
		private getLogo(){
			return this.sanitizer.bypassSecurityTrustStyle("url("+this.api+'assets/images/logo.jpg'+")");
		}
}