package earlgrey.admin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.CORS;
import earlgrey.annotations.Console;
import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.GET;
import earlgrey.annotations.Model;
import earlgrey.annotations.POST;
import earlgrey.annotations.ParamRequire;
import earlgrey.annotations.Policie;
import earlgrey.annotations.Route;
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.ModelCore;
import earlgrey.core.Properties;
import earlgrey.core.ResourceMaping;
import earlgrey.def.ActionDef;
import earlgrey.def.HttpMethod;
import earlgrey.def.RouteDef;
import earlgrey.def.SessionDef;

@Console(description = "Controller to manage the controllers in console.", name = "Controllers", version = 1)
@Route(route = "/controllers")
@CORS
public class ControllersConsole extends ControllerCore{
	@ControllerAction(description = "Method use to get the controllers", name = "Get Controllers", version = 1)
	@GET
	public static void getControllers(HttpRequest req, HttpResponse res){
		JSONArray retorno = new JSONArray();
		ResourceMaping resources = ResourceMaping.getInstance();
		Hashtable<String, Class<?>> controllers = resources.getControllerTable();
		Enumeration<String> keys = controllers.keys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			// route name
			JSONObject controller = new JSONObject();
			Class<?> ctllr = controllers.get(key);
			Controller ctr = ctllr.getAnnotation(Controller.class);
			controller.put("name", ctr.name());
			controller.put("description", ctr.description());
			controller.put("version", ctr.version());
			controller.put("class_name", ctllr.getName());
			// actions
			JSONArray actions = new JSONArray();
			Method[] methods = ctllr.getMethods();
			for(int i=0; i<methods.length; i++) {
				ControllerAction ctr_action = methods[i].getAnnotation(ControllerAction.class); 
				if(ctr_action != null) {
					JSONObject action_obj = new JSONObject();
					action_obj.put("name", ctr_action.name());
					action_obj.put("description", ctr_action.description());
					action_obj.put("version", ctr_action.version());
					action_obj.put("method_name", methods[i].getName());
					actions.put(action_obj);
				}
			}
			controller.put("actions", actions);
			retorno.put(controller);
		}
		res.json(retorno);
		return;
	}
}
