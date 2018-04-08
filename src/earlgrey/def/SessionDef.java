package earlgrey.def;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

import oracle.sql.DATE;

public class SessionDef {
	// IP DEL USUARIO
	private String IP;
	// ID DE SESSION
	private String ID;
	// VARIABLES DE AUTENTIFICACION
	private boolean authenticated;
	private boolean admin;
	// GRUPOS DE USUARIOS Y ROLES
	private String user;
	private String[] roles;
	// TIEMPO DE ULTIMA INTERACCIÓN
	private long last_time;
	// TIEMPO INICIO
	private long init_time;
	// REAL TIME VARIABLE
	private boolean real_time;
	// VARIABLES DE USUARIO
	private Hashtable<String,Object> variables; 
	// HISTORIAL DE ACTIONS
	private JSONArray history;
	// DECLARAMOS LOS METODOS Y CONSTRUCTORES
	public SessionDef(String iD) {
		super();
		ID = iD;
		this.init_time =  Instant.now().getEpochSecond();
		this.history = new JSONArray();
		this.variables = new Hashtable<String,Object>();
	}
	//DECLARAMOS EL METODO QUE OTORGA LA AUTENTIFICACION
	public void setAuth(String user, String[] roles){
		this.authenticated = true;
		this.user = user;
		this.roles = roles;
	}
	//DECLARAMOS EL METODO QUE OTORGA LA AUTENTIFICACION
	public void setAdmin(){
		this.admin = true;
	}
	// PING para registrar sucesos
	public void ping(Method action){
		this.last_time =  Instant.now().getEpochSecond();
		this.history.put(action.getName());
	}
	public long SessionDiff(long now){
		return now - this.last_time;
	}
	public void setSessionVar(String key, Object value){
		this.variables.put(key, value);
	}
	public Object getSessionVar(String key){
		if(this.variables.contains(key)){
			return this.variables.get(key);
		}
		return null;
	}
	public boolean isAuth(){
		if(this.authenticated){
			return true;
		}
		return false;
	}
	public boolean isAdmin(){
		if(this.admin){
			return true;
		}
		return false;
	}
}
