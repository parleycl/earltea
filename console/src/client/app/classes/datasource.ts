export class Datasource {

    public NAME: string;
    public TYPE: number = 0;
	public HOST: string;
	public USERNAME: string;
	public PASSWORD: string;
	public SOURCE: string;
	public PORT: number;
	public MAX_POOL: number;
	public ON_DEMAND: boolean;
	
	// CONTAINER DATASOURCE TYPE;
	
	public DATASOURCE: string;
	public CONTAINER_DATASOURCE: boolean = false;

    // CONSTRUCTOR
    constructor(name: string, datasource: any = null){
        this.NAME = name;
        if(datasource) {
            this.TYPE = (datasource.TYPE >= 0) ? datasource.TYPE : 0;
            this.HOST = datasource.HOST;
            this.USERNAME = datasource.USERNAME;
            this.PASSWORD = datasource.PASSWORD;
            this.SOURCE = datasource.SOURCE;
            this.PORT = datasource.PORT;
            this.MAX_POOL = datasource.MAX_POOL;
            this.ON_DEMAND = datasource.ON_DEMAND;
            this.DATASOURCE = datasource.DATASOURCE;
            this.CONTAINER_DATASOURCE = datasource.CONTAINER_DATASOURCE;
        }
    }

}