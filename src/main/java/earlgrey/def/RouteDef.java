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
import earlgrey.core.Logging;
import earlgrey.core.ModelCore;
import earlgrey.error.Error500;
import earlgrey.interfaces.PolicieCore;

public class RouteDef implements Cloneable {
	protected String path;
	protected Class<?> controlador;
	protected Method metodo;
	public ArrayList<String> ParamRequire;
	public Hashtable<String,String> ParamOptional;
	protected ArrayList<String> ParamByUri;
	public JSONObject Params;
	protected boolean EndPoint;
	public Class<?> policie;
	public boolean CACHE;
	public boolean POST;
	public boolean GET;
	public boolean PUT;
	public boolean DELETE;
	public boolean ModelRest = false;
	public Class<ModelCore> Model;
	protected Logging log = new Logging(this.getClass().getName());
	// CARGAMOS LA TABLA DE RUTAS
	private Hashtable<String,RouteDef> RouteTable = new Hashtable<String,RouteDef>();
	//DEFINIMOS LOS CONSTRUCTORES
	public RouteDef(String path, Class<?> controlador){
		this.path = path;
		this.controlador = controlador;
		this.ParamByUri = new ArrayList<String>();
	}
	public RouteDef(String path){
		this.path = path;
		this.ParamByUri = new ArrayList<String>();
	}
	public RouteDef(String path, Class<?> controlador, Method metodo){
		this.path = path;
		this.controlador = controlador;
		this.metodo = metodo;
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
	// METODO PARA OBTENER EL METODO ENRUTADO
	public Method getMetodo() {
		return metodo;
	}
	//hacemos el metodo para digest de routes
	public RouteDef digest_route(String[] route, Class<?> clase){
		if(route.length > 0){
			while(route.length > 0){
				if(route[0].startsWith(":")){
					String param = route[0].substring(1, (route[0].length()));
					String key = "{"+param+"}";
					this.ParamByUri.add(key);
					if(RouteTable.containsKey(key)){
						RouteDef router = this.RouteTable.get(key);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route,clase);
					}
					else {
						RouteDef router = new RouteDef(key);
						this.RouteTable.put(key, router);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route, clase);
					}
				}
				else
				{
					if(RouteTable.containsKey(route[0])){
						RouteDef router = this.RouteTable.get(route[0]);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route,clase);
					}
					else {
						RouteDef router = new RouteDef(route[0]);
						this.RouteTable.put(route[0], router);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route, clase);
					}
				}
			}
			return this;
		}
		else
		{
			this.controlador = clase;
			this.EndPoint = true;
			return this;
		}
	}
	public RouteDef digest_route(String[] route, Class<?> clase, Method metodo) {
		if(route.length > 0){
			while(route.length > 0){
				if(route[0].startsWith(":")){
					String param = route[0].substring(1, (route[0].length()));
					String key = "{"+param+"}";
					this.ParamByUri.add(key);
					if(RouteTable.containsKey(key)){
						RouteDef router = this.RouteTable.get(key);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route,clase, metodo);
					}
					else {
						RouteDef router = new RouteDef(key);
						this.RouteTable.put(key, router);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route, clase, metodo);
					}
				}
				else
				{
					if(RouteTable.containsKey(route[0])){
						RouteDef router = this.RouteTable.get(route[0]);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route,clase, metodo);
					}
					else {
						RouteDef router = new RouteDef(route[0]);
						this.RouteTable.put(route[0], router);
						route = ArrayUtils.remove(route,0);
						return router.digest_route(route, clase, metodo);
					}
				}
			}
			return this;
		}
		else
		{
			this.controlador = clase;
			this.metodo = metodo;
			return this;
		}
	}
	public void setParams(JSONObject params){
		this.Params = params;
	}
	public JSONObject getParams(){
		return this.Params;
	}
	protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	public RouteDef route(String[] route, JSONObject params){		
		if(params == null) params = new JSONObject();
		Method metodo = null;
		int param_counter = 0;
		if(route.length > 0){
			while(route.length > 0){
				if(RouteTable.containsKey(route[0])){
					RouteDef router = this.RouteTable.get(route[0]);
					route = ArrayUtils.remove(route,0);
					return router.route(route, params);
				}
				else {
					// BUSCAMOS UN METODO DENTRO DE LA CLASE EN CASO DE SER UN ENDPOINT
					// SOLO SI ES EL ULTIMO PARAMETRO DE LLAMADA
					if(this.EndPoint && route.length == 1){
						Method[] met = this.controlador.getDeclaredMethods();
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
					// EN CASO CONTRARIO INTENTAMOS SABER SI SON PARAMETROS
					if(this.ParamByUri.size() > param_counter){
						String pam = this.ParamByUri.get(param_counter++);
						params.put(pam.substring(1,pam.length()-1), route[0]);
						RouteDef router = this.RouteTable.get(pam);
						route = ArrayUtils.remove(route,0);
						return router.route(route, params);
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
			if((this.controlador != null && metodo != null) || (this.controlador != null && this.metodo != null) || (this.ModelRest && this.Model != null)){
				RouteDef rt = (RouteDef)this.clone();
				if(metodo != null) rt.metodo = metodo;
				rt.setParams(params);
				return rt;
			}
			else{
				return null;
			}
		} catch (CloneNotSupportedException e) {
			this.log.Critic("Error al clonar la propiedad", Error500.ROUTE_OBJECT_ERROR_CLONE);
		}
		return null;
	}
	public boolean getEndpoint(){
		return this.EndPoint;
	}
}
