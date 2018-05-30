package earlgrey.admin;

import java.util.ArrayList;
import java.util.Arrays;

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
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.ModelCore;
import earlgrey.core.Properties;
import earlgrey.def.SessionDef;

@Console(description = "Controller to manage the admin panel users.", name = "Admins", version = 1)
@Route(route = "/admins")
@CORS
public class UserConsole extends ControllerCore{
	//CONTROLADOR DE PRUEBA PARA EFECTUAR DESARROLLO DE LA PLATAFORMA.
	@ControllerAction(description = "Metodo de autentificación para el admin mediante session mediante WebSeal y Tivoli.", name = "session_login", version = 1)
	@Route(route = "/autologin")
	@GET
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
	@ControllerAction(description = "Method to login with user and password.", name = "user_login", version = 1)
	@Route(route = "/login")
	@ParamRequire(name = "username")
	@ParamRequire(name = "password")
	@POST
	public static void Login(HttpRequest req, HttpResponse res){
		SessionDef session = req.getSession();
		String username = req.getParam("username");
		String password = req.getParam("password");
		JSONArray users = Properties.getInstance().getConsoleUsers();
		for(int i=0; i < users.length(); i++) {
			if(users.getJSONObject(i).getString("USERNAME").equals(username) && users.getJSONObject(i).getString("PASSWORD").equals(password)) {
				session.setAdmin("Admin");
				JSONObject user = new JSONObject();
				user.put("USER", "Admin");
				user.put("ROLE", "Admin");
				res.json(user);
				return;
			}
		}
		res.notFound();
		return;
	}
	
	@ControllerAction(description = "Method to get the session user status.", name = "user_login", version = 1)
	@Route(route = "/status")
	@GET
	public static void Status(HttpRequest req, HttpResponse res){
		SessionDef session = req.getSession();
		if(session.isAdmin()){
			JSONObject user = new JSONObject();
			user.put("USER", session.getUser());
			user.put("ROLE", "Admin");
			res.json(user);
			return;
		}
		res.forbidden();
		return;
	}
	
	@ControllerAction(description = "Method to logout the admin user.", name = "user_login", version = 1)
	@Route(route = "/logout")
	@GET
	public static void logout(HttpRequest req, HttpResponse res){
		SessionDef session = req.getSession();
		session.killUser();
		res.ok("");
		return;
	}
	
	@ControllerAction(description = "Method get all users.", name = "Get Users", version = 1)
	@GET
	public static void getUsers(HttpRequest req, HttpResponse res){
		JSONArray retorno = new JSONArray();
		JSONArray users = Properties.getInstance().getConsoleUsers();
		for(int i=0; i < users.length(); i++) {
			retorno.put((new JSONObject()).put("username", users.getJSONObject(i).getString("USERNAME")).put("id", i));
		}
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Method update user", name = "Get Users", version = 1)
	@PATCH
	@Route(route = "/:id")
	public static void updateUsers(HttpRequest req, HttpResponse res){
		String username = req.getParam("username");
		String password = req.getParam("password");
		JSONArray users = Properties.getInstance().getConsoleUsers();
		JSONObject user = users.getJSONObject(Integer.parseInt(req.getParam("id")));
		user.put("USERNAME", username);
		user.put("PASSWORD", password);
		Properties.getInstance().saveToDisk();
		Properties.getInstance().restartProperties();
		res.ok("");
		return;
	}
	@ControllerAction(description = "Method delete user.", name = "Delete users", version = 1)
	@DELETE
	@Route(route = "/:id")
	public static void deleteUsers(HttpRequest req, HttpResponse res){
		String username = req.getParam("username");
		String password = req.getParam("password");
		JSONArray users = Properties.getInstance().getConsoleUsers();
		users.remove(Integer.parseInt(req.getParam("id")));
		Properties.getInstance().saveToDisk();
		Properties.getInstance().restartProperties();
		res.noContent();
		return;
	}
}
