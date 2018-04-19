import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { RestService } from '../rest/rest.service';
import { Observable } from 'rxjs/Observable';
import { Datasource } from '../../classes/datasource';
/**
* This class represents the service component.
*/
@Injectable()
export class DatasourcesService {

    constructor(@Inject(RestService) private rest: RestService ) { }
    
    public modifyDatasource(datasource: Datasource, enviroment: string): Observable<boolean> {
        return new Observable(observe => {
            this.rest.patch({
                datasource: JSON.stringify(datasource),
                name: datasource.NAME,
                enviroment: enviroment
            }, "datasources").subscribe(data=> {
                observe.next(true);
                observe.complete();
            }, error => {
                observe.next(false);
                observe.complete();
            })
        })
    }
}
