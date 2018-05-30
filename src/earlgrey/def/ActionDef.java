package earlgrey.def;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import earlgrey.core.Gear;
import earlgrey.core.ModelCore;

public class ActionDef implements Cloneable{
	// DECLARAMOS LAS CONSTANTES
	protected Class<?> controlador;
	protected Method metodo;
	private boolean console;
	private int httpAction; 
	public ArrayList<String> ParamRequire;
	public Hashtable<String,String> ParamOptional;
	public JSONObject Params;
	protected boolean EndPoint;
	public Class<?> policie;
	public boolean ModelRest = false;
	public Class<ModelCore> Model;
	private RouteDef route; 
	// CONSTRUCTOR
	public ActionDef(int httpAction, RouteDef route, Class<?> clase, Method metodo){
		this.controlador = clase;
		this.metodo = metodo;
		this.httpAction = httpAction;
		this.route = route;
		this.console = false;
	}
	public ActionDef(int httpAction, RouteDef route, Class<?> clase, Method metodo, boolean console){
		this.controlador = clase;
		this.metodo = metodo;
		this.httpAction = httpAction;
		this.route = route;
		this.console = console;
	}
	public ActionDef(int httpAction, RouteDef route, Class<?> clase){
		this.controlador = clase;
		this.httpAction = httpAction;
		this.route = route;
		this.console = false;
	}
	public ActionDef(int httpAction, RouteDef route, Class<?> clase, boolean console){
		this.controlador = clase;
		this.httpAction = httpAction;
		this.route = route;
		this.console = console;
	}
	// METODO PARA OBTENER EL METODO ENRUTADO
	public Method getMetodo() {
		return metodo;
	}
	public Class<?> getController() {
		return controlador;
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
	public boolean getEndpoint(){
		return this.EndPoint;
	}
	public RouteDef getRoute(){
		return this.route;
	}
	public boolean isConsole() {
		return this.console;
	}
}
