package earlgrey.def;

import earlgrey.error.ErrorBase;

public class ErrorDef {
	// DECLARAMOS LAS PROPIEDADES
	public Class<?> clase;
	public int code;
	public String description;
	// DECLARAMOS EL CONSTRUCTOR
	public ErrorDef(Class<?> clase, int code, String description) {
		super();
		this.clase = clase;
		this.code = code;
		this.description = description;
	}
}
