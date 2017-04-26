package geos.error;

import geos.annotations.ErrorCode;

public class Error700 extends ErrorBase{
	@ErrorCode(description="El aplicativo no tiene permisos para escribir en el disco.", code=701)
	public static int FILESYSTEM_WRITE_ERROR = 701;
	@ErrorCode(description="Ha existido un error al escribir el archivo. Verifique los permisos.", code=701)
	public static int FILE_SAVE_ERROR = 702;
	@ErrorCode(description="Error al leer el archivo del sistema de archivo.", code=703)
	public static int FILE_READ_ERROR = 703;
	@ErrorCode(description="Error especificado esta vacio.", code=704)
	public static int FILE_EMPTY_ERROR = 704;
	@ErrorCode(description="Error especificado tiene su contenido dañado.", code=705)
	public static int FILE_DAMAGE_ERROR = 705;
}
