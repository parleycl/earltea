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
import earlgrey.annotations.PATCH;
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

@Console(description = "Controller to manage the cluster in console.", name = "Clusters", version = 1)
@Route(route = "/clusters")
@CORS
public class ClusterConsole extends ControllerCore{
	@ControllerAction(description = "Method get clusters.", name = "Get clusters", version = 1)
	@GET
	public static void getClusters(HttpRequest req, HttpResponse res){
		Properties prop = Properties.getInstance();
		JSONArray com = prop.getComunication();
		res.json(com);
		return;
	}
	@ControllerAction(description = "Method update clusters.", name = "Update clusters", version = 1)
	@ParamRequire(name = "cluster")
	@PATCH
	public static void modifyClusters(HttpRequest req, HttpResponse res){
		Properties prop = Properties.getInstance();
		JSONArray cluster = new JSONArray(req.getParam("cluster"));
		prop.setComunication(cluster);
		res.ok("");
		return;
	}
}
