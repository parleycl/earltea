import { Route } from './route';
import { Model } from './model';
import { Console } from './console';
import { Environment } from './environment';
import { Cluster } from './cluster';
import { Policie } from './policie';
import { Controller } from './controller';
import { Properties } from './properties';
import { Templates } from './templates';

export class ConfigProp {
    public routes: Route[];
    public models: Model[];
    public console: Console;
    public environment: Environment[];
    public comunication: Cluster[];
    public policies: Policie[];
    public controllers: Controller[];
    public config: Properties;
    public conf_templates: Templates;
    public prop_templates: Templates;
    public env_used: string;

    private valid: boolean = true;

    constructor(config:any = null) {
        // check if routes exists
        if(typeof config['routes'] != undefined) {
            this.routes = [];
            config['routes'].forEach((element: any) => {
                this.routes.push(new Route(element));
            });
        } else {
            this.valid = false;
        } 

        //check the models
        if(typeof config['models'] != undefined) {
            this.models = [];
            config['models'].forEach((element: any) => {
                this.models.push(new Model(element));
            });
        } else {
            this.valid = false;
        } 

        //check the console
        if(typeof config['console'] != undefined) {
            this.console = new Console(config['console']);
        } else {
            this.valid = false;
        } 

        //check the console
        if(typeof config['environment'] != undefined) {
            this.environment = [];
            config['environment'].forEach((element: any) => {
                this.environment.push(new Environment(element));
            });
        } else {
            this.valid = false;
        } 

        //check the comunication
         if(typeof config['comunication'] != undefined) {
            this.comunication = [];
            config['comunication'].forEach((element: any) => {
                this.comunication.push(new Cluster(element));
            });
        } else {
            this.valid = false;
        } 

        //check the policies
        if(typeof config['policies'] != undefined) {
            this.policies = [];
            config['policies'].forEach((element: any) => {
                this.policies.push(new Policie(element));
            });
        } else {
            this.valid = false;
        } 

        //check the controllers
        if(typeof config['controllers'] != undefined) {
            this.controllers = [];
            config['controllers'].forEach((element: any) => {
                this.controllers.push(new Controller(element));
            });
        } else {
            this.valid = false;
        } 

        //Check the config
        if(typeof config['config'] != undefined) {
            this.config = new Properties(config['config']);
        } else {
            this.valid = false;
        } 

        //Check the conf_templates
        if(typeof config['conf_templates'] != undefined) {
            this.conf_templates = new Templates(config['conf_templates']);
        } else {
            this.valid = false;
        }

        //Check the conf_templates
        if(typeof config['prop_templates'] != undefined) {
            this.prop_templates = new Templates(config['prop_templates']);
        } else {
            this.valid = false;
        }

        //Check the env_name
        if(typeof config['env_used'] != undefined) {
            this.env_used = config['env_used'];
        } else {
            this.valid = false;
        }
    }

    public getValidity(){
        return this.valid;
    }
}