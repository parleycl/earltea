import { Template } from "./template";
import { Propertie } from "./propertie";

/*
* This class represents the class definition.
*/
export class Properties {
    public STATIC: Propertie;
    public TEMPLATES: Template;
    constructor(propertie: any = null) {
        if(propertie) {
            this.STATIC = propertie.STATIC;
            this.TEMPLATES = propertie.TEMPLATES;
        }
    }
    
}
