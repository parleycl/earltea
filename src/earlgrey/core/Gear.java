package earlgrey.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import earlgrey.def.ActionDef;
import earlgrey.def.HttpActionDef;
import earlgrey.def.HttpMethod;

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
		this.verbose = HttpMethod.GET;
		if(console){
			this.digest_console(HttpMethod.GET);
		}
		else{
			this.digest(HttpMethod.GET);
		}
	}
	public void post(boolean console) {
		this.verbose = HttpMethod.POST;
		if(console){
			this.digest_console(HttpMethod.POST);
		}
		else{
			this.digest(HttpMethod.POST);
		}
	}
	public void put(boolean console) {
		this.verbose = HttpMethod.PUT;
		if(console){
			this.digest_console(HttpMethod.PUT);
		}
		else{
			this.digest(HttpMethod.PUT);
		}
	}
	public void delete(boolean console) {
		this.verbose = HttpMethod.DELETE;
		if(console){
			this.digest_console(HttpMethod.DELETE);
		}
		else{
			this.digest(HttpMethod.DELETE);
		}
	}
	public void patch(boolean console) {
		this.verbose = HttpMethod.PATCH;
		if(console){
			this.digest_console(HttpMethod.PATCH);
		}
		else{
			this.digest(HttpMethod.PATCH);
		}
	}
	public void options(boolean console) {
		this.verbose = HttpMethod.OPTIONS;
		if(console){
			this.digest_console(HttpMethod.OPTIONS);
		}
		else{
			this.digest(HttpMethod.OPTIONS);
		}
	}
	public void purge(boolean console) {
		this.verbose = HttpMethod.PURGE;
		if(console){
			this.digest_console(HttpMethod.PURGE);
		}
		else{
			this.digest(HttpMethod.PURGE);
		}
	}
	public void head(boolean console) {
		this.verbose = HttpMethod.HEAD;
		if(console){
			this.digest_console(HttpMethod.HEAD);
		}
		else{
			this.digest(HttpMethod.HEAD);
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
		if(!Earlgrey.getInstance().getApi() && !uri.matches(".*(/api/).*")){
			this.response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		// EXTRAEMOS LA RUTA
		String route = (Earlgrey.getInstance().getApi()) ? uri.substring(Earlgrey.getInstance().getContext().getContextPath().length()+1) : uri.substring((uri.indexOf("/api/")+5));
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
		String charset = "UTF-8";
		if(content != null && content.indexOf("application/json") == 0){
			String[] chunks = content.split(";");
			// Search Charset
			if(chunks.length > 1) {
				for(int i=0; i<chunks.length; i++) {
					if(chunks[i].indexOf("charset=") != -1) {
						charset = chunks[i].trim().replace("charset=", "").trim().toUpperCase();
						break;
					}
				}
			}
			try {
				this.request.setCharacterEncoding(charset);
			} catch (UnsupportedEncodingException e1) {
				this.response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			}
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
		
		if(params.length() == 0){
			try {
				String query_string = this.request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				Map<String, String[]> param_body = this.getQueryParameters(query_string);
				Set<String> keys = param_body.keySet();
				for(String key : keys) {
					String[] value = param_body.get(key);
					if(value.length > 0) {
					    params.put(key, URLDecoder.decode(value[0], charset));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return params;
	}
	
	public static Map<String, String[]> getQueryParameters(String payload) {
	    Map<String, String[]> queryParameters = new HashMap<>();
	    String queryString = payload;

	    if (StringUtils.isEmpty(queryString)) {
	        return queryParameters;
	    }

	    String[] parameters = queryString.split("&");

	    for (String parameter : parameters) {
	        String[] keyValuePair = parameter.split("=");
	        String[] values = queryParameters.get(keyValuePair[0]);
	        values = ArrayUtils.add(values, keyValuePair.length == 1 ? "" : keyValuePair[1]); //length is one if no value is available.
	        queryParameters.put(keyValuePair[0], values);
	    }
	    return queryParameters;
	}
	
	private void doAction(ActionDef actionDef, JSONObject params){
		Engine engine = Engine.getInstance();
		if(actionDef.getRoute().hasHttpMethod(this.verbose)) {
			HttpActionDef action = new HttpActionDef(actionDef, this.request, this.response, params, this);
			this.ID = engine.registerTask(action);
			action.set_id(this.ID);
			action.run();
		} else if(actionDef.getRoute().CORS && this.verbose == HttpMethod.OPTIONS) {
			HttpActionDef action = new HttpActionDef(actionDef, this.request, this.response, params, this);
			this.ID = engine.registerTask(action);
			action.set_id(this.ID);
			action.run();
		} else {
			this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		
	}
	public void finalice(){
		Engine engine = Engine.getInstance();
		engine.killProcess(this.ID);
	}
}