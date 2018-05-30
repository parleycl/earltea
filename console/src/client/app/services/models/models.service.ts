import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { RestService } from '../rest/rest.service';
import { Observable } from 'rxjs/Observable';
import { Model } from '../../classes/model';
/**
* This class represents the service component.
*/
@Injectable()
export class ModelsService {
    private models: Model[];
    
    constructor(@Inject(RestService) private rest: RestService) { }
    
    public getModels():Observable<Model[]> {
        return new Observable<Model[]>(observe => {
            if(this.models != null) {
                observe.next(this.models);
                observe.complete();
            } else {
                this.models = [];
                this.rest.get("models").subscribe(data => {
                    for(let i=0; i<data.length; i++) {
                        this.models.push(new Model(data[i]));
                    }
                    observe.next(this.models);
                    observe.complete();
                });
            }
        });
    }
    
}
