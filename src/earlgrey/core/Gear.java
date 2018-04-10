package earlgrey.core;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import earlgrey.core.Logging;
import earlgrey.core.Router;
import earlgrey.def.ActionDef;
import earlgrey.def.HttpActionDef;
import earlgrey.def.HttpMethods;
import earlgrey.def.RouteDef;

public class Gear {
	// DEFINIMOS EL CLASS QUE EFECTUA LAS OPERACIONES DE LA CONSOLA
	HttpServletRequest request;
	HttpServletResponse response;
	private Logging log;
	public int verbose;
	private int ID;
	private String SESSION_ID;
	// DEFINIMOS EL CONSTRUCTOR
	public Gear(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.SESSION_ID = request.getSession().getId();
	}
	public void get(boolean console) {
		this.verbose = HttpMethods.GET;
		if(console){
			this.digest_console(HttpMethods.GET);
		}
		else{
			this.digest(HttpMethods.GET);
		}
	}
	public void post(boolean console) {
		this.verbose = HttpMethods.POST;
		if(console){
			this.digest_console(HttpMethods.POST);
		}
		else{
			this.digest(HttpMethods.POST);
		}
	}
	public void put(boolean console) {
		this.verbose = HttpMethods.PUT;
		if(console){
			this.digest_console(HttpMethods.PUT);
		}
		else{
			this.digest(HttpMethods.PUT);
		}
	}
	public void delete(boolean console) {
		this.verbose = HttpMethods.DELETE;
		if(console){
			this.digest_console(HttpMethods.DELETE);
		}
		else{
			this.digest(HttpMethods.DELETE);
		}
	}
	public void patch(boolean console) {
		this.verbose = HttpMethods.PATCH;
		if(console){
			this.digest_console(HttpMethods.PATCH);
		}
		else{
			this.digest(HttpMethods.PATCH);
		}
	}
	public void options(boolean console) {
		this.verbose = HttpMethods.OPTIONS;
		if(console){
			this.digest_console(HttpMethods.OPTIONS);
		}
		else{
			this.digest(HttpMethods.OPTIONS);
		}
	}
	public void purge(boolean console) {
		this.verbose = HttpMethods.PURGE;
		if(console){
			this.digest_console(HttpMethods.PURGE);
		}
		else{
			this.digest(HttpMethods.PURGE);
		}
	}
	public void head(boolean console) {
		this.verbose = HttpMethods.HEAD;
		if(console){
			this.digest_console(HttpMethods.HEAD);
		}
		else{
			this.digest(HttpMethods.HEAD);
		}
	}
	private void digest_console(int verbose){
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
		JSONObject params = this.extract_params();
		
		Router router = new Router();
		ActionDef action = router.route(route, verbose);
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
	private void digest(int verbose){
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
		
		JSONObject params = this.extract_params();
		
		Router router = new Router();
		ActionDef action = router.route(route, verbose);
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
	private JSONObject extract_params() {
		String content = this.request.getHeader("Content-Type");
		JSONObject params = new JSONObject();
		if(content != null && content.equals("application/json")){
			StringBuffer jb = new StringBuffer();
			String line = null;
			try {
			    BufferedReader reader = this.request.getReader();
			    while ((line = reader.readLine()) != null)
			      jb.append(line);
			} catch (Exception e) {
				this.response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
			
			try {
			    params =  new JSONObject(jb.toString());
			} catch (JSONException e) {
			    // crash and burn
				this.response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		else
		{
			Enumeration<String> param = this.request.getParameterNames();
			while(param.hasMoreElements()){
				String name = param.nextElement();
				params.put(name,this.request.getParameter(name));
			}
		}
		return params;
	}
	private void doAction(ActionDef actionDef, JSONObject params){
		Engine engine = Engine.getInstance();
		if(actionDef.getRoute().hasHttpMethod(this.verbose)){
			HttpActionDef action = new HttpActionDef(actionDef, this.request, this.response, params, this);
			this.ID = engine.registerTask(action);
			action.set_id(this.ID);
			action.run();
		}
		else
		{
			this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		
	}
	public void finalice(){
		Engine engine = Engine.getInstance();
		engine.killProcess(this.ID);
	}
}