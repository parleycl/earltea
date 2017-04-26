package geos.model;

import geos.annotations.Model;
import geos.annotations.ModelField;
import geos.core.ModelCore;
import geos.types.CENTROID;
import geos.types.GEOM;

@Model(name = "Cartografia_comunal", tableName = "SPATIAL_DATA_COMUNAL", version = 1)
public class CartografiaComunal extends ModelCore{
	@ModelField
	public String CINE_COM;
	@ModelField
	public String COMUNA;
	@ModelField
	public String X_REGI;
	@ModelField
	public GEOM GEOMCOMUNAL;
	@ModelField
	public CENTROID CENTROIDE;
	
} 
