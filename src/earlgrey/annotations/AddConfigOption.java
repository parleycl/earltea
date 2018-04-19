package earlgrey.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GroupConfigOption.class)
public @interface AddConfigOption {
	String name();
	String earlgrey_name();
	String[] option();
	int defaultTo() default 0;
}
