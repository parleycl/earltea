import { Pipe, PipeTransform } from '@angular/core';
@Pipe({name: 'logfile'})
export class LogFilePipe implements PipeTransform {
  transform(value:any, args:string[]) : any {
  	if(value == "" || value == null) return "";
  	var fecha = value.replace("logfile_","").split(".");
  	var current = new Date();
  	if(parseInt(fecha[0]) == current.getDate() && parseInt(fecha[1]) == (current.getMonth()+1) && parseInt(fecha[2]) == current.getFullYear()){
  		return "Current log";
  	}
  	else{
  		if(fecha[0].length < 2) fecha[0] = "0"+fecha[0];
	  	if(fecha[1].length < 2) fecha[1] = "0"+fecha[1];
	    return "Log file: "+fecha[0]+"/"+fecha[1]+"/"+fecha[2];
  	}
  }
}
