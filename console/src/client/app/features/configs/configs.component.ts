import { Component } from '@angular/core';
import { RestService } from '../../services/rest/rest.service';
import { ConfigService } from '../../services/config/config.service';
/**
* This class represents the main application component.
*/
@Component({
    moduleId: module.id,
    selector: 'sd-configs',
    templateUrl: 'configs.component.html',
    styleUrls: ['configs.component.css'],
})
export class ConfigsComponent {
    private config: any;
    private prop_templates:any;
    private prop_static:any;

    constructor(private config_service:ConfigService) {
        this.fetch();
    }

    public fetch(){
        this.config_service.getConfigs().subscribe(data => {
            this.config = data;
            this.prop_static = data;
        });
    }
    public saveAll(event:any){
        var config: any = {};
        config['STATIC'] = {};
        for(let key in this.prop_static) {
            config.STATIC[this.prop_static[key].name] = this.prop_static[key];
        }
        config['TEMPLATES'] = {};
        for(let key in this.prop_templates) {
            config.TEMPLATES[this.prop_templates[key].name] = this.prop_templates[key]
        }
        this.config_service.updateConfigs(config).subscribe(data => {

        });
    }
}
