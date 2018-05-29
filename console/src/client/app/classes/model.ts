/*
* This class represents the class definition.
*/
export class Model {
    public name: string;
    public class_name: string;
    public table_name: string;
    public version: number;

    constructor(model: any = null) {
        if(model) {
            this.name = model.name;
            this.class_name = model.class_name;
            this.table_name = model.table_name;
            this.version = model.version;
        }
    }
    
}