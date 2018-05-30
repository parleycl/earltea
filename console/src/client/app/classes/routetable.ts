import { Action } from './action';
/*
* This class represents the class definition.
*/
export class Routetable {
    
    public httpmethod: string;
    public route: string;
    public controller: string;
    public method: string;
    public model: string;
    public blueprint: boolean;
    public action: Action;
    
    constructor(httpmethod: string, route: string, controller: string, method: string, model: string, blueprint: boolean, action: Action) {
        this.httpmethod = httpmethod;
        this.route = route;
        this.controller = controller;
        this.method = method;
        this.model = model;
        this.blueprint = blueprint;
        this.action = action;
    }
}
