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

@Console(description = "Controller to manage the routes in console.", name = "Routes", version = 1)
@Route(route = "/routes")
@CORS
public class RoutesConsole extends ControllerCore{
	@ControllerAction(description = "Method use to get the routes", name = "Get Routes", version = 1)
	@GET
	public static void getRoutes(HttpRequest req, HttpResponse res){
		JSONArray retorno = new JSONArray();
		ResourceMaping resources = ResourceMaping.getInstance();
		Hashtable<String, RouteDef> RouteMap = resources.getRouteMap();
		Hashtable<String, RouteDef> routes = resources.getRouteTable();
		Enumeration<String> keys = routes.keys();
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			// route name
			JSONObject ruta = new JSONObject();
			ruta.put("route", key);
			// actions
			JSONArray actions = new JSONArray();
			String[] rutas = key.split("/");
			if(rutas.length > 0){
				if(RouteMap.containsKey(rutas[0])){
					RouteDef router = RouteMap.get(rutas[0]);
					rutas = ArrayUtils.remove(rutas,0);
					router = router.digest_route(rutas);
					Hashtable<Integer,ActionDef> action_table = router.getActions();
					// VERIFY THE HTTP METHODS ALLOWED
					Enumeration<Integer> action_keys = action_table.keys();
					while(action_keys.hasMoreElements()) {
						Integer action_key = action_keys.nextElement();
						ActionDef action = action_table.get(action_key);
						// If console action skip
						if(action.isConsole()) continue;
						// then continue
						JSONObject actioninfo = new JSONObject();
						// Show the method
						if(action.getMetodo() != null) {
							JSONObject action_obj = new JSONObject();
							Method mt = action.getMetodo();
							ControllerAction mt_info = mt.getAnnotation(ControllerAction.class);
							if(mt_info != null) {
								action_obj.put("name", mt_info.name());
								action_obj.put("method_name", mt.getName());
								action_obj.put("description", mt_info.description());
								action_obj.put("version", mt_info.version());
								actioninfo.put("action", action_obj);
							}
						}
						// Show the controller
						if(action.getController() != null) {
							JSONObject controller_obj = new JSONObject();
							Class<?> mt = action.getController();
							Controller mt_info = mt.getAnnotation(Controller.class);
							if(mt_info != null) {
								controller_obj.put("name", mt_info.name());
								controller_obj.put("class_name", mt.getName());
								controller_obj.put("description", mt_info.description());
								controller_obj.put("version", mt_info.version());
								actioninfo.put("controller", controller_obj);
							}
						}
						actioninfo.put("blueprint", action.ModelRest);
						// Show the model
						if(action.Model != null) {
							JSONObject action_obj = new JSONObject();
							Class<?> mt = action.Model;
							Model mt_info = mt.getAnnotation(Model.class);
							if(mt_info != null) {
								action_obj.put("name", mt_info.name());
								action_obj.put("class_name", mt.getName());
								action_obj.put("table_name", mt_info.tableName());
								action_obj.put("version", mt_info.version());
								actioninfo.put("model", action_obj);
							}
						}
						if(action_key == HttpMethod.GET) {
							actioninfo.put("method", "GET");
						} else if(action_key == HttpMethod.POST) {
							actioninfo.put("method", "POST");
						} else if(action_key == HttpMethod.DELETE) {
							actioninfo.put("method", "DELETE");
						} else if(action_key == HttpMethod.PATCH) {
							actioninfo.put("method", "PATCH");
						} else if(action_key == HttpMethod.PUT) {
							actioninfo.put("method", "PUT");
						} else if(action_key == HttpMethod.OPTIONS) {
							actioninfo.put("method", "OPTIONS");
						} else if(action_key == HttpMethod.HEAD) {
							actioninfo.put("method", "HEAD");
						} else if(action_key == HttpMethod.PURGE) {
							actioninfo.put("method", "PURGE");
						} else {
							actioninfo.put("method", "UNKNOWN");
						}
						actions.put(actioninfo);
					}
				}
			}
			if(actions.length() > 0) {
				ruta.put("actions", actions);
				retorno.put(ruta);
			}
		}
		res.json(retorno);
		return;
	}
}
