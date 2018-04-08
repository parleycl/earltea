import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { Http, Response, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import { Router } from '@angular/router';
import { RestService } from '../rest/rest.service';
import { Observable } from 'rxjs/Observable';
import { User } from '../../classes/user';

@Injectable()
export class UserService {
	private user: User;
    constructor(@Inject(RestService) private rest: RestService, private router: Router){
        
    }
    public isLoggedReady() {
		return this.restore(true);
	}
	private restore(fallback:boolean): Promise<boolean> {
        return new Promise((resolve, reject) => {
			//TODO
            this.rest.getSilent('admins/status').subscribe(data => {
				this.user = new User(data);
				resolve(true);
            }, err => {
				if(fallback) this.router.navigate(['/login']);
				resolve(false);
			});
        });
	}
	public login(username: string, password: string):Observable<boolean>{
		return new Observable<boolean>(observe => {
			this.rest.post({
				username: username,
				password: password
			}, 'admins/login').subscribe(data => {
				this.user = new User(data);
				observe.next(true);
				observe.complete();
			}, error => {
				observe.next(false);
				observe.complete();
			});
		});
	}
	public logout():Observable<boolean>{
		return new Observable<boolean>(observe => {
			this.rest.get('admins/logout').subscribe(data => {
				observe.next(true);
				observe.complete();
			}, error => {
				observe.next(false);
				observe.complete();
			});
		});
	}
}
