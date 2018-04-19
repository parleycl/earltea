package earlgrey.admin;

import java.util.ArrayList;
import java.util.Arrays;

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
import earlgrey.core.Engine;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.Earlgrey;
import earlgrey.core.ModelCore;
import earlgrey.core.Properties;
import earlgrey.def.Datasource;

@Console(description = "Controller to manage the datasources", name = "Datasources", version = 1)
@Route(route = "/datasources")
@CORS
public class Datasources extends ControllerCore{
	//CONTROLADOR PARA GESTIONAR LAS PROPERTIES
	@ControllerAction(description = "Method use to modify a datasource", name = "modify_datasource", version = 1)
	@PATCH
	@ParamRequire(name = "datasource")
	@ParamRequire(name = "name")
	@ParamRequire(name = "enviroment")
	public static void setDatasource(HttpRequest req, HttpResponse res){
		Properties prop = Properties.getInstance();
		String name = req.getParam("name");
		String enviroment = req.getParam("enviroment");
		Datasource datasource = new Datasource(new JSONObject(req.getParam("datasource")));
		prop.setDatasource(name, enviroment, datasource);
		res.noContent();
		return;
	}
}
