import { Injectable, Inject } from '@angular/core';
import { Config } from '../../shared/config/env.config';
import { Cluster } from '../../classes/cluster';
import { Observable } from 'rxjs/Observable';
import { RestService } from '../rest/rest.service';
/**
* This class represents the service component.
*/
@Injectable()
export class ClusterService {
    private clusters: Cluster[];
    
    constructor(@Inject(RestService) private rest: RestService) { }
    
    public getClusters():Observable<Cluster[]> {
        return new Observable<Cluster[]>(observe => {
            if(this.clusters != null) {
                observe.next(this.clusters);
                observe.complete();
            } else {
                this.clusters = [];
                this.rest.get("clusters").subscribe(data => {
                    for(let i=0; i<data.length; i++) {
                        this.clusters.push(new Cluster(data[i]));
                    }
                    observe.next(this.clusters);
                    observe.complete();
                });
            }
        });
    }
    public updateClusters(clusters: Cluster[]):Observable<Cluster[]> {
        return new Observable<Cluster[]>(observe => {
            this.rest.patch({
                cluster: JSON.stringify(clusters)
            },"clusters").subscribe(data => {
                this.clusters = [];
                for(let i=0; i<clusters.length; i++) {
                    this.clusters.push(new Cluster(clusters[i]));
                }
                observe.next(this.clusters);
                observe.complete();
            });
        });
    }
    
}
