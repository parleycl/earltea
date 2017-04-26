package geos.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE )
public @interface Model {
	String name();
	String tableName();
	boolean REST() default false;
	boolean migrate() default true;
	float version();
}
