package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error70 extends ErrorBase{
	@ErrorCode(description="The field defined in the model have a overwrite between ModelField and ModelRelation.", code=71)
	public static int FIELD_OVERLAYED_DEFINITION = 71;
	@ErrorCode(description="The PrimaryKey must be an Integer type value", code=72)
	public static int PRIMARY_KEY_NOT_INTEGER = 72;
}
