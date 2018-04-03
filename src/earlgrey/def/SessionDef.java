package earlgrey.def;

import java.lang.reflect.Method;
import java.time.Instant;

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
	private JSONObject variables; 
	// VARIABLES TEMPORALES
	private JSONObject temp; 
	// HISTORIAL DE ACTIONS
	private JSONArray history;
	// DECLARAMOS LOS METODOS Y CONSTRUCTORES
	public SessionDef(String iD) {
		super();
		ID = iD;
		this.init_time =  Instant.now().getEpochSecond();
		this.history = new JSONArray();
		this.variables = new JSONObject();
		this.temp = new JSONObject();
	}
	//DECLARAMOS EL METODO QUE OTORGA LA AUTENTIFICACION
	public void setAuth(String user, String[] roles){
		this.authenticated = true;
		this.user = user;
		this.roles = roles;
	}
	// PING para registrar sucesos
	public void ping(Method action){
		this.last_time =  Instant.now().getEpochSecond();
		this.history.put(action.getName());
	}
	public long SessionDiff(long now){
		return now - this.last_time;
	}
	public boolean isAuth(){
		if(this.authenticated){
			return true;
		}
		return false;
	}
}
