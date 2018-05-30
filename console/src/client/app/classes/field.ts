import { Modeljoin } from './modeljoin';
import { Model } from './model';
/*
* This class represents the class definition.
*/
export class Field {
    public name: string;
    public type: string;
    public primary: boolean;
    public unique: boolean;
    public autoincrement: boolean;
    public defaultValue: boolean;
    public model_join: Modeljoin;
    public model_relation: Model;
    
    constructor(field: any = null) {
        if(field) {
            this.name = field.name;
            this.type = field.type;
            this.primary = field.primary;
            this.unique = field.unique;
            this.autoincrement = field.autoincrement;
            this.defaultValue = field.defaultValue;
            this.model_join = (field.model_join) ? new Modeljoin(field.model_join) : null;
            this.model_relation = (field.model_relation) ? new Model(field.model_relation) : null;
        }
    }
}
