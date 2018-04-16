package earlgrey.core;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONObject;

import earlgrey.annotations.AddPropertie;
import earlgrey.annotations.AddPropertieSet;
import earlgrey.database.Connector;
import earlgrey.database.OracleConnector;
import earlgrey.database.PostgresConnector;
import earlgrey.def.Database;
import earlgrey.def.Datasource;
import earlgrey.error.Error800;
import earlgrey.interfaces.PropertiesDepend;
import earlgrey.interfaces.Process;

public class ConnectionPool {
	private static ConnectionPool instance = null;
	private Properties prop;
	private ArrayList<Connector> free_conectors = new ArrayList<Connector>();
	private ArrayList<Connector> connections = new ArrayList<Connector>();
	private Logging log;
	private String datasource;
	private Hashtable<Integer,Class<?>> drivers;
	private Connector driver;
	//CONSTRUCTOR
	public ConnectionPool(String datasource){
		instance = this;
		this.datasource = datasource;
		this.prop = Properties.getInstance();
		this.log = new Logging(this.getClass().getName());
		this.drivers = ResourceMaping.getInstance().getDatabaseDriverTable();
		this.makeConnection();
	}
	private void makeConnection(){
		//LIMPIAMOS LAS CONEXIONES
		this.clearConnections();
		// OBTENEMOS LAS NUEVAS VARIABLES
		Datasource source = this.prop.getDatasource(this.datasource);
		try {
			if(drivers.containsKey(source.TYPE)){
				Class<?> clase = drivers.get(source.TYPE);
				Connector driver = (Connector) clase.newInstance();
				if(!source.HOST.equals("") || !source.USERNAME.equals("") || !source.PASSWORD.equals("") || !source.equals("")) {
					this.log.Info("Cargando Datasource "+this.datasource);
					this.log.Info("Cargando Driver "+Database.toString(source.TYPE));
					if(source.CONTAINER_DATASOURCE){
						driver.setDatasource(source.DATASOURCE);
						if(driver.TestConector()){
							this.driver = driver;
							this.log.Info("Estableciendo pool de conexión en: "+source.MAX_POOL);
							for(int h=0;h<source.MAX_POOL;h++){
								Connector driver_pool = (Connector) clase.newInstance();
								driver_pool.setDatasource(source.DATASOURCE);
								driver_pool.setPool(this);
								this.free_conectors.add(driver_pool);
							}
							return;
						}
						
					} else {
						driver.setCredencial(source.USERNAME, source.PASSWORD, source.SOURCE, source.HOST, source.PORT);
						if(driver.TestConector()){
							this.driver = driver;
							this.log.Info("Estableciendo pool de conexión en: "+source.MAX_POOL);
							for(int h=0;h<source.MAX_POOL;h++){
								Connector driver_pool = (Connector) clase.newInstance();
								driver_pool.setCredencial(source.USERNAME, source.PASSWORD, source.SOURCE, source.HOST, source.PORT);
								driver_pool.setDemand(source.ON_DEMAND);
								driver_pool.setPool(this);
								this.free_conectors.add(driver_pool);
							}
							return;
						}
					}
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
			this.connections.get(index).checkCloseConnection();
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
				conector.checkConnection();
				this.connections.add(conector);
				cone = conector;
			}
			else
			{
				this.log.Info("Not have free connector. Delaying execution.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					this.log.Warning("Having error while delaying connector requirement execution. " + e.getMessage());
				} 
			}
		}
		return cone;
	}
	
	public void restart() {
		this.makeConnection();
	}
}
