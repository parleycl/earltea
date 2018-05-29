import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { RestService } from '../rest/rest.service';
import { Observable } from 'rxjs/Observable';
import { Route } from '../../classes/route';
/**
* This class represents the service component.
*/
@Injectable()
export class RoutesService {
    private routes: Route[];

    constructor(@Inject(RestService) private rest: RestService) { }
    
    public getRoutes():Observable<Route[]> {
        return new Observable<Route[]>(observe => {
            if(this.routes != null) {
                observe.next(this.routes);
                observe.complete();
            } else {
                this.routes = [];
                this.rest.get("routes").subscribe(data => {
                    for(let i=0; i<data.length; i++) {
                        this.routes.push(data[i]);
                    }
                    observe.next(this.routes);
                    observe.complete();
                });
            }
        });
    }
    
}
