package geos.error;

import geos.annotations.ErrorCode;

public class Error500 extends ErrorBase{
	@ErrorCode(description="Ha existido un error al renderizar la respuesta http en la clase response.", code=501)
	public static int RENDER_ERROR = 501;
	@ErrorCode(description="Ha existido un error interno al cargar el nucleo del sistema.", code=502)
	public static int LOAD_ERROR = 502;
	@ErrorCode(description="No se ha cargado ningun entorno de propiedades, por favor corrijalo desde la consola.", code=503)
	public static int PROPERTIES_ENV = 503;
	@ErrorCode(description="La policie especificada no existe declarada como una clase valida, verifique que la policie sea correcta y herede la clase PolicieCore", code=504)
	public static int POLICIE_NOT_LOADED = 504;
	@ErrorCode(description="El mapeador de recursos ha tenido problemas al localizar una de las clases declaradas.", code=505)
	public static int CLASS_LOAD_ERROR = 505;
	@ErrorCode(description="El recurso requiere una ruta, la cual no ha sido declarada.", code=506)
	public static int ROUTE_NOT_DECLARED = 506;
	@ErrorCode(description="Ha existido un error al clonar el objeto route.", code=507)
	public static int ROUTE_OBJECT_ERROR_CLONE = 507;
	@ErrorCode(description="Ha existido al renderizar la respuesta web.", code=508)
	public static int HTTP_RENDER_ERROR = 508;
	@ErrorCode(description="El metodo no se puede invocar con los elementos standard, asegurese que esta bien declarado como metodo publico estatico y recibiendo como parametro request y response.", code=509)
	public static int METHOD_INVOCATION_ERROR = 509;
}
