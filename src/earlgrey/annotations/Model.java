package earlgrey.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE )
@Inherited
public @interface Model {
	String name();
	String tableName();
	boolean REST() default false;
	boolean migrate() default true;
	String datasource() default "Default";
	float version();
}
