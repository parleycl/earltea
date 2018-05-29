package earlgrey.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.CORS;
import earlgrey.annotations.Console;
import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.GET;
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
import earlgrey.def.RouteDef;
import earlgrey.def.SessionDef;

@Console(description = "Controller to manage the modesl in console.", name = "Models", version = 1)
@Route(route = "/models")
@CORS
public class ModelsConsole extends ControllerCore{
	@GET
	public static void getModels(HttpRequest req, HttpResponse res){
		ResourceMaping resources = ResourceMaping.getInstance();
		Hashtable<String, RouteDef> routes = resources.getRouteTable();
		Enumeration<String> keys = routes.keys();
		while(keys.hasMoreElements()) {
			System.out.println(keys.nextElement());
		}
		return;
	}
}
