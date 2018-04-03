package earlgrey.def;

import java.lang.reflect.Field;

public class RelationDef {
	public Field field;
	public String field_name_to;
	public Class model;
	public RelationDef(Field field, Class model){
		this.field = field;
		this.model = model;
	}
	public RelationDef(Field field, String field_name, Class model){
		this.field_name_to = field_name;
		this.field = field;
		this.model = model;
	}
	public RelationDef(String field, Class model){
		this.field_name_to = field;
		this.model = model;
	}
}
