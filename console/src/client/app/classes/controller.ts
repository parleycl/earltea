import { Method } from './method';
/*
* This class represents the class definition.
*/
export class Controller {
    
    public name: string;
    public description: string;
    public class_name: string;
    public version: number;
    public actions: Method[];
    
    constructor(controller: any = null) {
        if(controller) {
            this.name = controller.name;
            this.description = controller.description;
            this.class_name = controller.class_name;
            this.version = controller.version;
            this.actions = [];
            if(controller.actions) {
                for(let i=0; i<controller.actions.length; i++) {
                    this.actions.push(new Method(controller.actions[i]));
                }
            }
        }
    }
    
}
