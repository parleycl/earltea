import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { Http, Response, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import { Router } from '@angular/router';
import { RestService } from '../rest/rest.service';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class UserService {
    constructor(@Inject(RestService) private rest: RestService){
        
    }
    public isLoggedReady() {
		return this.restore(true);
	}
	private restore(fallback:boolean) {
        return new Promise((resolve, reject) => {
            //TODO
            this.rest.get('admins/status').subscribe(data => {
                console.log(data);
                resolve(true);
            });
        });
		// .map((res:Response) => {
		// 	let response = res.json();
		// 	if(response.RESPONSE === 200) {
		// 		this.user = response.USER;
		// 		this.status = true;
		// 		this.callUserCallback();
		// 		return true;
		// 	}
		// 	if(fallback) this.router.navigate(['/login']);
		// 	return false;
		// });
	}
}
