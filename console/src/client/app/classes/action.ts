import { Method } from './method';
import { Controller } from './controller';
import { Model } from './model';
/*
* This class represents the class definition.
*/
export class Action {
    public action: Method;
    public method: string;
    public controller: Controller;
    public blueprint: boolean;
    public model: Model;
    
    constructor(action: any = null) {
        if(action) {
            this.method = action.method;
            this.action = new Method(action.action);
            this.controller = (action.controller) ? new Controller(action.controller) : null;
            this.blueprint = action.blueprint;
            this.model = (action.model) ? new Model(action.model) : null;
        }
    }
    
}
