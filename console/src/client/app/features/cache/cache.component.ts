import { Component } from '@angular/core';
import { CacheService } from '../../services/cache/cache.service';
/**
* This class represents the main application component.
*/
@Component({
    moduleId: module.id,
    selector: 'sd-cache',
    templateUrl: 'cache.component.html',
    styleUrls: ['cache.component.css'],
})
export class CacheComponent {
    constructor(private cache: CacheService) {}
    public cleanCache() {
        var r = confirm("Esta seguro que desea eliminar el cache existente en la aplicación.");
        if (r == true) {
           this.cache.cleanCache().subscribe(data => {
                alert("La limpieza de cache ha concluido con exito");
           });
        }
    }
}
