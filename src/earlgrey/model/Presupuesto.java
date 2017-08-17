package earlgrey.model;

import earlgrey.annotations.Model;
import earlgrey.annotations.ModelField;
import earlgrey.core.ModelCore;

@Model(name = "Presupuesto", tableName = "PRESUPUESTO", version = 1)
public class Presupuesto extends ModelCore{
	@ModelField
	public String REGION;
	@ModelField
	public String MUNICIPALIDAD;
	@ModelField
	public String AREA;
	@ModelField
	public String PRESUPUESTO;
	@ModelField
	public String EJECUTADO;
	@ModelField
	public String PORCENTAJE;
	@ModelField
	public String C_COMUNA_SUBDERE;
} 
