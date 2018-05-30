package earlgrey.admin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.AutoIncrement;
import earlgrey.annotations.CORS;
import earlgrey.annotations.Console;
import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.GET;
import earlgrey.annotations.Model;
import earlgrey.annotations.ModelField;
import earlgrey.annotations.MultiTenant;
import earlgrey.annotations.POST;
import earlgrey.annotations.ParamRequire;
import earlgrey.annotations.Policie;
import earlgrey.annotations.PrimaryKey;
import earlgrey.annotations.Route;
import earlgrey.annotations.Unique;
import earlgrey.annotations.DefaultValue;
import earlgrey.annotations.ModelRelation;
import earlgrey.annotations.ModelJoin;
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.ModelCore;
import earlgrey.core.Properties;
import earlgrey.core.ResourceMaping;
import earlgrey.def.RouteDef;
import earlgrey.def.SessionDef;

@Console(description = "Controller to manage the modesl in console.", name = "Models", version = 1)
@Route(route = "/models")
@CORS
public class ModelsConsole extends ControllerCore{
	@ControllerAction(description = "Method use to get the models", name = "Get Models", version = 1)
	@GET
	public static void getModels(HttpRequest req, HttpResponse res){
		JSONArray retorno = new JSONArray();
		ResourceMaping resources = ResourceMaping.getInstance();
		Hashtable<String, Class<?>> models = resources.getModelTable();
		Enumeration<String> keys = models.keys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			// route name
			JSONObject model = new JSONObject();
			Class<?> mdl = models.get(key);
			Model ctr = mdl.getAnnotation(Model.class);
			model.put("name", ctr.name());
			model.put("table_name", ctr.tableName());
			model.put("version", ctr.version());
			model.put("class_name", mdl.getName());
			model.put("multi_tenant", mdl.isAnnotationPresent(MultiTenant.class));
			// actions
			JSONArray fields = new JSONArray();
			Field[] fld = mdl.getFields();
			for(int i=0; i<fld.length; i++) {
				ModelField fld_action = fld[i].getAnnotation(ModelField.class); 
				if(fld_action != null) {
					JSONObject field_obj = new JSONObject();
					field_obj.put("name", fld[i].getName());
					field_obj.put("type", fld[i].getType().getName());
					field_obj.put("primary", fld[i].isAnnotationPresent(PrimaryKey.class));
					field_obj.put("unique", fld[i].isAnnotationPresent(Unique.class));
					field_obj.put("autoincrement", fld[i].isAnnotationPresent(AutoIncrement.class));
					if(fld[i].isAnnotationPresent(DefaultValue.class)) {
						DefaultValue default_value = fld[i].getAnnotation(DefaultValue.class);
						field_obj.put("defaultValue", default_value.value());
					} else {
						field_obj.put("defaultValue", false);
					}
					
					if(fld[i].isAnnotationPresent(ModelJoin.class)) {
						ModelJoin model_join = fld[i].getAnnotation(ModelJoin.class);
						Class<?> mdl_relation = model_join.model();
						Model mdl_join = mdl_relation.getAnnotation(Model.class);
						if(mdl_join != null) {
							JSONObject join = new JSONObject();
							JSONObject submodel = new JSONObject();
							submodel.put("name", mdl_join.name());
							submodel.put("table_name", mdl_join.tableName());
							submodel.put("version", mdl_join.version());
							submodel.put("class_name", mdl_relation.getName());
							submodel.put("multi_tenant", mdl_relation.isAnnotationPresent(MultiTenant.class));
							join.put("model", submodel);
							join.put("field", model_join.field());
							field_obj.put("model_join", join);
						}
					} else {
						field_obj.put("model_join", false);
					}
					
					if(fld[i].isAnnotationPresent(ModelRelation.class)) {
						ModelRelation model_relation = fld[i].getAnnotation(ModelRelation.class);
						Class<?> mdl_relation = model_relation.model();
						Model mdl_join = mdl_relation.getAnnotation(Model.class);
						if(mdl_join != null) {
							JSONObject submodel = new JSONObject();
							submodel.put("name", mdl_join.name());
							submodel.put("table_name", mdl_join.tableName());
							submodel.put("version", mdl_join.version());
							submodel.put("class_name", mdl_relation.getName());
							submodel.put("multi_tenant", mdl_relation.isAnnotationPresent(MultiTenant.class));
							field_obj.put("model_relation", submodel);
						}
					} else {
						field_obj.put("model_relation", false);
					}
					fields.put(field_obj);
				}
			}
			model.put("fields", fields);
			retorno.put(model);
		}
		res.json(retorno);
		return;
	}
}
