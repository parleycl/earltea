package earlgrey.annotations;

public @interface ModelTransaction {
	Class<?> model();
	String[] fields();
}
