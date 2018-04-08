import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Config } from '../../shared/config/env.config';
import { UserService } from '../../services/user/user.service';
import { AlertService } from '../../shared/alerts/alert.service';
import { Router } from '@angular/router';
/**
* This class represents the main application component.
*/
@Component({
    moduleId: module.id,
    selector: 'sd-login',
    templateUrl: 'login.component.html',
    styleUrls: ['login.component.css'],
})
export class LoginComponent {
    private path: string;

    constructor(private sanitizer: DomSanitizer, private user: UserService, private alert: AlertService, private router: Router) {
        this.path = window.location.pathname;
        this.path = this.path.substring(0,this.path.indexOf("console/"))
        if(Config.ENV == 'DEV'){
            this.path = window.location.protocol+"//"+window.location.host+"/"+this.path;
        } else {
            this.path = window.location.protocol+"//"+window.location.host+"/"+this.path+"console/";
        }
    }

    private getLogo(){
        return this.sanitizer.bypassSecurityTrustStyle("url("+this.path+'assets/images/logo.jpg'+")");
    }

    private login(event:any, username: string, password: string) {
        event.preventDefault();
        this.user.login(username, password).subscribe(data => {
            if(!data){
                this.alert.warning("User or password incorrect.");
                return;
            }
            this.router.navigate(['/dashboard']);
        })
    }
}
