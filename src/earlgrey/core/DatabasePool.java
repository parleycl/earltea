package earlgrey.core;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONObject;

import earlgrey.annotations.AddPropertie;
import earlgrey.annotations.AddPropertieSet;
import earlgrey.database.Connector;
import earlgrey.database.OracleConnector;
import earlgrey.database.PostgresConnector;
import earlgrey.error.Error800;
import earlgrey.interfaces.PropertiesDepend;
import earlgrey.interfaces.Process;

@AddPropertieSet(name = "DB_CONFIG", 
set = { "DB_TYPE",
		"DB_HOST",
		"DB_USERNAME", 
		"DB_PASSWORD", 
		"DB_SOURCE", 
		"DB_PORT", 
		"DB_MAX_POOL"}, 
defaultTo = { 
		"Oracle",
		"",
		"",
		"",
		"",
		"",
		"5"
})
public class DatabasePool implements Process, PropertiesDepend {
	private static DatabasePool instance = null;
	private Properties prop;
	private JSONObject config;
	private ArrayList<Connector> free_conectors = new ArrayList<Connector>();
	private ArrayList<Connector> connections = new ArrayList<Connector>();
	private Logging log;
	private Hashtable<String,Class<?>> drivers;
	private Connector driver;
	//CONSTRUCTOR
	public static synchronized DatabasePool getInstance(){
		if(instance == null) instance = new DatabasePool();
		return instance;
	}
	public DatabasePool(){
		instance = this;
		Engine.getInstance().registerTask(this);
		this.prop = Properties.getInstance();
		this.log = new Logging(this.getClass().getName());
		this.drivers = ResourceMaping.getInstance().getDatabaseDriverTable();
		this.makeConnection();
	}
	private void makeConnection(){
		//LIMPIAMOS LAS CONEXIONES
		this.clearConnections();
		// OBTENEMOS LAS NUEVAS VARIABLES
		this.config = this.prop.getPropertieSet("DB_CONFIG");
		String host = this.config.getString("DB_HOST");
		String username = this.config.getString("DB_USERNAME");
		String password = this.config.getString("DB_PASSWORD");
		String source = this.config.getString("DB_SOURCE");
		String port = this.config.getString("DB_PORT");
		int pool = Integer.valueOf(this.config.getString("DB_MAX_POOL"));
		try {
			String type = this.config.getString("DB_TYPE");
			if(drivers.containsKey(type)){
				Class<?> clase = drivers.get(type);
				Connector driver = (Connector) clase.newInstance();
				this.log.Info("Cargando Driver "+type);
				driver.setCredencial(username, password, source, host, port);
				if(driver.TestConector()){
					this.driver = driver;
					this.log.Info("Estableciendo pool de conexión en: "+pool);
					for(int h=0;h<pool;h++){
						Connector driver_pool = (Connector) clase.newInstance();
						driver_pool.setCredencial(username, password, source, host, port);
						driver_pool.setPool(this);
						this.free_conectors.add(driver_pool);
					}
					return;
				}
			}
			// SI NO SE ACOGE A NIGUNO DE LOS DRIVERS ESTABLECIDOS SE INICIA GEOS SIN DRIVER PERSISTENTE.
			// NO SE PODRAN EJECUTAR CONSULTAR NI TRABAJO CON MODELO DE DATOS.
			this.log.Warning("No se ha cargado ningun driver en memoria",Error800.DATABASE_NO_DRIVER);
			this.driver = null;
			return;
		} catch (InstantiationException | IllegalAccessException e) {
			this.log.Critic("Existio un error al crear una instancia del driver de base de datos. Asegurese que el constructor no recibe parametros", Error800.DATABASE_CONNECT_ERROR);
		}
	}
	public void closeConnection(Connector conection){
		int index = this.connections.indexOf(conection);
		if(index != -1){
			this.free_conectors.add(this.connections.get(index));
			this.connections.remove(index);
		}
	}
	private void clearConnections(){
		if(this.connections != null){
			while(this.connections.size() > 0){
				Connector con = this.connections.get(0);
				this.connections.get(0).close();
				int index = this.connections.indexOf(con);
				if(index != -1) this.connections.remove(index);
			}
		}
		this.connections = new ArrayList<Connector>();
	}
	public synchronized Connector getConector(){
		Connector cone = null;
		int watchdog = 0;
		while(cone == null && watchdog++ < 60){
			if(this.free_conectors.size() > 0){
				Connector conector = this.free_conectors.get(0);
				this.free_conectors.remove(0);
				conector.connect();
				this.connections.add(conector);
				cone = conector;
			}
			else
			{
				this.log.Info("No hay conectores disponbiles, Retardandno ejecucion.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
		return cone;
	}
	@Override
	public void propertiesRestart() {
		// TODO Auto-generated method stub
		this.makeConnection();
	}
}
