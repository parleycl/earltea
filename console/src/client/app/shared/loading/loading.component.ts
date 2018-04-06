import { Component, Output, EventEmitter, Input} from '@angular/core';
import { LoadingService } from '../../services/loading/loading.service';
/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-loading',
  templateUrl: 'loading.component.html',
  styleUrls: ['loading.component.css'],
})
export class LoadingComponent {
  private showme:boolean;
    constructor(private loading:LoadingService) {
      this.loading.registerLoading(this);
	  }
    public show(){
        this.showme = true;
    }
    public hide(){
        this.showme = false;
    }
}
