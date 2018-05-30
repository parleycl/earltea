/*
* This class represents the class definition.
*/
export class User {
    public id: number;
    public Name: string;
    public Admin: boolean;
    
    constructor(user: any = null) {
        if(user) {
            this.Name = user.Name;
            this.id = user.id;
            this.Admin = (user.Admin) ? user.Admin : false;
        }
    }
}
