import { Component } from '@angular/core';
import { ModelsService } from '../../services/models/models.service';
import { Model } from '../../classes/model';
/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-modelos',
  templateUrl: 'modelos.component.html',
  styleUrls: ['modelos.component.css'],
})
export class ModelosComponent {
    private models_db: Model[];  
    private view: string = 'panel'
    private viewDetail: boolean = false;
    private model_view: Model;
    
    constructor(private models: ModelsService) {
        this.fetch();
    }

    public fetch() {
        this.models.getModels().subscribe(data => {
            this.models_db = data;
        })
    }

    public showDetails(model: Model) {
        this.viewDetail = true;
        this.model_view = model;
    }
}
