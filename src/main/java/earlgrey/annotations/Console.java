package earlgrey.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Console {
	String name();
	float version();
	String description();
	boolean POST() default true;
	boolean GET() default true;
	boolean PUT() default true;
	boolean DELETE() default false;
}
