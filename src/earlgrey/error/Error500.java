package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error500 extends ErrorBase{
	@ErrorCode(description="Earlgrey detect an error when try to render the response.", code=501)
	public static int RENDER_ERROR = 501;
	@ErrorCode(description="There was an internal error when Earlgrey loading the system core.", code=502)
	public static int LOAD_ERROR = 502;
	@ErrorCode(description="No property environment has been loaded, please fix it in the console.", code=503)
	public static int PROPERTIES_ENV = 503;
	@ErrorCode(description="The specify policie don't exist like a class, verify the policie are correct and extends of PolicieCore", code=504)
	public static int POLICIE_NOT_LOADED = 504;
	@ErrorCode(description="The resource mapping had a problem while localized a declared class.", code=505)
	public static int CLASS_LOAD_ERROR = 505;
	@ErrorCode(description="The resource require a route, Please declare a route.", code=506)
	public static int ROUTE_NOT_DECLARED = 506;
	@ErrorCode(description="There was an error while Earlgrey clone the route object.", code=507)
	public static int ROUTE_OBJECT_ERROR_CLONE = 507;
	@ErrorCode(description="Earlgrey detect an error when try to render the web response.", code=508)
	public static int HTTP_RENDER_ERROR = 508;
	@ErrorCode(description="The method can't invoke with the standard elements, please check is defined like a static method and use HttpRequest and HttpResponse parameters'.", code=509)
	public static int METHOD_INVOCATION_ERROR = 509;
	@ErrorCode(description="There was and error while Earlgrey process the filter chain.", code=510)
	public static int CHAIN_FILTER_ERROR = 510;
}
