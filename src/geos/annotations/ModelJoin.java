package geos.annotations;
import java.lang.annotation.Repeatable;

@Repeatable(ModelJoins.class)
public @interface ModelJoin {
	String table();
	String field();
	String relation();
}
