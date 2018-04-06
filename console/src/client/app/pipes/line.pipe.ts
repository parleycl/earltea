import { Pipe, PipeTransform } from '@angular/core';
@Pipe({name: 'line'})
export class LinePipe implements PipeTransform {
  transform(value:any, args:string[]) : any {
  	console.log(value);
    return value.replace(/\s/g, "&nbsp;");
  }
}
