package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error60 extends ErrorBase{
	@ErrorCode(description="La propertie especificada no existe.", code=61)
	public static int PROPERTIE_NOT_SET = 61;
	@ErrorCode(description="La propertie especificada no correponde al tipo requerido.", code=62)
	public static int PROPERTIE_TYPE_INCORRECT = 62;
	@ErrorCode(description="El object propertie no posee la llave type, annotation y key requeridos.", code=63)
	public static int PROPERTIE_OBJECT_INCORRECT = 63;
	@ErrorCode(description="Las properties templates set no estan configurados en el archivo de properties.", code=64)
	public static int PROPERTIE_SET_TEMPLATE_ERROR = 64;
	@ErrorCode(description="Las propertie template no existe.", code=65)
	public static int PROPERTIE_SET_TEMPLATE_NOTFOUND = 65;
}
