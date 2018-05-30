/*
* This class represents the class definition.
*/
export class Method {
    
    public name: string;
    public description: string;
    public method_name: string;
    public version: number;
    
    constructor(method: any = null) {
        if(method) {
            this.name = method.name;
            this.description = method.description;
            this.method_name = method.method_name;
            this.version = method.version;
        }
    }
    
}
