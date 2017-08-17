package earlgrey.model;

import earlgrey.annotations.Model;
import earlgrey.annotations.ModelField;
import earlgrey.core.ModelCore;

@Model(name = "Sica", tableName = "INFORMES_AUDITORIA", version = 1)
public class Sica extends ModelCore{
	@ModelField
	public Integer IDINFORME;
	@ModelField
	public Integer NUMEROINFORME;
	@ModelField
	public Integer ANOINFORME;
	@ModelField
	public String FECHAINFORME;
	@ModelField
	public String TIPOINFORME;
	@ModelField
	public String NOMBRESERVICIO;
	@ModelField
	public String TIPOSUBDERE;
	@ModelField
	public String C_REG_SUBDERE;
	@ModelField
	public String CODIGOSUBDERE;
	@ModelField
	public String NOMBRESUBDERE;
	@ModelField
	public String MATERIAINFORME;
	@ModelField
	public String FICHAINFORME;
	@ModelField
	public String PDFINFORME;
} 
