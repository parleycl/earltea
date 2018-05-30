import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { RestService } from '../rest/rest.service';
import { Observable } from 'rxjs/Observable';
import { Controller } from '../../classes/controller';
/**
* This class represents the service component.
*/
@Injectable()
export class ControllersService {
    private controllers: Controller[];
    
    constructor(@Inject(RestService) private rest: RestService) { }
    
    public getControllers():Observable<Controller[]> {
        return new Observable<Controller[]>(observe => {
            if(this.controllers != null) {
                observe.next(this.controllers);
                observe.complete();
            } else {
                this.controllers = [];
                this.rest.get("controllers").subscribe(data => {
                    for(let i=0; i<data.length; i++) {
                        this.controllers.push(new Controller(data[i]));
                    }
                    observe.next(this.controllers);
                    observe.complete();
                });
            }
        });
    }
    
}
