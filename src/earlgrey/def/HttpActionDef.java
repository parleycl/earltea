package earlgrey.def;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import earlgrey.annotations.AddPropertie;
import earlgrey.annotations.Cache;
import earlgrey.annotations.ParamOptional;
import earlgrey.annotations.ParamRequire;
import earlgrey.annotations.UserCache;
import earlgrey.core.CacheCore;
import earlgrey.core.CacheElement;
import earlgrey.core.Gear;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.Logging;
import earlgrey.core.Session;
import earlgrey.error.Error500;
import earlgrey.interfaces.Process;
// ACA DECLARAMOS LAS ACCIONES QUE ENTRAN POR API REST Y WEBSOCKET.
public class HttpActionDef implements Runnable, Process{
	// DECLARAMOS LAS PROPIEDADES DE LA PETICION
	private HttpServletRequest request; 
	private HttpServletResponse response;
	private SessionDef session;
	// DECLARAMOS LOS PARAMETROS
	private JSONObject parametros;
	// DECLARAMOS LOS MOTORES
	private Gear motor;
	// ID DEL ACTION / HASH
	private int ID;
	// CACHE
	private boolean cache = false;
	// OBJETO DE RUTA
	private RouteDef route;
	// OBJETO DE RUTA
	private ActionDef action;
	// LOGGING
	private Logging log = new Logging(this.getClass().getName());
	
	// DECLARAMOS LOS CONSTRUCTORES
	public HttpActionDef(ActionDef action, HttpServletRequest request, HttpServletResponse response, 
			JSONObject parametros, Gear motor){
		this.request = request;
		this.response = response;
		this.parametros = parametros;
		this.motor = motor;
		this.route = action.getRoute();
		this.action = action;
		this.session = this.session();
	}
	
	private SessionDef session(){
		String authorization = (this.request.getHeader("authorization") != null) ? this.request.getHeader("authorization") : this.request.getHeader("Authorization");
		if(authorization != null) {
			String token = (authorization.indexOf("Bearer") != -1) ? authorization.substring(authorization.indexOf("Bearer")+7) : authorization;
			return Session.getInstance().getJWTSession(token, request.getSession().getId());
		} else {
			return Session.getInstance().getSession(request.getSession().getId());
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(this.checkAllParams()) {
			this.execute();
		} else {
			this.response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			try {
				this.response.getWriter().println("Parameters are required");
			} catch (IOException e) {
				this.log.Critic("Occured an error when Earlgrey render the response", Error500.HTTP_RENDER_ERROR);
			}
			this.finalice();
		}
	}
	private boolean checkAllParams() {
		// BUSCAMOS PARAMETROS OPCIONALES Y LOS AGREGAMOS SI HACE FALTA
		ParamOptional[] pa_op = this.action.metodo.getAnnotationsByType(ParamOptional.class);
		for(int i=0; i<pa_op.length; i++){
			if(!this.parametros.has(pa_op[i].name())){
				this.parametros.put(pa_op[i].name(), pa_op[i].defaultsTo());
			}
		}
		// BUSCAMOS PARAMETROS REQUERIDOS
		ParamRequire[] pa_re = this.action.metodo.getAnnotationsByType(ParamRequire.class);
		for(int i=0; i<pa_re.length; i++){
			if(!this.parametros.has(pa_re[i].name())){
				return false;
			}
			if(pa_re[i].type() == int.class && !StringUtils.isNumeric(this.parametros.getString(pa_re[i].name()))){
				return false;
			}
			Pattern redbl=Pattern.compile("[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
			if(pa_re[i].type() == double.class && !redbl.matcher(this.parametros.getString(pa_re[i].name())).matches()){
				return false;
			}
		}
		return true;
	}
	// DECLARAMOS EL METODO DE EJECUCIÓN
	private void execute() {
		// ANALIZAMOS LAS POLICIES
		
		// ESTE ES EL NUCLEO DE LAS PETICIONES SE CREA UN NUEVO OBJETO REQUEST Y UN OBJETO RESPONSE
		HttpRequest request = new HttpRequest(this.parametros, this.request, this);
		HttpResponse response = new HttpResponse(this.response, this.request, this, this.action.getRoute().CORS);
		Method metodo = this.action.metodo;
		this.session.ping(metodo);
		if(metodo.isAnnotationPresent(Cache.class)){
			CacheCore cache = CacheCore.getInstance();
			if(cache.hasCache(this.request.getRequestURI()+this.parametros.toString())) {
				CacheElement element = cache.getCache(this.request.getRequestURI()+this.parametros.toString());
				this.processCache(element, response);
				return;
			} else {
				this.cache = true;
			}
		}
		if(metodo.isAnnotationPresent(UserCache.class)){
			if(this.session.hasCache(this.request.getRequestURI()+this.parametros.toString())) {
				CacheElement element = this.session.getCache(this.request.getRequestURI()+this.parametros.toString());
				this.processCache(element, response);
				return;
			} else {
				this.cache = true;
			}
		}
		try {
			metodo.invoke(null, request,response);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			StackTraceElement[] stack = e.getCause().getStackTrace();
			this.log.Critic("Earlgrey detect an error in the method call: "+metodo.getName(), Error500.METHOD_INVOCATION_ERROR);
			this.log.Critic("Cause: "+e.getCause().getMessage(), Error500.METHOD_INVOCATION_ERROR);
			this.log.Critic("Cause: ", Error500.METHOD_INVOCATION_ERROR);
			this.log.Critic("Localized Message: "+e.getLocalizedMessage(), Error500.METHOD_INVOCATION_ERROR);
			this.log.Critic("Message:"+e.getMessage(), Error500.METHOD_INVOCATION_ERROR);
			this.log.Critic("-------------------- STACK --------------------", Error500.METHOD_INVOCATION_ERROR);
			for(int i=0;i<stack.length;i++){
				this.log.Critic(stack[i].toString(), Error500.METHOD_INVOCATION_ERROR);
			}
		}
	}
	private void processCache(CacheElement element, HttpResponse response){
		if(element.getType() == CacheCore.CACHE_JSON){
			this.response.setContentType("application/json");
		} else if(element.getType() == CacheCore.CACHE_XML) {
			this.response.setContentType("application/xml");
			
		} else if(element.getType() == CacheCore.CACHE_TEXT) {
			this.response.setContentType("text/plain");
		}
		try {
			this.response.setCharacterEncoding("UTF-8");
			this.response.setStatus(HttpServletResponse.SC_OK);
			this.response.getWriter().println(element.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.finalice();
	}
	// METODO PARA PROCESAR CON METODO ASOACIADO
	// METODO DE LLAMADA PARA RETIRAR EL ACTION DEL TASKLIST
	public void finalice(){
		this.motor.finalice();
	}
	public void set_id(int id){
		this.ID = id;
	}
	public SessionDef getSession(){
		return this.session;
	}
	public void checkCache(String content, int type){
		if(this.cache){
			Method metodo = this.action.metodo;
			if(metodo.isAnnotationPresent(Cache.class)){
				Cache cache_annotation = metodo.getAnnotation(Cache.class);
				CacheCore cache = CacheCore.getInstance();
				cache.setCache(this.request.getRequestURI()+this.parametros.toString(), content, cache_annotation.time(), type);
			} else if(metodo.isAnnotationPresent(UserCache.class)) {
				Cache cache_annotation = metodo.getAnnotation(Cache.class);
				this.session.setCache(this.request.getRequestURI()+this.parametros.toString(), content, cache_annotation.time(), type);
			}
		}
	}
}
