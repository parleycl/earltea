import { Component, AfterViewChecked, ElementRef, ViewChild, OnInit} from '@angular/core';
import { RestService } from '../../services/rest/rest.service';

/**
 * This class represents the main application component.
 */
@Component({
  moduleId: module.id,
  selector: 'sd-logs',
  templateUrl: 'logs.component.html',
  styleUrls: ['logs.component.css'],
})
export class LogsComponent implements OnInit, AfterViewChecked {
	private list:string[];
	private puntero:number;
	private filename:string;
	private file:any[];
	private timer:any;
	@ViewChild('logcontent') private myScrollContainer: ElementRef;
  	constructor(private rest:RestService) {
    	this.fetchHistory();
    	this.file = [];
  	}
  	ngOnInit() { 
        this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    }

    ngAfterViewChecked() {
    	if(this.puntero != this.file.length) this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;        
    }
  	private fetchHistory(){
  		this.rest.get('logs/gethistory').subscribe(
    		data => {
    			this.list = data.reverse();
    			this.call_file(this.list[0]);
    		},
    		error => console.log(error)
    	);
  	}
  	private call_file(file:string){
  		clearInterval(this.timer);
  		this.filename = file;
  		this.file = [];
  		this.getfile(file);
  	}
  	private getfile(file:string){
  		var self = this;
  		this.puntero = this.file.length;
  		this.rest.post({
  			from: this.puntero,
  			file: file
  		},'logs/getlog').subscribe(
    		data => {
    			for(let i=0; i<data.length; i++){
    				this.file.push(JSON.parse(data[i]));
    			}
    			this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    			var current = new Date();
    			var fecha = file.replace("logfile_","").split(".");
			  	if(parseInt(fecha[0]) == current.getDate() && parseInt(fecha[1]) == (current.getMonth()+1) && parseInt(fecha[2]) == current.getFullYear()){
			  		self.timer = setInterval(() => {
	    				self.update_file(file);
	    			}, 2000);
			  	}
    		},
    		error => console.log(error)
    	);
  	}
  	private update_file(file:string){
  		this.puntero = this.file.length;
  		this.rest.postSilent({
  			from: this.puntero,
  			file: file
  		},'logs/getlog').subscribe(
    		data => {
    			if(data.length > 0){
    				for(let i=0; i<data.length; i++){
	    				this.file.push(JSON.parse(data[i]));
	    			}
	    			this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
    			}
    		},
    		error => console.log(error)
    	);
  	}
  	private break_spaces(value:string){
			if (value) {
				return value.replace(/\s/g, "&nbsp;");
			} else {
				return value;
			}
  	}
}