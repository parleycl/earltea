import { Component } from '@angular/core';
import { ControllersService } from '../../services/controllers/controllers.service';
import { Controller } from '../../classes/controller';

/**
 * This class represents the main application component.
 */
@Component({
    moduleId: module.id,
    selector: 'sd-controladores',
    templateUrl: 'controladores.component.html',
    styleUrls: ['controladores.component.css'],
})
export class ControladoresComponent {
    private controllers_db: Controller[];  
    private view: string = 'panel'
    private viewDetail: boolean = false;
    private controller_view: Controller;
    
    constructor(private controllers: ControllersService) {
        this.fetch();
    }

    public fetch() {
        this.controllers.getControllers().subscribe(data => {
            this.controllers_db = data;
        })
    }

    public showDetails(controller: Controller) {
        this.viewDetail = true;
        this.controller_view = controller;
    }
}
