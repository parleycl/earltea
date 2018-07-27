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
import earlgrey.annotations.DELETE;
import earlgrey.annotations.GET;
import earlgrey.annotations.PATCH;
import earlgrey.annotations.POST;
import earlgrey.annotations.ParamRequire;
import earlgrey.annotations.Policie;
import earlgrey.annotations.Route;
import earlgrey.core.CacheCore;
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.Logging;
import earlgrey.core.ModelCore;
import earlgrey.core.Properties;
import earlgrey.core.ResourceMaping;
import earlgrey.def.RouteDef;
import earlgrey.def.SessionDef;

@Console(description = "Controller to manage the cache in console.", name = "Cache", version = 1)
@Route(route = "/cache")
@CORS
public class CacheConsole extends ControllerCore{
	@ControllerAction(description = "Clear Cache.", name = "Clear Cache", version = 1)
	@DELETE
	public static void clearCache(HttpRequest req, HttpResponse res){
		CacheCore.getInstance().cleanAllCache();
		Logging log = new Logging(CacheConsole.class.getName());
		log.Info("Task ready - Cache Clear");
		res.noContent();
		return;
	}
}
