import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { Observable } from 'rxjs/Observable';
import { RestService } from '../rest/rest.service';
import { ConfigProp } from '../../classes/configprop';
/**
* This class represents the service component.
*/
@Injectable()
export class PropertiesService {
    constructor(@Inject(RestService) private rest:RestService) {
    }
    public getConfigFile():Observable<any>{
        return new Observable(observe => {
            this.rest.get('properties').subscribe(data => {
                observe.next(data);
                observe.complete();
            });
        });
    }
    public saveConfigFile(config:ConfigProp) : Observable<boolean>{
        return new Observable(observe => {
            this.rest.post({
                file: JSON.stringify(config)
            },'properties').subscribe(data => {
                observe.next(true);
                observe.complete();
            }, error => {
                observe.next(false);
                observe.complete();
            });
        });
    }
}
