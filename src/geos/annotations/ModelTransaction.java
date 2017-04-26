package geos.annotations;

public @interface ModelTransaction {
	Class<?> model();
	String[] fields();
}
