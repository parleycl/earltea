import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { Http, Response, Headers } from '@angular/http';
import { Router } from '@angular/router';
import { LoadingService } from '../loading/loading.service';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class RestService {
	http:Http;
	private api:string;
	// private blackscreen:any;
	//construimos los metodos
	constructor(http: Http, private router: Router, @Inject(LoadingService) private loading:LoadingService) {
		this.http = http;
		this.api = window.location.pathname;
		this.api = this.api.substring(0,this.api.indexOf("console/"))
		if(Config.ENV == 'DEV'){
			this.api = Config.API+this.api;
		} else {
			this.api = window.location.protocol+"//"+window.location.host+this.api;
		}		
	}
	public post(data:any, uri:string) {
		let body:string = '';
		this.loading.show(this);
		for(let key in data){
			if(data[key] == 0 || (data[key] != null && data[key] != '')){
				if(typeof data[key] == 'object') data[key] = JSON.stringify(data[key]);
				body += key+`=${data[key]}&`;
			}
		}
		body = body.substring(0,(body.length-1));
		let header = this.createHeaders();
		return this.http.post(this.api+"admin/console/"+uri, body, {
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			this.loading.hide(this);
			return response;
		}).catch(
			res => Observable.throw(this.loading.hide(this))
		);
	}

	public patch(data:any, uri:string) {
		let body:string = '';
		this.loading.show(this);
		for(let key in data){
			if(data[key] == 0 || (data[key] != null && data[key] != '')){
				if(typeof data[key] == 'object') data[key] = JSON.stringify(data[key]);
				body += key+`=${data[key]}&`;
			}
		}
		body = body.substring(0,(body.length-1));
		let header = this.createHeaders();
		return this.http.patch(this.api+"admin/console/"+uri, body, {
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			this.loading.hide(this);
			return response;
		}).catch(
			res => Observable.throw(this.loading.hide(this))
		);
	}

	public postSilent(data:any, uri:string) {
		let body:string = '';
		for(let key in data){
			if(data[key] == 0 || (data[key] != null && data[key] != '')){
				if(typeof data[key] == 'object') data[key] = JSON.stringify(data[key]);
				body += key+`=${data[key]}&`;
			}
		}
		body = body.substring(0,(body.length-1));
		let header = this.createHeaders();
		return this.http.post(this.api+"admin/console/"+uri, body, {
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			return response;
		});
	}
	public postSpecial(data:any, uri:string, header:any) {
		return this.http.post(this.api+"admin/console/"+uri, data, {
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			this.loading.hide(this);
			return response;
		});
	}
	public postMap(data:any, uri:string) {
		let body:string = '';
		for(let key in data){
			if(data[key] == 0 || (data[key] != null && data[key] != '')){
				if(typeof data[key] == 'object') data[key] = JSON.stringify(data[key]);
				body += key+`=${data[key]}&`;
			}
		}
		body = body.substring(0,(body.length-1));
		let header = this.createHeaders();
		return this.http.post(this.api+uri, body, {
	      	headers: header,
	      	withCredentials: true
	    });
	}
	// FOR JWT IMPLEMENTATION (FUTURE)
	public get(uri:string) {
		let header = this.createHeaders();
		this.loading.show(this);
		return this.http.get(this.api+"admin/console/"+uri, {
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			this.loading.hide(this);
			return response;
		});
	}
	public getSilent(uri:string) {
		let header = this.createHeaders();
		return this.http.get(this.api+"admin/console/"+uri, {
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			return response;
		});
	}
	public getMap(uri:string) {
		let header = this.createHeaders();
		return this.http.get(this.api+"admin/console/"+uri, {
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			return response;
		});
	}
	public delete(id:number,uri:string) {
		let header = this.createHeaders();
		if(id === null) {
			this.loading.show(this);
			return this.http.delete(this.api+"admin/console/"+uri, {
				headers: header,
				withCredentials: true
			}).map((res:Response) => {
				let response = res.json();
				this.loading.hide(this);
				return response;
			});
		} else {
			var r = confirm('¿Estas seguro que deseas borrar el elemento?');
			if (r == true) {
				this.loading.show(this);
				return this.http.delete(this.api+"admin/console/"+uri+'/'+id, {
					headers: header,
					withCredentials: true
				}).map((res:Response) => {
					let response = res.json();
					this.loading.hide(this);
					return response;
				});
			}
			return new Observable(observe => {
				observe.complete();
			});
		}
	}
	public put(id:number,uri:string, data:any) {
		this.loading.show(this);
		let body:string = '';
		for(let key in data){
			if(data[key] == 0 || (data[key] != null && data[key] != '')){
				if(typeof data[key] == 'object') data[key] = JSON.stringify(data[key]);
				body += key+`=${data[key]}&`;
			}
		}
		body = body.substring(0,(body.length-1));
		let header = this.createHeaders();
		return this.http.put(this.api+"admin/console/"+uri+'/'+id, body,{
	      	headers: header,
	      	withCredentials: true
	    }).map((res:Response) => {
			let response = res.json();
			this.loading.hide(this);
			return response;
		});
	}
	private createHeaders() {
		var headers = new Headers();
	    headers.append('Content-Type', 'application/x-www-form-urlencoded');
		return headers;
	}
}
