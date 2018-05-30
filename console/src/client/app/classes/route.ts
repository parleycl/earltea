import { Action } from './action';
/*
* This class represents the class definition.
*/
export class Route {
    public route: string;
    public actions: Action[];
    
    constructor(routes: any = null) {
        if(routes) {
            this.route = routes.route;
            this.route = this.route.replace(":earl_model_aaa_id_grey", ":id");
            this.actions = [];
            if(routes.actions && routes.actions.length > 0) {
                for(let i=0; i<routes.actions.length; i++) {
                    this.actions.push(new Action(routes.actions[i]));
                }
            }
        }
    }
    
}
