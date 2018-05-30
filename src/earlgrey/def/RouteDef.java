package earlgrey.def;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import com.sun.mail.iap.Response;

import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.Logging;
import earlgrey.core.ModelCore;
import earlgrey.error.Error500;
import earlgrey.http.CORSResponse;
import earlgrey.interfaces.PolicieCore;

public class RouteDef implements Cloneable {
	protected String path;
	private Hashtable<Integer,ActionDef> actions = new Hashtable<Integer,ActionDef>();
	protected ArrayList<String> ParamByUri;
	public boolean CORS;
	protected Logging log = new Logging(this.getClass().getName());
	// CARGAMOS LA TABLA DE RUTAS
	private Hashtable<String,RouteDef> RouteTable = new Hashtable<String,RouteDef>();
	//DEFINIMOS LOS CONSTRUCTORES
	public RouteDef(String path){
		this.path = path;
		this.ParamByUri = new ArrayList<String>();
	}
	//PROCESAMIENTO DE RUTA
	public boolean verify(HttpServletRequest request, HttpServletResponse response, Hashtable<String,String> parametros){
		//VERIFICAMOS QUE EL VERBO SEA CORRECTO
		return false;
	}
	public Hashtable<String,String> getParameters(){
		return null;
	}
	
	//hacemos el metodo para digest de routes
	public RouteDef digest_route(String[] route){
		if(route.length > 0){
			while(route.length > 0){
				if(route[0].startsWith(":")){
					String param = route[0].substring(1, (route[0].length()));
					String key = "{"+param+"}";
					this.ParamByUri.add(key);
					if(RouteTable.containsKey(key)){
						RouteDef router = this.RouteTable.get(key);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route);
					}
					else {
						RouteDef router = new RouteDef(key);
						this.RouteTable.put(key, router);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route);
					}
				}
				else
				{
					if(RouteTable.containsKey(route[0])){
						RouteDef router = this.RouteTable.get(route[0]);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route);
					}
					else {
						RouteDef router = new RouteDef(route[0]);
						this.RouteTable.put(route[0], router);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route);
					}
				}
			}
			return this;
		}
		else
		{
			return this;
		}
	}
	// Creamos la action para añadir el verbo http
	public ActionDef createAction(int httpAction, Class<?> clase, Method metodo) {
		ActionDef action = new ActionDef(httpAction, this, clase, metodo);
		if(this.actions.containsKey(httpAction)) this.log.Warning("The last route with "+HttpMethod.toString(httpAction)+" method was duplicate. The core set the controller "+clase.getSuperclass().getName()+" with the action "+metodo.getName()+" in this route.");
		this.actions.put(httpAction, action);
		return action;
	}
	
	public ActionDef createAction(int httpAction, Class<?> clase) {
		ActionDef action = new ActionDef(httpAction, this, clase);
		if(this.actions.containsKey(httpAction)) this.log.Warning("The last route with "+HttpMethod.toString(httpAction)+" method was duplicate. The core set the controller "+clase.getSuperclass().getName()+" in this route.");
		this.actions.put(httpAction, action);
		return action;
	}
	
	public ActionDef createAction(int httpAction, Class<?> clase, Method metodo, boolean console) {
		ActionDef action = new ActionDef(httpAction, this, clase, metodo, console);
		if(this.actions.containsKey(httpAction)) this.log.Warning("The last route with "+HttpMethod.toString(httpAction)+" method was duplicate. The core set the controller "+clase.getSuperclass().getName()+" with the action "+metodo.getName()+" in this route.");
		this.actions.put(httpAction, action);
		return action;
	}
	
	public ActionDef createAction(int httpAction, Class<?> clase, boolean console) {
		ActionDef action = new ActionDef(httpAction, this, clase, console);
		if(this.actions.containsKey(httpAction)) this.log.Warning("The last route with "+HttpMethod.toString(httpAction)+" method was duplicate. The core set the controller "+clase.getSuperclass().getName()+" in this route.");
		this.actions.put(httpAction, action);
		return action;
	}
	
	public ActionDef route(String[] route, int httpMethod, JSONObject params){		
		if(params == null) params = new JSONObject();
		Method metodo = null;
		int param_counter = 0;
		if(route.length > 0){
			while(route.length > 0){
				if(RouteTable.containsKey(route[0])){
					RouteDef router = this.RouteTable.get(route[0]);
					route = ArrayUtils.remove(route,0);
					return router.route(route, httpMethod, params);
				}
				else {
					// BUSCAMOS UN METODO DENTRO DE LA CLASE EN CASO DE SER UN ENDPOINT
					// SOLO SI ES EL ULTIMO PARAMETRO DE LLAMADA
					if(this.actions.containsKey(httpMethod)){
						ActionDef action = this.actions.get(httpMethod);
						if(action.EndPoint && route.length == 1){
							Method[] met = action.controlador.getDeclaredMethods();
							for(int k=0; k<met.length;k++){
								if(met[k].getName().equals(route[0])){
									metodo = met[k];
								}
							}
							if(metodo != null) {
								route = ArrayUtils.remove(route,0);
								continue;
							}
						}
					} 
					// EN CASO CONTRARIO INTENTAMOS SABER SI SON PARAMETROS
					if(this.ParamByUri.size() > param_counter){
						String pam = this.ParamByUri.get(param_counter++);
						params.put(pam.substring(1,pam.length()-1), route[0]);
						RouteDef router = this.RouteTable.get(pam);
						route = ArrayUtils.remove(route,0);
						return router.route(route, httpMethod, params);
					}
					else
					{
						return null;
					}
				}
			}
		}
		// EN CASO QUE LA RUTA ENCAJE EN ALGUNA DE LAS PETICIONES ENVIAMOS LA RUTA
		// SI ES UN MODELO REST DE IGUAL FORMA SE EFECTUA LA LLAMADA
		try {
			ActionDef action = this.actions.get(httpMethod);
			if (this.CORS && httpMethod == HttpMethod.OPTIONS) {
				Method[] methods = CORSResponse.class.getMethods();
				for(Method method : methods) {
					if(!method.getName().equals("CORS")) continue;
					ActionDef rt = new ActionDef(HttpMethod.OPTIONS, this, CORSResponse.class, method);
					JSONObject method_allow = new JSONObject();
					if(this.actions.containsKey(HttpMethod.GET)) method_allow.put("GET", "true");
					if(this.actions.containsKey(HttpMethod.POST)) method_allow.put("POST", "true");
					if(this.actions.containsKey(HttpMethod.OPTIONS)) method_allow.put("OPTIONS", "true");
					if(this.actions.containsKey(HttpMethod.PURGE)) method_allow.put("PURGE", "true");
					if(this.actions.containsKey(HttpMethod.PUT)) method_allow.put("PUT", "true");
					if(this.actions.containsKey(HttpMethod.PATCH)) method_allow.put("PATCH", "true");
					if(this.actions.containsKey(HttpMethod.HEAD)) method_allow.put("HEAD", "true");
					if(this.actions.containsKey(HttpMethod.DELETE)) method_allow.put("DELETE", "true");
					rt.setParams(method_allow);
					return rt;
				}
			} else  if((action != null && action.controlador != null && metodo != null) || (action != null && action.controlador != null && action.metodo != null)){
				ActionDef rt = (ActionDef)action.clone();
				if(metodo != null) rt.metodo = metodo;
				rt.setParams(params);
				return rt;
			} else if (action != null && action.ModelRest && action.Model != null) {
				ActionDef rt = (ActionDef)action.clone();
				if(metodo != null) rt.metodo = metodo;
				rt.setParams(params);
				return rt;
			} else {
				return null;
			}
		} catch (CloneNotSupportedException e) {
			this.log.Critic("Error al clonar la propiedad", Error500.ROUTE_OBJECT_ERROR_CLONE);
		}
		return null;
	}
	
	public boolean hasHttpMethod(int method){
		return this.actions.containsKey(method);
	}
	
	public Hashtable<Integer,ActionDef> getActions() {
		return this.actions;
	}
}
