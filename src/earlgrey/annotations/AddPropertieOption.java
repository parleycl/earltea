package earlgrey.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GroupPropertieOption.class)
public @interface AddPropertieOption {
	String name();
	String[] option();
	int defaultTo() default 0;
}
