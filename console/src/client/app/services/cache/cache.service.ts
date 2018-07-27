import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { RestService } from '../rest/rest.service';
import { Observable } from 'rxjs/Observable';
/**
* This class represents the service component.
*/
@Injectable()
export class CacheService {
    constructor(@Inject(RestService) private rest: RestService) {
        
    }
    public cleanCache(): Observable<boolean> {
        return new Observable<boolean>(observe => {
            this.rest.delete(null, 'cache').subscribe(data => {
                observe.next();
                observe.complete();
            })
        });
    }
}
