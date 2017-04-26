package geos.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControllerAction {
	String name();
	float version();
	String description();
	boolean POST() default true;
	boolean GET() default true;
	boolean PUT() default false;
	boolean DELETE() default false;
}
