package earlgrey.model;

import earlgrey.annotations.Model;
import earlgrey.annotations.ModelField;
import earlgrey.core.ModelCore;

@Model(name = "Auditoria_comunal", tableName = "INFORMES_AUDITORIA_COMUNAL", version = 1)
public class AuditoriaComunal extends ModelCore{
	@ModelField
	public String C_REG_SUBDERE;
	@ModelField
	public String CODIGOSUBDERE;
	@ModelField
	public Integer TOTAL;
} 
