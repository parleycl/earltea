package earlgrey.def;

import java.lang.reflect.Field;

import earlgrey.core.ModelCore;

public class Criteria {
	public ModelCore model;
	public Field field;
	
	public Criteria(ModelCore model, Field field) {
		this.model = model;
		this.field = field;
	}
}
