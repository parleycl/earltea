import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { LinePipe } from './line.pipe';
import { LogFilePipe } from './logfile.pipe';
import { KeysPipe } from './keys.pipe';
/**
 * Do not specify providers for modules that might be imported by a lazy loaded module.
 */

@NgModule({
  imports: [],
  declarations: [LinePipe,LogFilePipe,KeysPipe],
  providers: [],
  exports: [LinePipe,LogFilePipe,KeysPipe]
})
export class PipesModule {
}
