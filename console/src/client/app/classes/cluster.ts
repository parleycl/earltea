/*
* This class represents the class definition.
*/
export class Cluster {
    public ip: string;
    public last: string;
    public status: number;
    
    constructor(cluster: any = null) {
        if(cluster) {
            this.ip = cluster.ip;
            this.last = cluster.last;
            this.status = cluster.status;
        }
    }
    
}
