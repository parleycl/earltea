package earlgrey.model;

import earlgrey.annotations.Model;
import earlgrey.annotations.ModelField;
import earlgrey.core.ModelCore;
import earlgrey.types.CENTROID;
import earlgrey.types.GEOM;

@Model(name = "Cartografia_regional", tableName = "SPATIAL_DATA_REGIONAL", version = 1)
public class CartografiaRegional extends ModelCore{
	@ModelField
	public String REGION;
	@ModelField
	public String C_REG;
	@ModelField
	public GEOM GEOMREGIONAL;
	@ModelField
	public CENTROID CENTROIDE;
} 
