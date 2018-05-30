import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { Observable } from 'rxjs/Observable';
import { RestService } from '../rest/rest.service';
/**
* This class represents the service component.
*/
@Injectable()
export class ConfigService {
    private configs: any;
    
    constructor(@Inject(RestService) private rest: RestService) { }
    
    public getConfigs():Observable<any> {
        return new Observable<any>(observe => {
            if(this.configs != null) {
                observe.next(this.configs);
                observe.complete();
            } else {
                this.configs = [];
                this.rest.get("configs").subscribe(data => {
                    this.configs = data;
                    observe.next(this.configs);
                    observe.complete();
                });
            }
        });
    }

    public updateConfigs(configs: any):Observable<any> {
        return new Observable<any>(observe => {
            this.rest.patch({
                configs: JSON.stringify(configs)
            },"configs").subscribe(data => {
                this.configs = data;
                observe.next(this.configs);
                observe.complete();
            });
        });
    }
    
}
