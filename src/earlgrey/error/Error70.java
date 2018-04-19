package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error70 extends ErrorBase{
	@ErrorCode(description="The field defined in the model have a overwrite between ModelField and ModelRelation.", code=61)
	public static int FIELD_OVERLAYED_DEFINITION = 71;
}
