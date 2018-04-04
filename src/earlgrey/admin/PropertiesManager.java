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
import earlgrey.core.Kernel;
import earlgrey.core.ModelCore;
import earlgrey.core.Properties;

@Console(description = "Controlador para gestionar las properties", name = "Properties", version = 1)
@Route(route = "/properties")
@CORS
public class PropertiesManager extends ControllerCore{
	//CONTROLADOR PARA GESTIONAR LAS PROPERTIES
	@ControllerAction(description = "Metodo para obtener los entornos de properties.", name = "get_env", version = 1)
	@Route(route = "/getenv")
	@GET
	public static void GetEnviroments(HttpRequest req, HttpResponse res){
		Properties prop = Properties.getInstance();
		JSONObject obj = prop.getPropertiesEnv();
		JSONArray env = obj.getJSONArray("ENV");
		JSONObject retorno = new JSONObject();
		retorno.put("SELECTED", obj.getString("SELECTED"));
		retorno.put("ENV", env);
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Metodo para designar un entorno de properties por defecto.", name = "set_env", version = 1)
	@ParamRequire(name = "env", type = String.class)
	@Route(route = "/setenv")
	@POST
	public static void SetEnviroments(HttpRequest req, HttpResponse res){
		Properties prop = Properties.getInstance();
		String env = req.getParam("env");
		prop.selectEnv(env);
		JSONObject retorno = new JSONObject();
		retorno.put("STATUS", "OK");
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Metodo para crear un entorno properties por defecto.", name = "create_env", version = 1)
	@Route(route = "/createenv")
	@ParamRequire(name = "name", type = String.class)
	@POST
	public static void createEnviroments(HttpRequest req, HttpResponse res){
		String env = req.getParam("name");
		JSONObject objeto = Properties.getInstance().createEnv(env);
		res.json(objeto);
		return;
	}
	@ControllerAction(description = "Metodo para modificar un entorno de properties.", name = "mod_env", version = 1)
	@Route(route = "/modprop")
	@ParamRequire(name = "name", type = String.class)
	@ParamRequire(name = "prop", type = String.class)
	@PATCH
	public static void modPropertie(HttpRequest req, HttpResponse res){
		JSONObject obj = new JSONObject(req.getParam("prop"));
		Properties.getInstance().setProp(obj,req.getParam("name"));
		JSONObject retorno = new JSONObject();
		retorno.put("STATUS", "OK");
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Metodo para verificar el estado de restarting.", name = "get_restart_status", version = 1)
	@Route(route = "/getrestart")
	@GET
	public static void getRestartStatus(HttpRequest req, HttpResponse res){
		JSONObject retorno = new JSONObject();
		retorno.put("STATUS", Engine.getInstance().getRestartStatus());
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Metodo para reiniciar el entorno de properties.", name = "restart_env", version = 1)
	@Route(route = "/restart")
	@POST
	public static void restartEnv(HttpRequest req, HttpResponse res){
		Properties.getInstance().restartProperties();
		JSONObject retorno = new JSONObject();
		retorno.put("STATUS", "OK");
		res.json(retorno);
		return;
	}
}
