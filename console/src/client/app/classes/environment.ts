import { Propertie } from './propertie';
import { Template } from './template';
import { Datasource } from './datasource';
/*
* This class represents the class definition.
*/
export class Environment {
    public DATASOURCES: any;
    public EARLGREY_ENVNAME: string;
    public STATIC: Propertie;
    public TEMPLATES: Template;

    constructor(environment: any = null) {
        if(environment) {
            // check datasources
            if(typeof environment['DATASOURCES'] != "undefined") {
                this.DATASOURCES = {};
                for(let key in environment['DATASOURCES']){
                    this.DATASOURCES[key] = (new Datasource(key, environment['DATASOURCES'][key]));
                }
            }
            this.EARLGREY_ENVNAME = environment.EARLGREY_ENVNAME;
            this.STATIC = environment.STATIC;
            this.TEMPLATES = environment.TEMPLATES;
        }
    }
    
}
