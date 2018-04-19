/*
* This class represents the class definition.
*/
export class Admin {
    public USERNAME: string;
    public PASSWORD: string;
    constructor(admin: any = null) {
        if(admin) {
            this.PASSWORD = admin.PASSWORD;
            this.USERNAME = admin.USERNAME;
        }
    }
    
}
