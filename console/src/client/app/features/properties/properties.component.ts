import { Component, ChangeDetectorRef, ApplicationRef} from '@angular/core';
import { RestService } from '../../services/rest/rest.service';
/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-properties',
  templateUrl: 'properties.component.html',
  styleUrls: ['properties.component.css'],
})
export class PropertiesComponent {
  private list:any[];
  private selected:string;
  private restarting:boolean;
  private view:string = 'panel';
  private env_mod:any;
  private prop_static:any;
  private prop_dinamic:any;
  private prop_templates:any;
  private setprop:string;
  private timer:any;
    constructor(private rest:RestService,private cd: ChangeDetectorRef, private ref:ApplicationRef) {
      this.fetchEntornos();
      this.restarting = false;
      this.checkRestart();
    }
    private fetchEntornos(){
      this.rest.get('properties/getenv').subscribe(
        data => {
          this.list = data.ENV;
          this.selected = data.SELECTED;
        },
        error => console.log(error)
      );
    }
    private add_new(){
      this.view = 'create';
    }
    private newsubmit(event:any, name:string, form:any){
      event.preventDefault();
      var repeat:boolean = false;
      for(let env of this.list){
        if(env.EARLGREY_ENVNAME.toUpperCase() == name.toUpperCase()	) repeat = true;
      }
      if(!repeat){
        this.rest.post({
          name: name
        },'properties/createenv').subscribe(
          data => {
            this.list.push(data);
            this.view = 'panel';
            form.reset();
          },
          error => console.log(error)
        );
      }
      else{
        alert("Ya existe un entorno con este nombre. Intente Nuevamente");
      }
    }
    private deleteEnv(name:string){
      if(this.selected != name){
        var r = confirm("Esta seguro que desea borrar el entorno de properties "+name);
        if (r == true) {
          
        }
      }
      else{
        alert("No se puede eliminar un entorno de properties que esta activo.")
      }
    }
    private selectEnv(name:string){
      if(this.selected != name){
        var r = confirm("Esta seguro cambiar el aplicativo al entorno de properties "+name+". El sistema reiniciara los subsistemas que necesiten esten ligados al funcionamiento de las properties.");
        if (r == true) {
          this.rest.post({
            env: name
          },'properties/setenv').subscribe(
            data => {
              this.selected = name;
            },
            error => console.log(error)
          );
        }
      }
      else{
        alert("No se puede seleccionar un entorno de properties que esta activo.")
      }
    }
    private modEnv(env:any){
      this.env_mod = env;
      this.prop_static = [];
      this.prop_dinamic = [];
      this.prop_templates = [];
      for(let key in env.STATIC){
        this.prop_static.push(env.STATIC[key]);
      }
      for(let key in env.TEMPLATES){
        this.prop_templates.push(env.TEMPLATES[key]);
      }
      for(let key in env.DINAMIC){
        this.prop_dinamic.push(env.DINAMIC[key]);	
      }
      console.log(this.prop_templates);
      this.view = 'modify';
    }
    private changeset(set:string){
      if(this.setprop == set){
        this.setprop = null
      }
      else
      {
        this.setprop = set;
      }
    }
    private addNewSet(set:any){
      var index = this.prop_static.indexOf(set);
      this.prop_static[index].sets = this.prop_static[index].sets.concat([set.set]);
      this.ref.tick();
    }
    private setSetDefault(set:any, key:number, event:any){
      event.stopPropagation();
      var index = this.prop_static.indexOf(set);
      this.prop_static[index].value = key;
    }
    private saveAll(event:any){
      var index = this.list.indexOf(this.env_mod);
      this.list[index].STATIC = {};
      for(let key in this.prop_static){
        this.list[index].STATIC[this.prop_static[key].name] = this.prop_static[key];
      }
      this.list[index].DYNAMIC = {};
      for(let key in this.prop_dinamic){
        this.list[index].DYMANIC[this.prop_dinamic[key].name] = this.prop_dinamic[key]
      }
      this.list[index].TEMPLATES = {};
      for(let key in this.prop_templates){
        this.list[index].TEMPLATES[this.prop_templates[key].name] = this.prop_templates[key]
      }
      this.rest.patch({
        name: this.env_mod.EARLGREY_ENVNAME,
        prop: JSON.stringify(this.env_mod)
      },'properties/modprop').subscribe(
        data => {
          this.view = 'panel';
          if(this.env_mod.EARLGREY_ENVNAME == this.selected){
            this.rest.get('properties/restart').subscribe(
              data => {
              },
              error => console.log(error)
            );
          }
        },
        error => console.log(error)
      );
    }
    private changeOption(option:any,value:any){
      var index = this.prop_static.indexOf(option);
      this.prop_static[index].value = value.value;
    }
    private checkRestart(){
      var self = this;
      this.timer = setInterval(() => {
        this.rest.getSilent('properties/getrestart').subscribe(
          data => {
            self.restarting = data.STATUS;
          },
          error => console.log(error)
        );
        }, 500);
  }
  private changesetvalue(props:number, set:number, prop:string, value:any){
    this.prop_static[props].sets[set][prop] = value;
    if(this.prop_static[props].value == set){
      this.prop_static[props].set[prop] = value;
    }
  }
  private changesetvaluetemplate(props:number, set:number, prop:string, value:any){
    this.prop_templates[props].sets[set][prop] = value;
    if(this.prop_templates[props].value == set){
      this.prop_templates[props].set[prop] = value;
    }
  }
}
