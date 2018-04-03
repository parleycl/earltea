package earlgrey.core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.json.JSONObject;

import earlgrey.annotations.AddPropertie;
import earlgrey.annotations.AddPropertieSet;
import earlgrey.annotations.AddPropertieSetTemplate;
import earlgrey.annotations.Model;
import earlgrey.database.Connector;
import earlgrey.database.OracleConnector;
import earlgrey.database.PostgresConnector;
import earlgrey.error.Error800;
import earlgrey.interfaces.PropertiesDepend;
import earlgrey.interfaces.Process;

@AddPropertieSetTemplate(name = "DB_CONFIG", 
set = { "DB_TYPE",
		"DB_HOST",
		"DB_USERNAME", 
		"DB_PASSWORD", 
		"DB_SOURCE", 
		"DB_PORT", 
		"DB_MAX_POOL"}, 
defaultTo = { 
		"Oracle",
		"172.30.21.174",
		"GEOCGR",
		"iOda7Piz",
		"CGR",
		"1541",
		"5"
})
public class DatasourceManager implements Process, PropertiesDepend {
	private Hashtable<String,ConnectionPool> sources;
	private static DatasourceManager instance = null;
	private Logging log;
	private Properties prop;
	//CONSTRUCTOR
	public static synchronized DatasourceManager getInstance(){
		if(instance == null) instance = new DatasourceManager();
		return instance;
	}
	public DatasourceManager(){
		instance = this;
		this.log = new Logging(this.getClass().getName());
		this.log.Info("Inciando Datasource Manager");
		this.sources = new Hashtable<String,ConnectionPool>();
		Engine.getInstance().registerTask(this);
		this.prop = Properties.getInstance();
		this.makeDataSources();
	}
	private void makeDataSources(){
		/*CREAMOS EL DATASOURCE POR DEFECTO*/
		this.registerConnection("DEFAULTDB");
		this.sources.put("DEFAULTDB", new ConnectionPool("DEFAULTDB"));
		/*ITERAMOS EL RESTO DE LOS ELEMENTOS*/
		Hashtable<String, Class<?>> model = ResourceMaping.getInstance().getModelTable();
		Enumeration<String> keys = model.keys();
		while(keys.hasMoreElements()){
			String llave = keys.nextElement();
			Class<?> modelo = model.get(llave);
			Model model_info = modelo.getAnnotation(Model.class);
			if(!this.sources.containsKey(model_info.datasource())){
				this.registerConnection(model_info.datasource());
				this.sources.put(model_info.datasource(), new ConnectionPool(model_info.datasource()));
			}
		}
	}
	private void registerConnection(String datasource){
		// CREAMOS EL DATASOURCE POR DEFECTO
		// PARA ESTO CREAMOS EL SET DE PROPERTIES QUE LO MANEJA
		this.prop.createOrSetPropertieTemplate("DB_CONFIG", datasource);
	}
	public ConnectionPool getConnection(String datasource){
		return this.sources.get(datasource);
	}
	@Override
	public void propertiesRestart() {
		// TODO Auto-generated method stub
	}
}
