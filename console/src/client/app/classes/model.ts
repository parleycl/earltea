import { Field } from './field';
/*
* This class represents the class definition.
*/
export class Model {
    public name: string;
    public class_name: string;
    public table_name: string;
    public version: number;
    public fields: Field[];
    public multi_tenant: boolean;

    constructor(model: any = null) {
        if(model) {
            this.name = model.name;
            this.class_name = model.class_name;
            this.table_name = model.table_name;
            this.version = model.version;
            this.multi_tenant = (model.multi_tenant) ? model.multi_tenant : false;
            this.fields = [];
            if(model.fields) {
                for(let i=0; i<model.fields.length; i++) {
                    this.fields.push(new Field(model.fields[i]));
                }
            }
        }
    }
    
}