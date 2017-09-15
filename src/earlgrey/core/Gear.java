package earlgrey.core;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import earlgrey.core.Logging;
import earlgrey.core.Router;
import earlgrey.def.HttpActionDef;
import earlgrey.def.RouteDef;

public class Gear {
	// DEFINIMOS EL CLASS QUE EFECTUA LAS OPERACIONES DE LA CONSOLA
	HttpServletRequest request;
	HttpServletResponse response;
	private Logging log;
	public String verbose;
	private int ID;
	private String SESSION_ID;
	// DEFINIMOS EL CONSTRUCTOR
	public Gear(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.SESSION_ID = request.getSession().getId();
	}
	public void get(boolean console) {
		this.verbose = "GET";
		if(console){
			this.digest_console("GET");
		}
		else{
			this.digest("GET");
		}
	}
	public void post(boolean console) {
		this.verbose = "POST";
		if(console){
			this.digest_console("POST");
		}
		else{
			this.digest("POST");
		}
	}
	public void put(boolean console) {
		this.verbose = "PUT";
		if(console){
			this.digest_console("PUT");
		}
		else{
			this.digest("PUT");
		}
	}
	public void delete(boolean console) {
		this.verbose = "DELETE";
		if(console){
			this.digest_console("DELETE");
		}
		else{
			this.digest("DELETE");
		}
	}
	private void digest_console(String verbose){
		Enumeration<String> param = this.request.getParameterNames();
		String uri = this.request.getRequestURI();
		// VERIFICAMOS QUE SEA UNA QUERY VALIDA
		// EN CASO CONTRARIIO RETORNAMOS UN ERROR 404
		if(!uri.matches(".*(/console/).*")){
			this.response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// EXTRAEMOS LA RUTA
		String route = uri.substring((uri.indexOf("/console/")+9));
		// MODIFICAMOS LA RUTA PARA INDICAR QUE ES CONSOLA
		route = "admin/console/"+route;
		// EXTRAEMOS LOS PARAMETROS
		JSONObject params = new JSONObject();
		while(param.hasMoreElements()){
			String name = param.nextElement();
			params.put(name,this.request.getAttribute(name));
		}
		Router router = new Router();
		RouteDef action = router.route(route);
		if(action == null){
			this.response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		else
		{
			// BUSCAMOS URI PARAMS
			JSONObject uri_params = action.getParams();
			Iterator<String> llaves = uri_params.keys();
			while(llaves.hasNext()){
				String llave = llaves.next();
				params.put(llave, uri_params.get(llave));
			}
			this.doAction(action, params);
		}
	}
	private void digest(String verbose){
		Enumeration<String> param = this.request.getParameterNames();
		String uri = this.request.getRequestURI();
		// VERIFICAMOS QUE SEA UNA QUERY VALIDA
		// EN CASO CONTRARIIO RETORNAMOS UN ERROR 404
		if(!uri.matches(".*(/api/).*")){
			this.response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// EXTRAEMOS LA RUTA
		String route = uri.substring((uri.indexOf("/api/")+5));
		// MODIFICAMOS LA RUTA PARA INDICAR QUE ES CONSOLA
		// EXTRAEMOS LOS PARAMETROS
		JSONObject params = new JSONObject();
		while(param.hasMoreElements()){
			String name = param.nextElement();
			params.put(name,this.request.getParameter(name));
		}
		Enumeration<String> enume = this.request.getHeaderNames();
		while(enume.hasMoreElements()){
			System.out.println(enume.nextElement());
		}
		System.out.println(this.request.getHeader("iv-groups"));
		System.out.println(this.request.getHeader("iv-user"));
		System.out.println(this.request.getHeader("iv-remote-address"));
		Router router = new Router();
		RouteDef action = router.route(route);
		//DECIDIMOS LA ACTION
		if(action == null){
			this.response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		else
		{
			// BUSCAMOS URI PARAMS
			JSONObject uri_params = action.getParams();
			Iterator<String> llaves = uri_params.keys();
			while(llaves.hasNext()){
				String llave = llaves.next();
				params.put(llave, uri_params.get(llave));
			}
			this.doAction(action, params);
		}
	}
	private void doAction(RouteDef route, JSONObject params){
		Engine engine = Engine.getInstance();
		switch(verbose){
			case "POST":
				if(route.POST){
					HttpActionDef action = new HttpActionDef(route, this.request, this.response, params, this);
					this.ID = engine.registerTask(action);
					action.set_id(this.ID);
					action.run();
				}
				else
				{
					this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				}
				break;
			case "GET":
				if(route.GET){
					HttpActionDef action = new HttpActionDef(route, this.request, this.response, params, this);
					this.ID = engine.registerTask(action);
					action.set_id(this.ID);
					action.run();
				}
				else
				{
					this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				}
				break;
			case "DELETE":
				if(route.DELETE){
					HttpActionDef action = new HttpActionDef(route, this.request, this.response, params, this);
					this.ID = engine.registerTask(action);
					action.set_id(this.ID);
					action.run();
				}
				else
				{
					this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				}
				break;
			case "PUT":
				if(route.PUT){
					HttpActionDef action = new HttpActionDef(route, this.request, this.response, params, this);
					this.ID = engine.registerTask(action);
					action.set_id(this.ID);
					action.run();
				}
				else
				{
					this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				}
				break;
			default:
				this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				break;
		}
		
	}
	public void finalice(){
		Engine engine = Engine.getInstance();
		engine.killProcess(this.ID);
	}
}