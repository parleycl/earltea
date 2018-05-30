import { Component } from '@angular/core';
import { User } from '../../classes/user';
import { UserService } from '../../services/user/user.service';
/**
* This class represents the main application component.
*/
@Component({
    moduleId: module.id,
    selector: 'sd-users',
    templateUrl: 'users.component.html',
    styleUrls: ['users.component.css'],
})
export class UsersComponent {
    private users_db: User[];  
    private view: string = 'panel'
    private userbox: string;
    private user_view: User;
    
    constructor(private users: UserService) {
        this.fetch();
    }

    public fetch() {
        this.users.getUsers().subscribe(data => {
            this.users_db = data;
        })
    }

    public create() {
        this.userbox = 'create';
    }

    public edit(user: User) {
        this.userbox = 'edit';
        this.user_view = user;
    }

    public trash(user: User) {
        if(this.users_db.length > 1) {
            this.users.trashUser(user.id).subscribe(data => {
                this.userbox = null;
            });
        } else {
            alert("Cannot delete user when only have one in the list");
        }
    }

    public newuser(username: string, password: string) {
        if(username != "" && password != "") {
            this.users.newUser(username, password).subscribe(data => {
                this.userbox = null;
            });
        } else {
            alert("You must complete username and password");
        }
    }
    public edituser(username: string, password: string) {
        if(username != "" && password != "") {
            this.users.editUser(this.user_view.id, username, password).subscribe(data => {
                for(let i=0; i<this.users_db.length; i++) {
					if(this.users_db[i].id == this.user_view.id) {
						this.users_db[i].Name = username;
					}	
                }
                this.userbox = null;
            });
        } else {
            alert("You must complete username and password");
        }
    }
}
