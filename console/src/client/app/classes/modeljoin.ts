import { Model } from './model';
/*
* This class represents the class definition.
*/
export class Modeljoin {
    public field: string;
    public model: Model;
    constructor(join: any = null) {
        if(join) {
            this.model = new Model(join.model);
            this.field = join.field;
        }
    }
    
}
