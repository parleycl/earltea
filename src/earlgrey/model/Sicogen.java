package earlgrey.model;

import earlgrey.annotations.Model;
import earlgrey.annotations.ModelField;
import earlgrey.core.ModelCore;

@Model(name = "Sicogen", tableName = "SEMAFORO_SICOGEN", version = 1)
public class Sicogen extends ModelCore{
	@ModelField
	public String ENT_NOMBRE;
	@ModelField
	public String CODIGO;
	@ModelField
	public String FECHA;
	@ModelField
	public Integer ESTADO;
	@ModelField
	public String URL;
	@ModelField
	public String C_REG_SUBDERE;
} 
