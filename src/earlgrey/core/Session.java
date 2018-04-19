package earlgrey.core;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import earlgrey.annotations.AddConfig;
import earlgrey.annotations.AddPropertie;
import earlgrey.def.Datasource;
import earlgrey.def.SessionDef;
import earlgrey.interfaces.Cacheable;
import earlgrey.utils.JWT;
@AddConfig(defaultTo = "1800000", name = "SESSION_TIME", earlgrey_name = "Session time expiration")
public class Session {
	// LOG
	Logging log = new Logging(this.getClass().getName());
	//ADMINISTRADOR DE SESIONES
	private Hashtable<String,SessionDef> sessions = new Hashtable<String,SessionDef>();
	private int timer_time;
	private Datasource tenant;
	private static Session instance;
	//CONSTRUCTOR
	public Session(){
		this.timer_time = Integer.valueOf(Properties.getInstance().getConfig("SESSION_TIME"));
		this.startTimer();
		instance = this;
	}
	// INSTANCIAR LA CLASE DESDE CUALQUIER PUNTO
	public static synchronized Session getInstance(){
		if(instance == null) instance = new Session();
		return instance;
	}
	// RECUPERAR SESSION DEL SISTEMA
	public SessionDef getSession(String id){
		if(this.sessions.containsKey(id)){
			SessionDef session =  this.sessions.get(id);
			return session;
		}
		else
		{
			SessionDef session = new SessionDef(id);
			this.sessions.put(id, session);
			return session;
		}
	}
	// PROCESO QUE EJECUTAR EL CLEANER
	private void sessionCleaner(){
		Enumeration<String> keys = this.sessions.keys();
		int counter = 0;
		while(keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(this.sessions.get(key).SessionDiff() > this.timer_time) {
				this.sessions.remove(key);
				counter++;
			}
		}
		if(counter > 0) {
			this.log.Info(counter+" sessions has been cleaned.");
		}
	}
	
	private void startTimer(){
		Session self = this;
		TimerTask timerTask = new TimerTask() 
	     { 
	         public void run()  
	         { 
	             self.sessionCleaner();
	         } 
	     }; 
	     // PROGRAMAMOS EL TIMER SE SESIONES
	     Timer timer = new Timer(); 
	     // ACTIVAMOS EL SESSION WATCHDOG
	     timer.scheduleAtFixedRate(timerTask, 0, this.timer_time);
	     this.log.Info("Session Watchdog - Online");
	}
	
	public String setTenant(Datasource datasource){
		this.tenant = datasource;
		return JWT.getJWT(new JSONObject());
	}
	
	public void wakeupTenant(String jwt){
		this.tenant = new Datasource(JWT.getJWTPayload(jwt));
	}
}
