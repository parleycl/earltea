import { Component } from '@angular/core';
import { RoutesService } from '../../services/routes/routes.service';
import { Routetable } from '../../classes/routetable';
import { Action } from '../../classes/action';
import { Method } from '../../classes/method';
import { Route } from '../../classes/route';

/**
 * This class represents the main application component.
 */
@Component({
    moduleId: module.id,
    selector: 'sd-rutas',
    templateUrl: 'rutas.component.html',
    styleUrls: ['rutas.component.css'],
})
export class RutasComponent {
    private routes_db: Route[];  
    private routes_table: Routetable[];
    private view: string = 'panel'
    private viewDetail: boolean = false;
    
    constructor(private routes: RoutesService) {
        this.fetch();
    }

    public fetch() {
        this.routes.getRoutes().subscribe(data => {
            this.routes_db = [];
            for(let i=0; i<data.length; i++) {
                this.routes_db.push(new Route(data[i]));
            }
            this.routes_table = [];
            for(let i=0; i<this.routes_db.length; i++) {
                for(let l=0; l<this.routes_db[i].actions.length; l++) {
                    let act: Action = this.routes_db[i].actions[l];
                    this.routes_table.push(new Routetable(act.method, this.routes_db[i].route, (act.controller) ? act.controller.name : 'No controller', 
                        act.action.name, (act.model) ? act.model.name : null, act.blueprint, act));
                }
            }
        })
    }

    public showDetails(action: Action) {
        this.viewDetail = true;
    }
}
