package geos.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.Instant;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import geos.error.Error500;
import geos.error.Error60;
import geos.error.Error700;
import oracle.sql.DATE;

public class Properties {
	private static Properties instance = null;
	private Logging log;
	private File config;
	private JSONObject config_obj;
	private JSONObject target;
	ResourceMaping propertiesMap;
	//CONSTRUCTOR
	public static synchronized Properties getInstance(){
		if(instance == null) instance = new Properties();
		return instance;
	}
	public Properties(){
		if(instance == null) instance = this;
		this.log = new Logging(this.getClass().getName());
		this.propertiesMap = ResourceMaping.getInstance();
		this.setFile();
	}
	private void setFile(){
		this.config = new File("kernel/properties/config.properties");
		// EN CASO DE QUE NO EXISTA DEBE SER CREADO
		log.Info("Cargando Archivo de configuraciones de properties");
		try {
			if(!this.config.exists()){
				this.config.createNewFile();
				this.setDefault();
			}
			else
			{
				log.Info("Verificando integridad de archivo de configuraciones");
				FileReader fr = new FileReader(this.config);
				BufferedReader br = new BufferedReader(fr);
				String linea = br.readLine();
				if(linea != null){
					try {
						JSONObject conf = new JSONObject(linea);
						this.config_obj = conf;
						// VERIFICAMOS MODIFICACIONES
						this.checkPropertieFileAndFix();
				    } catch (JSONException ex) {
				    	this.log.Critic("El archivo de configuraciones esta corrupto, verifique su confguracion", Error700.FILE_DAMAGE_ERROR);
				    }
				}
				else{
					this.log.Critic("El archivo de configuraciones esta vacio, restaurando la copia", Error700.FILE_EMPTY_ERROR);
					this.setDefault();
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.Info("Archivo de configuración de properties, cargado.");
		this.selectEnv();
	}
	private void setDefault() throws IOException{
		JSONObject config = new JSONObject();
		// ESTABLECEMOS LOS AMBIENTES DE TRABAJO
		JSONArray envi = new JSONArray();
		envi.put(this.getTemplate("DEVELOP", this.propertiesMap.getPropertieTable()));
		envi.put(this.getTemplate("TESTING", this.propertiesMap.getPropertieTable()));
		envi.put(this.getTemplate("PRODUCTION", this.propertiesMap.getPropertieTable()));
		//ASIGANAMOS LAS DEFAULT PROPERTIES
		config.put("env_used", "DEVELOP");
		config.put("environment", envi);
		config.put("routes", new JSONArray());
		config.put("controllers", new JSONArray());
		config.put("models", new JSONArray());
		config.put("comunication", new JSONObject());
		config.put("policies", new JSONArray());
		//config.put("propertie_map", db_template);
		JSONObject users = (new JSONObject()).put("USERNAME", "ADMIN").put("PASSWORD", "ADMIN");
		config.put("console",(new JSONObject()).put("users", (new JSONArray()).put(users)));
		FileWriter fichero = new FileWriter(this.config,true);
		PrintWriter pw = new PrintWriter(fichero);
		pw.println(config.toString());
		pw.close();
		fichero.close();
		this.config_obj = config;
		this.log.Info("ARCHIVO DE CONFIGURACIÓN CREADO");
	}
	private JSONObject getTemplate(String envname, Hashtable<String,JSONObject> properties){
		JSONObject config_master = new JSONObject();
		config_master.put("DYNAMIC", new JSONObject());
		config_master.put("GEOS_ENVNAME", envname);
		JSONObject config = new JSONObject();
		Enumeration<String> keys = properties.keys();
		while(keys.hasMoreElements()){
			String propName = keys.nextElement();
			JSONObject prop = properties.get(propName);
			if(prop.get("type").equals("single")){
				prop.put("value", prop.getString("default"));
				config.put(propName, prop);
			}
			else if(prop.get("type").equals("set")){
				// CARACTERISTICA AUN NO LISTA
				// PENSANDO EN NUEVO REALEASE
				// PERO NO SE DESCARTA EL ELIMINARLA
				JSONObject template = prop.getJSONObject("set");
				/*JSONObject set_template = new JSONObject();
				Iterator<String> llaves = template.keys();
				while(llaves.hasNext()){
					String llave = llaves.next();
					String value = template.getString(llave);
					// BUSCAMOS UNA ANNOTATION QUERY
					try{
						JSONObject object = new JSONObject(value);
						if(object.has("type") && object.has("anottation") && object.has("anottation")){
							
						}
						else{
							this.log.Warning("el object propertie no se encuentra bien estructurado", Error60.PROPERTIE_OBJECT_INCORRECT);
							JSONObject obj = new JSONObject();
							obj.put("type", "single");
							obj.put("default", value);
							obj.put("value", value);
							set_template.put(llave, obj);
						}
					}
					catch(JSONException e){
						JSONObject obj = new JSONObject();
						obj.put("type", "single");
						obj.put("default", value);
						obj.put("value", value);
						set_template.put(llave, obj);
					}
				}*/
				JSONArray sets = new JSONArray();
				sets.put(template);
				prop.put("sets", sets);
				prop.put("value", 0);
				config.put(propName, prop);
			}
			else if(prop.get("type").equals("option")){
				prop.put("value", prop.getInt("default"));
				config.put(propName, prop);
			}
			else if(prop.get("type").equals("array")){
				System.out.println(prop.getJSONArray("default"));
				prop.put("value", prop.getJSONArray("default"));
				config.put(propName, prop);
			}
		}
		config_master.put("STATIC", config);
		return config_master;
	}
	private String[] search_annotation(){
		
		return null;
	}
	private void checkPropertieFileAndFix(){
		this.log.Info("Analizando archivo de configuraciones");
		JSONObject prop_save = this.config_obj;
		Hashtable<String,JSONObject> propiedades = this.propertiesMap.getPropertieTable();
		int hash = prop_save.toString().hashCode();
		JSONArray environments = prop_save.getJSONArray("environment");
		for(int i=0;i<environments.length();i++){
			JSONObject env = this.checkEnv(environments.getJSONObject(i), propiedades);
			environments.put(i,env);
		}
		prop_save.put("environment", environments);
		this.config_obj = prop_save;
		this.log.Info("Verificando cambios en archivos de configuraciones");
		if(hash == prop_save.toString().hashCode()){
			this.log.Info("No existen cambios, cargando archivo");
			return;
		}
		else
		{
			this.log.Info("Existen cambios respecto a la version antigua. respaldando.");
			this.backupFile();
			this.log.Info("Guardando nuevo archivo de configuraciones.");
			this.saveFile();
		}
		
	}
	private JSONObject checkEnv(JSONObject env, Hashtable<String,JSONObject> props){
		JSONObject conf_global = new JSONObject();
		conf_global.put("GEOS_ENVNAME", env.getString("GEOS_ENVNAME"));
		if(env.has("DYNAMIC")){
			conf_global.put("DYNAMIC", env.getJSONObject("DYNAMIC"));
		}
		else
		{
			conf_global.put("DYNAMIC", new JSONObject());
		}
		JSONObject conf = new JSONObject();
		Enumeration<String> keys = props.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			if(env.has(key)){
				JSONObject prop = env.getJSONObject(key);
				JSONObject new_prop = props.get(key);
				if(new_prop.get("type").equals("single")){
					//COMPARAMOS Y MODIFICAMOS EN CASO SE SERLO
					if(prop.has("value")){
						new_prop.put("value", prop.getString("value"));
					}
					else
					{
						new_prop.put("value", new_prop.getString("default"));
					}
					conf.put(key, new_prop);
				}
				else if(new_prop.get("type").equals("set")){
					if(!prop.getString("type").equals("set")) prop.put("type", "set");
					// VERIFICAMOS EL SET QUE POSEA TODAS LAS CARACTETISTICAS.
					JSONArray new_sets = new JSONArray();
					JSONArray sets = prop.getJSONArray("sets");
					for(int g=0; g<sets.length();g++){
						JSONObject new_set = new JSONObject();
						JSONObject old_set = sets.getJSONObject(g);
						JSONObject set = new_prop.getJSONObject("set");
						Iterator<String> llaves = set.keys();
						while(llaves.hasNext()){
							String keyName = llaves.next();
							if(old_set.has(keyName)){
								new_set.put(keyName, old_set.getString(keyName));
							}
							else
							{
								new_set.put(keyName, set.getString(keyName));
							}
						}
						new_sets.put(new_set);
					}
					prop.put("sets", new_sets);
					if(prop.has("value") && new_sets.length() <= prop.getInt("value")){
						prop.put("value", 0);
					}
					conf.put(key, prop);
				}
				else if(new_prop.get("type").equals("option")){
					JSONArray old_option = prop.getJSONArray("options");
					String option = old_option.getString(prop.getInt("default"));
					for(int g=0; g<new_prop.getJSONArray("options").length(); g++){
						if(option.equals(new_prop.getJSONArray("options").getString(g))) new_prop.put("value", g);
					}
					new_prop.put("default", prop.getInt("default"));
					conf.put(key, new_prop);
				}
				else if(new_prop.get("type").equals("array")){
					new_prop.put("default", prop.getJSONArray("default"));
					new_prop.put("value", prop.getJSONArray("value"));
					conf.put(key, new_prop);
				}
			}
			else
			{
				//EN CASO DE NO EXISTIR LAS PROPIEDADES LAS CREAMOS
				JSONObject prop = props.get(key);
				if(prop.get("type").equals("single")){
					prop.put("value", prop.getString("default"));
					conf.put(key, prop);
				}
				else if(prop.get("type").equals("set")){
					JSONArray sets = new JSONArray();
					sets.put(prop.getJSONObject("set"));
					prop.put("sets", sets);
					prop.put("default", 0);
					prop.put("value", 0);
					conf.put(key, prop);
				}
				else if(prop.get("type").equals("option")){
					prop.put("value", prop.getInt("default"));
					conf.put(key, prop);
				}
				else if(prop.get("type").equals("array")){
					prop.put("value", prop.getJSONArray("default"));
					conf.put(key, prop);
				}
			}
		}
		conf_global.put("STATIC", conf);
		return conf_global;
	}
	private void saveFile(){
		FileWriter fichero;
		try {
			fichero = new FileWriter(this.config);
			PrintWriter pw = new PrintWriter(fichero);
			pw.println(config_obj.toString());
			pw.close();
		} catch (IOException e) {
			this.log.Critic("", Error700.FILE_SAVE_ERROR);
		}
	}
	private void backupFile(){
		FileReader fr;
		try {
			fr = new FileReader(this.config);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			if(linea != null){
				try {
					File back = new File("kernel/backups/config.properties."+Instant.now());
					FileWriter fichero;
					fichero = new FileWriter(back);
					PrintWriter pw = new PrintWriter(fichero);
					pw.println(linea);
					pw.close();
			    } catch (JSONException ex) {
			    	this.log.Critic("No se puede escribir el archivo Backup. Verifique los permisos y el sistema de archivos kernel", Error700.FILE_SAVE_ERROR);
			    }
			}
			else{
				this.log.Critic("El archivo antiguo de configuracion esta vacio. Abortando Backup", Error700.FILE_EMPTY_ERROR);
				this.setDefault();
			}
		} catch (FileNotFoundException e1) {
			this.log.Critic("El archivo de configuraciones no existe.", 2);
		} catch (IOException e) {
			this.log.Critic("Error IO, al intentar obtener el archivo de configuraciones. (Backup)",Error700.FILE_READ_ERROR);
		}
	}
	private void selectEnv() {
		String prop = this.config_obj.getString("env_used");
		JSONArray envs = this.config_obj.getJSONArray("environment");
		for(int i=0; i< envs.length(); i++){
			JSONObject env = envs.getJSONObject(i);
			if(env.getString("GEOS_ENVNAME").equals(prop)){
				this.joinProperties(env);
				this.log.Info("Funcionando en entorno, "+prop);
				return;
			}
		}
		this.log.Critic("No se cargo ningun entorno de propiedades", Error500.PROPERTIES_ENV);
	}
	public JSONObject getProperties(){
		return this.target;
	}
	public String getPropertie(String propname){
		if(this.target.has(propname)){
			if(this.target.getJSONObject(propname).getString("type").equals("single")){
				return this.target.getJSONObject(propname).getString("value");
			}
			else{
				this.log.Warning("La propertie ("+propname+") especificada no corresponde al tipo llamado. Tipo declarado "+this.target.getJSONObject(propname).getString("type"), Error60.PROPERTIE_TYPE_INCORRECT);
			}
		}
		else
		{
			this.log.Warning("La propertie ("+propname+")especificada no existe, se envia valor null", Error60.PROPERTIE_NOT_SET);
			
		}
		return null;
	}
	public JSONObject getPropertieSet(String propname){
		if(this.target.has(propname)){
			if(this.target.getJSONObject(propname).getString("type").equals("set")){
				int selected = this.target.getJSONObject(propname).getInt("default");
				return this.target.getJSONObject(propname).getJSONArray("sets").getJSONObject(selected);
			}
			else{
				this.log.Warning("La propertie ("+propname+") especificada no corresponde al tipo llamado. Tipo declarado "+this.target.getJSONObject(propname).getString("type"), Error60.PROPERTIE_TYPE_INCORRECT);
			}
		}
		else
		{
			this.log.Warning("La propertie ("+propname+")especificada no existe, se envia valor null", Error60.PROPERTIE_NOT_SET);
			
		}
		return null;
	}
	public String getPropertieOption(String propname){
		if(this.target.has(propname)){
			if(this.target.getJSONObject(propname).getString("type").equals("option")){
				return this.target.getJSONObject(propname).getJSONArray("options").getString(this.target.getJSONObject(propname).getInt("value"));
			}
			else{
				this.log.Warning("La propertie ("+propname+") especificada no corresponde al tipo llamado. Tipo declarado "+this.target.getJSONObject(propname).getString("type"), Error60.PROPERTIE_TYPE_INCORRECT);
			}
		}
		else
		{
			this.log.Warning("La propertie ("+propname+")especificada no existe, se envia valor null", Error60.PROPERTIE_NOT_SET);
			
		}
		return null;
	}
	public JSONArray getPropertieArray(String propname){
		if(this.target.has(propname)){
			return this.target.getJSONObject(propname).getJSONArray("value");
		}
		else
		{
			this.log.Warning("La propertie ("+propname+")especificada no existe, se envia valor null", Error60.PROPERTIE_NOT_SET);
			return null;
		}
	}
	private void joinProperties(JSONObject env){
		// CREAMOS EL TARGET
		this.target = new JSONObject();
		// UNIMOS LAS PROPIEDADES ESTATICAS
		JSONObject estaticas = env.getJSONObject("STATIC");
		Iterator<String> est_keys = estaticas.keys();
		while(est_keys.hasNext()){
			String llave  = est_keys.next();
			this.target.put(llave, estaticas.getJSONObject(llave));
		}
		// UNIMOS LAS PROPIEDADES DINAMICAS
		JSONObject dinamicas = env.getJSONObject("DYNAMIC");
		Iterator<String> din_keys = dinamicas.keys();
		while(din_keys.hasNext()){
			String llave  = din_keys.next();
			this.target.put(llave, dinamicas.getJSONObject(llave));
		}
	}
}