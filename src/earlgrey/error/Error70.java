package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error70 extends ErrorBase{
	@ErrorCode(description="Los campos definidos en el modelo tienen una sobreposicion entre ModelField y ModelRelation.", code=61)
	public static int FIELD_OVERLAYED_DEFINITION = 71;
}
