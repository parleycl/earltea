package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error60 extends ErrorBase{
	@ErrorCode(description="The propertie selected don't exists.", code=61)
	public static int PROPERTIE_NOT_SET = 61;
	@ErrorCode(description="The propertie selected is not same to type required.", code=62)
	public static int PROPERTIE_TYPE_INCORRECT = 62;
	@ErrorCode(description="The propertie object the type key, annotation and key are required.", code=63)
	public static int PROPERTIE_OBJECT_INCORRECT = 63;
	@ErrorCode(description="the properties templates sets are not configured in the properties file.", code=64)
	public static int PROPERTIE_SET_TEMPLATE_ERROR = 64;
	@ErrorCode(description="The propertie template dont exist.", code=65)
	public static int PROPERTIE_SET_TEMPLATE_NOTFOUND = 65;
}
