/*
* This class represents the class definition.
*/
export class User {
    public Name: string;
    public Admin: boolean;
    
    constructor(user: any = null) {
        if(user) {
            this.Name = user.Name;
            this.Admin = user.Admin;
        }
    }
}
