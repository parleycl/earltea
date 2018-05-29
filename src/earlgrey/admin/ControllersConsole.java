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
	
}
