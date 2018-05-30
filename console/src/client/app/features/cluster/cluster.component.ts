import { Component } from '@angular/core';
import { Cluster } from '../../classes/cluster';
import { ClusterService } from '../../services/cluster/cluster.service';

/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-cluster',
  templateUrl: 'cluster.component.html',
  styleUrls: ['cluster.component.css'],
})
export class ClusterComponent {
  private clusters_db: Cluster[];  
  private view: string = 'panel'
  private clusterbox: string;
  private cluster_view: Cluster;
  
  constructor(private clusters: ClusterService) {
      this.fetch();
  }

  public fetch() {
      this.clusters.getClusters().subscribe(data => {
          this.clusters_db = data;
      })
  }

  public create() {
      this.clusterbox = 'create';
  }

  public edit(cluster: Cluster) {
      this.clusterbox = 'edit';
      this.cluster_view = cluster;
  }

    public trash(cluster: Cluster) {
        this.clusters_db.splice(this.clusters_db.indexOf(cluster),1);
        this.clusters.updateClusters(this.clusters_db).subscribe(data => {
            this.clusterbox = null;
        });
    }

    public newcluster(ip: string) {
        if(ip != "") {
            let clu = new Cluster();
            clu.ip = ip;
            this.clusters_db.push(clu);
            this.clusters.updateClusters(this.clusters_db).subscribe(data => {
                this.clusterbox = null;
            });
        } else {
            alert("You must complete clustername ip");
        }
  }
  public editcluster(ip: string) {
        if(ip != "") {
            let index = this.clusters_db.indexOf(this.cluster_view);
            this.clusters_db[index].ip = ip;
            this.clusters.updateClusters(this.clusters_db).subscribe(data => {
                this.clusterbox = null;
            });
        } else {
            alert("You must complete clustername ip");
        }
  }
}
