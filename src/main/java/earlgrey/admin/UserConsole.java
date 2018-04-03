package earlgrey.admin;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.Console;
import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.ParamRequire;
import earlgrey.annotations.Policie;
import earlgrey.annotations.Route;
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.ModelCore;

@Console(description = "Controlador para manejar los usuarios del admin panel.", name = "Users", version = 1)
@Route(route = "/users")
public class UserConsole extends ControllerCore{
	//CONTROLADOR DE PRUEBA PARA EFECTUAR DESARROLLO DE LA PLATAFORMA.
	@ControllerAction(description = "Metodo de autentificación para el admin mediante session mediante WebSeal y Tivoli.", name = "session_login", version = 1)
	@Route(route = "/autologin")
	public static void SessionLogin(HttpRequest req, HttpResponse res){
		JSONObject headers = req.getHeader();
		if(headers.has("iv-user") && headers.has("iv-group")){
			if(headers.getString("iv-group") != null && headers.getString("iv-group") != "Unauthenticated" 
					&& headers.getString("iv-user") != null && headers.getString("iv-user") != "Unauthenticated"){
				String[] groups = headers.getString("iv-group").split(",");
				req.getSession().setAuth(headers.getString("iv-user"), groups);
			}
		}
		return;
	}
	//CONTROLADOR DE PRUEBA PARA EFECTUAR DESARROLLO DE LA PLATAFORMA.
	@ControllerAction(description = "Metodo de autentificación para el admin mediante session.", name = "user_login", version = 1)
	@Route(route = "/login")
	public static void Login(HttpRequest req, HttpResponse res){
		JSONObject informes = new JSONObject();
		informes.put("USERS", 0);
		res.json(informes);
		return;
	}
	//CONTROLADOR DE PRUEBA PARA EFECTUAR DESARROLLO DE LA PLATAFORMA.
	@ControllerAction(description = "Metodo de autentificación para el admin mediante session.", name = "user_login", version = 1)
	@Route(route = "/status")
	public static void Status(HttpRequest req, HttpResponse res){
		JSONObject informes = new JSONObject();
		informes.put("USERS", 0);
		res.json(informes);
		return;
	}
}
