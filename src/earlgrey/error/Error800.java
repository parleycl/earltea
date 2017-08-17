package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error800 extends ErrorBase{
	@ErrorCode(description="No se pudo cargar dinamicamente el driver de base de datos.", code=801)
	public static int DATABASE_DRIVER_ERROR = 801;
	@ErrorCode(description="No se pudo establecer conexion con la base de datos.", code=802)
	public static int DATABASE_CONNECT_ERROR = 802;
	@ErrorCode(description="La libreria JDBC Oracle no esta disponible.", code=803)
	public static int DATABASE_ORACLE_JDBC = 803;
	@ErrorCode(description="No existe un driver de base datos disponible para efectuar una conexion a base de datos persistente.", code=804)
	public static int DATABASE_NO_DRIVER = 804;
}
