package earlgrey.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.CORS;
import earlgrey.annotations.Console;
import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.GET;
import earlgrey.annotations.ParamRequire;
import earlgrey.annotations.Policie;
import earlgrey.annotations.Route;
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.Logging;
import earlgrey.core.ModelCore;
import earlgrey.core.Properties;

@Console(description = "Controlador para gestionar los logs", name = "Logs", version = 1)
@Route(route = "/logs")
@CORS
public class LogsConsole extends ControllerCore{
	//CONTROLADOR PARA GESTIONAR LAS PROPERTIES
	@ControllerAction(description = "Metodo para obtener el historial de logs.", name = "logs_history", version = 1)
	@Route(route = "/gethistory")
	@GET
	public static void GetHistory(HttpRequest req, HttpResponse res){
		File[] logs = Logging.getHistory();
		JSONArray historial = new JSONArray();
		for(File log:logs){
			historial.put(log.getName());
		}
		res.json(historial);
		return;
	}
	@ControllerAction(description = "Metodo para designar un set de properties por defecto.", name = "set_env", version = 1)
	@Route(route = "/getlog")
	@ParamRequire(name = "from", type = double.class)
	@ParamRequire(name = "file", type = String.class)
	@GET
	public static void getFile(HttpRequest req, HttpResponse res){
		Logging log = new Logging("Logs");
		JSONArray lineas = Logging.getFile(req.getParam("file"), Integer.valueOf(req.getParam("from")));
		res.json(lineas);
		return;
	}
}
