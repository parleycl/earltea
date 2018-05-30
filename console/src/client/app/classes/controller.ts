/*
* This class represents the class definition.
*/
export class Controller {
    
    public name: string;
    public description: string;
    public class_name: string;
    public version: number;
    
    constructor(controller: any = null) {
        if(controller) {
            this.name = controller.name;
            this.description = controller.description;
            this.class_name = controller.class_name;
            this.version = controller.version;
        }
    }
    
}
