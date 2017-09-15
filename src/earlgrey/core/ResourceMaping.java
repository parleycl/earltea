package earlgrey.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import earlgrey.annotations.AddPropertie;
import earlgrey.annotations.AddPropertieArray;
import earlgrey.annotations.AddPropertieOption;
import earlgrey.annotations.AddPropertieSet;
import earlgrey.annotations.Console;
import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.DatabaseDriver;
import earlgrey.annotations.ErrorCode;
import earlgrey.annotations.Model;
import earlgrey.annotations.Policie;
import earlgrey.annotations.Route;
import earlgrey.database.Connector;
import earlgrey.def.ErrorDef;
import earlgrey.def.ModelRest;
import earlgrey.def.RouteDef;
import earlgrey.error.Error500;
import earlgrey.error.ErrorBase;
import earlgrey.interfaces.PolicieCore;

public class ResourceMaping {
	// DECLARAMOS LA INSANCIA ESTTAICA
	private static ResourceMaping instance = null;
	// DEFINIMOS EL LOADER DE JARS
	private ClassLoader classloader;
	// LISTA DE PACKAGES Y CLASES DISPONIBLES
	private ArrayList<String> packages;
	private ArrayList<String> jar;
	private ArrayList<String> clases;
	// CARGAMOS LOS CODIGOS DE ERROR
	private Hashtable<String,ErrorDef> ErrorTable = new Hashtable<String,ErrorDef>();
	// CARGAMOS LA TABLA DE RUTAS
	private Hashtable<String,RouteDef> RouteMap = new Hashtable<String,RouteDef>();
	// CARGAMOS LA TABLA DE RUTAS
	private Hashtable<String,RouteDef> RouteTable = new Hashtable<String,RouteDef>();
	// CARGAMOS LA TABLA DE MODELOS
	private Hashtable<String,Class<?>> ModelTable = new Hashtable<String,Class<?>>();
	// CARGAMOS LA TABLA DE CONTROLADORES
	private Hashtable<String,Class<?>> ControllerTable = new Hashtable<String,Class<?>>();
	// CARGAMOS LA TABLA DE PROPERTIES
	private Hashtable<String,JSONObject> PropertieTable = new Hashtable<String,JSONObject>();
	// CARGAMOS LA TABLA DE
	private Hashtable<String,Class<?>> ConsoleTable = new Hashtable<String,Class<?>>();
	// CARGAMOS LOS CODIGOS DE ERROR
	private Hashtable<String,Class<?>> PolicieTable = new Hashtable<String,Class<?>>();
	// CARGAMOS LOS DRIVERS DE BASE DE DATOS
	private Hashtable<String,Class<?>> databaseDriverTable = new Hashtable<String,Class<?>>();
	// LOGGING MAP
	private Logging log = new Logging(this.getClass().getName());
	//CONSTRUCTOR
	public static synchronized ResourceMaping getInstance(){
		return instance;
	}
	//CONSTRUCTOR
	public ResourceMaping(String packages){
		instance = this;
		this.MapError();
		this.MapPackages(packages);
		this.MapJar();
		this.loadJAR();
		this.MapClases();
		this.MapProperties();
		this.MapPolicies();
		this.MapConsole();
		this.MapControllers();
		this.MapModels();
		this.search_database_drivers();
	}
	// MAPEO DE LOS ERRORES
	private void MapError(){
		log.Info("Mapeo de Errores: Iniciando mapeo de errores");
		String packageName = "earlgrey.error";
		URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
		// Filter .class files.
		File[] files = new File(root.getFile()).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".class");
		    }
		});
		// Find classes implementing ICommand.
		for (File file : files) {
		    String className = file.getName().replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className);
			    if (ErrorBase.class.isAssignableFrom(cls)) {
			       Field[] campos = cls.getFields();
			       for(Field campo: campos){
			    	   ErrorCode error = campo.getAnnotation(ErrorCode.class);
			    	   ErrorDef reso = new ErrorDef(cls,error.code(),error.description());
			    	   this.ErrorTable.put(String.valueOf(error.code()), reso);
			       }
			    }
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de Errores: Proceso concluido con exito");
	}
	// MAPEO DE LOS CONTROLADOES
	private void MapControllers(){
		log.Info("Mapeo de Controladores: Iniciando mapeo de controladores y rutas");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				if (ControllerCore.class.isAssignableFrom(cls)) {
				    //MAPEAMOS QUE SEA UN CONTROLADOR HABILITADO
					Controller controlador = cls.getAnnotation(Controller.class);
					if(controlador != null){
						this.ControllerTable.put(controlador.name(), cls);
						// BUSCAMOS UNA POSIBLE RUTA
						Route ruta = cls.getAnnotation(Route.class);
						if(ruta != null){
							String ruta_uri = ruta.route().trim();
							if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
							if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
							this.log.Info("Enrutando Controlador: "+ruta.route());
							RouteDef router = this.MapRoute(ruta_uri, cls);
							router.DELETE = controlador.DELETE();
							router.GET = controlador.GET();
							router.POST = controlador.POST();
							router.PUT = controlador.PUT();
							//BUSCAMOS POLICIES
							Policie policie = cls.getAnnotation(Policie.class);
							if(policie != null){
								Class<?> policie_class = this.PolicieTable.get(policie.name());
								if(policie_class != null){
									router.policie = policie_class;
								}
								else{
									this.log.Warning("Policie especificada ("+policie.name()+") no existe en el registro de policies.", Error500.POLICIE_NOT_LOADED);
								}
							}
						}
						else
						{
							this.log.Warning("El controlador no posee una ruta valida. Se ha declarado pero no es ruteable.", Error500.ROUTE_NOT_DECLARED);
						}
						//BUSCAMOS ACTIONS AL INTERIOR
						Method[] metodos = cls.getDeclaredMethods();
						for(int g=0;g<metodos.length;g++){
							//VERIFICAMOS SIN SON CONTROLLER ACTIONS
							ControllerAction action = metodos[g].getAnnotation(ControllerAction.class);
							if(action != null){
								//VERIFICAMOS SI TIENE UNA RUTA ASIGNADA
								Route rutaAction = metodos[g].getAnnotation(Route.class);
								if(rutaAction != null){
									String ruta_uri;
									if(rutaAction.ignorePath()){
										ruta_uri = rutaAction.route().trim();
										if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
										if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
									}
									else
									{
										ruta_uri = ruta.route().trim();
										ruta_uri = ruta_uri.replaceAll(":.*[/]?", "");
										if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
										if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
										String ruta_uri_action = rutaAction.route();
										if(ruta_uri_action.endsWith("/")) ruta_uri_action = ruta_uri_action.substring(0, (ruta_uri_action.length()-1));
										if(ruta_uri_action.startsWith("/")) ruta_uri_action = ruta_uri_action.substring(1);
										ruta_uri = ruta_uri + "/" + ruta_uri_action;
									}
									this.log.Info("Enrutando Controller Action: "+rutaAction.route());
									RouteDef router = this.MapRoute(ruta_uri, cls, metodos[g]);
									router.DELETE = action.DELETE();
									router.GET = action.GET();
									router.POST = action.POST();
									router.PUT = action.PUT();
									//BUSCAMOS POLICIES
									Policie policie = metodos[g].getAnnotation(Policie.class);
									if(policie != null){
										Class<?> policie_class = this.PolicieTable.get(policie.name());
										if(policie_class != null){
											router.policie = policie_class;
										}
										else{
											this.log.Warning("Policie especificada ("+policie.name()+") no existe en el registro de policies.", Error500.POLICIE_NOT_LOADED);
										}
									}
								}
								else
								{
									this.log.Warning("La acción del controlador no posee una ruta valida. Se ha declarado pero no es ruteable.", Error500.ROUTE_NOT_DECLARED);
								}
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de Controladores: Proceso Finalizado");
	}
	// MAPEAMOS UNA RUTA
	private RouteDef MapRoute(String ruta, Class<?> clase){
		this.RouteTable.put(ruta, (new RouteDef(ruta,clase)));
		String[] rutas = ruta.split("/");
		if(rutas.length > 0){
			if(RouteMap.containsKey(rutas[0])){
				RouteDef router = this.RouteMap.get(rutas[0]);
				rutas = ArrayUtils.remove(rutas,0);
				return router.digest_route(rutas,clase);
			}
			else {
				RouteDef router = new RouteDef(rutas[0]);
				this.RouteMap.put(rutas[0], router);
				rutas = ArrayUtils.remove(rutas,0);
				return router.digest_route(rutas, clase);
			}
		}
		else
		{
			log.Info("Ruta "+ruta+" incorrecta. Favor corregir");
			return null;
		}
	}
	private RouteDef MapRoute(String ruta, Class<?> clase, Method metodo){
		this.RouteTable.put(ruta, (new RouteDef(ruta,clase,metodo)));
		String[] rutas = ruta.split("/");
		if(rutas.length > 0){
			if(RouteMap.containsKey(rutas[0])){
				RouteDef router = this.RouteMap.get(rutas[0]);
				rutas = ArrayUtils.remove(rutas,0);
				return router.digest_route(rutas,clase, metodo);
			}
			else {
				RouteDef router = new RouteDef(rutas[0]);
				this.RouteMap.put(rutas[0], router);
				rutas = ArrayUtils.remove(rutas,0);
				return router.digest_route(rutas, clase, metodo);
			}
		}
		else
		{
			log.Info("Ruta "+ruta+" incorrecta. Favor corregir");
			return null;
		}
	}
	// MAPEAMOS LOS PACKAGES
	private void MapPackages(String packagename){
		log.Info("Mapeo de Packages: Iniciando mapeo de packages");
		String packageName = "earlgrey";
		URL root = Thread.currentThread().getContextClassLoader().getResource(packageName);
		File[] packages = new File(root.getFile()).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	return true;
		    }
		});
		URL root2 = Thread.currentThread().getContextClassLoader().getResource(packagename);
		File[] packages_project = new File(root2.getFile()).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	return true;
		    }
		});
		this.packages = new ArrayList<String>();
		for(int i=0;i<packages.length;i++){
			this.packages.add(packageName+"."+packages[i].getName()); 
		}
		for(int i=0;i<packages_project.length;i++){
			this.packages.add(packagename+"."+packages_project[i].getName()); 
		}
		log.Info("Mapeo de Packages: Mapeo Finalizado");
	}
	// FUNCION PARA MAPEAR LOS JAR
	private void MapJar() {
		log.Info("Mapeo de JAR Files: Iniciando mapeo de paquetes JAR");
		String packageName = "external";
		URL root = Thread.currentThread().getContextClassLoader().getResource(packageName);
		File[] packages = new File(root.getFile()).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	return true;
		    }
		});
		this.jar = new ArrayList<String>();
		for(int i=0;i<packages.length;i++){
			this.jar.add(root+packages[i].getName()); 
		}
		log.Info("Mapeo de JAR Files Finalizado");
	}
	// CARGAMOS LOS .JAR
	private void loadJAR(){
		URL[] jars = new URL[this.jar.size()];
		for(int i=0; i<this.jar.size();i++){
			try {
				jars[i] = new URL(this.jar.get(i));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		URLClassLoader child = new URLClassLoader(jars, Thread.currentThread().getContextClassLoader());
		this.classloader = child;
	}
	//OBTEBEMOS EL CLASSLOADER JAR
	public ClassLoader getJARClassLoader(){
		return this.classloader;
	}
	// MAPEAMOS LAS CLASES
	private void MapClases() {
		log.Info("Mapeo de Clases: Iniciando mapeo de clases");
		this.clases = new ArrayList<String>();
		for(int l=0;l<this.packages.size();l++){
			String packageName = this.packages.get(l);
			URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));
			File[] packages = new File(root.getFile()).listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			    	return true;
			    }
			});
			for(int i=0;i<packages.length;i++){
				this.clases.add(packageName+"."+packages[i].getName());
			}
		}
		log.Info("Mapeo de Clases: Mapeo Finalizado");
	}
	public String searchClass(String clase){
		for(int i=0;i<this.clases.size();i++){
			if(this.clases.get(0).contains(clase)){
				return this.clases.get(0);
			}
		}
		return null;
	}
	public void MapProperties(){
		log.Info("Mapeo de Properties: Buscando Propertie"
				+ "s declaradas");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				// BUSCAMOS PROPERTIES
				AddPropertie properties[] = cls.getAnnotationsByType(AddPropertie.class);
				if(properties.length > 0){
					for(int l=0;l<properties.length;l++){
						this.log.Info("Mapeando Properties: "+properties[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "single");
						prop.put("name", properties[l].name());
						prop.put("default", properties[l].defaultTo());
						this.PropertieTable.put(properties[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES
				AddPropertieSet propertiesSet[] = cls.getAnnotationsByType(AddPropertieSet.class);
				if(propertiesSet.length > 0){
					for(int l=0;l<propertiesSet.length;l++){
						this.log.Info("Mapeando Propertie Set: "+propertiesSet[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "set");
						prop.put("name", propertiesSet[l].name());
						String[] set = propertiesSet[l].set();
						String[] value = propertiesSet[l].defaultTo();
						JSONObject setObj = new JSONObject();
						for(int g=0;g<set.length;g++){
							String def = "";
							if(value.length > g) def = value[g];
							setObj.put(set[g], def);
						}
						prop.put("set", setObj);
						this.PropertieTable.put(propertiesSet[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES
				AddPropertieOption propertiesOption[] = cls.getAnnotationsByType(AddPropertieOption.class);
				if(propertiesOption.length > 0){
					for(int l=0;l<propertiesOption.length;l++){
						this.log.Info("Mapeando Propertie Option: "+propertiesOption[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "option");
						prop.put("name", propertiesOption[l].name());
						String[] options = propertiesOption[l].option();
						JSONArray option = new JSONArray();
						for(int g=0;g<options.length;g++){
							option.put(options[g]);
						}
						prop.put("options", options);
						prop.put("default", propertiesOption[l].defaultTo());
						this.PropertieTable.put(propertiesOption[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES
				AddPropertieArray propertiesArray[] = cls.getAnnotationsByType(AddPropertieArray.class);
				if(propertiesArray.length > 0){
					for(int l=0;l<propertiesArray.length;l++){
						this.log.Info("Mapeando Propertie Array: "+propertiesArray[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "array");
						prop.put("name", propertiesArray[l].name());
						prop.put("default", new JSONArray(Arrays.asList(propertiesArray[l].defaultTo())));
						this.PropertieTable.put(properties[l].name(), prop);
					}
				}
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de Properties: Proceso Finalizado");
	}
	@SuppressWarnings("unchecked")
	public void MapModels(){
		log.Info("Mapeo de Modelos: Buscando Modelos de datos declarados");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				if (ModelCore.class.isAssignableFrom(cls)) {
					// BUSCAMOS LAS ANOTACIONES QUE DECLARAN COMO MODELO
					Model model = cls.getAnnotation(Model.class);
					if(model != null){
						this.log.Info("Mapeando Modelo: "+model.name());
						this.ModelTable.put(model.name(), cls);
						// BUSCAMOS SI TIENE PROPIEDADES REST
						// DE LO CONTRARIO NO TIENE SENTIDO EL QUE TENGA UNA RUTA ASIGNADA
						// EN CASO DE QUE TENGA PROPIEDADES REST SE DEBE ASIGNAR UN OBJETO DE EJECUCIÓN PARA EL MODELO
						// SI EL MODELO AUTOINSTANCIADO NO POSEE RUTA DAREMOS UN WARNING INFORMANDO
						if(model.REST()){
							Route route = cls.getAnnotation(Route.class);
							if(route != null){
								this.log.Info("Enrutando Modelo RESTFULL: "+route.route());
								String ruta_uri = route.route().trim();
								if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
								if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
								RouteDef router = this.MapRoute(ruta_uri, ModelRest.class);
								router.ModelRest = true;
								router.DELETE = true;
								router.GET = true;
								router.POST = true;
								router.PUT = true;
								router.Model = (Class<ModelCore> ) cls;
								//BUSCAMOS POLICIES
								Policie policie = cls.getAnnotation(Policie.class);
								if(policie != null){
									Class<?> policie_class = this.PolicieTable.get(policie.name());
									if(policie_class != null){
										router.policie = policie_class;
									}
									else{
										this.log.Warning("Policie especificada ("+policie.name()+") no existe en el registro de policies.", Error500.POLICIE_NOT_LOADED);
									}
								}
							}
							else
							{
								this.log.Warning("El modelo fue indicado como RESTFULL pero no se indico la ruta. Se ha declarado pero no es ruteable", Error500.ROUTE_NOT_DECLARED);
							}
						}
					}
				}
			}
			catch (ClassNotFoundException e) {
				this.log.Critic("Error al intentar leer una de las clases declaras en el mapeador de recursos (MapModels).", Error500.CLASS_LOAD_ERROR);
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de Modelos: Finalizado");
	}
	private void MapConsole() {
		//INFORMAMOS
		log.Info("Mapeo de acciones de consola: Buscando controladores de consola");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				if (ControllerCore.class.isAssignableFrom(cls)) {
				    //MAPEAMOS QUE SEA UN CONTROLADOR HABILITADO
					Console controlador = cls.getAnnotation(Console.class);
					if(controlador != null){
						this.ConsoleTable.put(controlador.name(), cls);
						// BUSCAMOS UNA POSIBLE RUTA
						Route ruta = cls.getAnnotation(Route.class);
						if(ruta != null){
							String ruta_uri = ruta.route().trim();
							if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
							if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
							this.log.Info("Enrutando Controlador de Consola: "+ruta.route());
							ruta_uri = "admin/console/"+ruta_uri;
							RouteDef router = this.MapRoute(ruta_uri, cls);
							router.DELETE = controlador.DELETE();
							router.GET = controlador.GET();
							router.POST = controlador.POST();
							router.PUT = controlador.PUT();
							//BUSCAMOS POLICIES
							Policie policie = cls.getAnnotation(Policie.class);
							if(policie != null){
								Class<?> policie_class = this.PolicieTable.get(policie.name());
								if(policie_class != null){
									router.policie = policie_class;
								}
								else{
									this.log.Warning("Policie especificada ("+policie.name()+") no existe en el registro de policies.", Error500.POLICIE_NOT_LOADED);
								}
							}
						}
						else
						{
							this.log.Warning("El controlador de consola no posee una ruta valida. Se ha declarado pero no es ruteable.", Error500.ROUTE_NOT_DECLARED);
						}
						//BUSCAMOS ACTIONS AL INTERIOR
						Method[] metodos = cls.getDeclaredMethods();
						for(int g=0;g<metodos.length;g++){
							//VERIFICAMOS SIN SON CONTROLLER ACTIONS
							ControllerAction action = metodos[g].getAnnotation(ControllerAction.class);
							if(action != null){
								//VERIFICAMOS SI TIENE UNA RUTA ASIGNADA
								Route rutaAction = metodos[g].getAnnotation(Route.class);
								if(rutaAction != null){
									String ruta_uri;
									if(rutaAction.ignorePath()){
										ruta_uri = rutaAction.route().trim();
										if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
										if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
										ruta_uri = "admin/console/"+ruta_uri;
									}
									else
									{
										ruta_uri = ruta.route().trim();
										ruta_uri = ruta_uri.replaceAll(":.*[/]?", "");
										if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
										if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
										ruta_uri = "admin/console/"+ruta_uri;
										String ruta_uri_action = rutaAction.route();
										if(ruta_uri_action.endsWith("/")) ruta_uri_action = ruta_uri_action.substring(0, (ruta_uri_action.length()-1));
										if(ruta_uri_action.startsWith("/")) ruta_uri_action = ruta_uri_action.substring(1);
										ruta_uri = ruta_uri + "/" + ruta_uri_action;
									}
									this.log.Info("Enrutando Console Controller Action: "+ruta_uri);
									RouteDef router = this.MapRoute(ruta_uri, cls, metodos[g]);
									router.DELETE = action.DELETE();
									router.GET = action.GET();
									router.POST = action.POST();
									router.PUT = action.PUT();
									//BUSCAMOS POLICIES
									Policie policie = metodos[g].getAnnotation(Policie.class);
									if(policie != null){
										Class<?> policie_class = this.PolicieTable.get(policie.name());
										if(policie_class != null){
											router.policie = policie_class;
										}
										else{
											this.log.Warning("Policie especificada ("+policie.name()+") no existe en el registro de policies.", Error500.POLICIE_NOT_LOADED);
										}
									}
								}
								else
								{
									this.log.Warning("La acción del controlador no posee una ruta valida. Se ha declarado pero no es ruteable.", Error500.ROUTE_NOT_DECLARED);
								}
							}
						}
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de acciones de consola: Finalizado");
	}
	public void search_database_drivers() {
		//INFORMAMOS
		log.Info("Mapeo de drivers de base de datos.");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				if (Connector.class.isAssignableFrom(cls)) {
				    //MAPEAMOS QUE SEA UN CONTROLADOR HABILITADO
					DatabaseDriver database = cls.getAnnotation(DatabaseDriver.class);
					if(database != null){
						log.Info("Mapeando driver: "+database.name());
						this.databaseDriverTable.put(database.name(), cls);
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de drivers de base de datos: Finalizado");
	}
	public String[] MapAnnotation(String annotation, String key) {
		return null;
		/**
		 * CARCATERISTICA EN CONSTRUCCIÓN AUN NO LISTA PARA LIBERACION
		 */
		//Annotation p = Annotartion.
		
		/*//BUSCAMOS LA CLASE ANNOTATION
		String annotacion = null;
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			if(className.indexOf(annotation) != -1){
				annotation = className;
			}
		}
		if(annotation == null) return new String[0];
		
		//INFORMAMOS
		log.Info("Mapeando annotacion: "+annotation);
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Class.forName(className);
				//CREAMOS LA CLASE
				Class<? extends Annotation> = Class.forName(annotacion);
				Annotation p = Class.forName(annotacion).annota;
				Class<?> controlador = cls.getAnnotation();
				try {
					
					controlador.getDeclaredMethod(key, parameterTypes)
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*Console controlador = cls.getAnnotation(Console.class);
				if(controlador != null){
					this.ConsoleTable.put(controlador.name(), cls);
					// BUSCAMOS UNA POSIBLE RUTA
					Route ruta = cls.getAnnotation(Route.class);
					if(ruta != null){
						this.log.Info("Enrutando Controlador de Consola: "+ruta.route());
						String ruta_uri = ruta.route();
						if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1, (ruta_uri.length()-1));
						RouteDef router = this.MapRoute(ruta_uri, cls);
						//BUSCAMOS POLICIES
						Policie policie = cls.getAnnotation(Policie.class);
						if(policie != null){
							Class<?> policie_class = this.PolicieTable.get(policie.name());
							if(policie_class != null){
								router.policie = policie_class;
							}
							else{
								this.log.Warning("Policie especificada ("+policie.name()+") no existe en el registro de policies.", Error500.POLICIE_NOT_LOADED);
							}
						}
					}
					else
					{
						this.log.Warning("El controlador de consola no posee una ruta valida. Se ha declarado pero no es ruteable.", Error500.ROUTE_NOT_DECLARED);
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de acciones de consola: Finalizado");*/
	}
	private void MapPolicies() {
		//INFORMAMOS
		log.Info("Mapeo de policies: Buscando Policies");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				// BUSCAMOS PROPERTIES
				if (PolicieCore.class.isAssignableFrom(cls) && !cls.isInterface()) {
					this.log.Info("Policie Mapeada: "+cls.getName().substring(cls.getName().lastIndexOf(".")+1));
					this.PolicieTable.put(cls.getName().substring(cls.getName().lastIndexOf(".")+1), cls);
				}
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				this.log.Critic("Error al intentar leer una de las clases declaras en el mapeador de recursos (MapPolicies).", Error500.CLASS_LOAD_ERROR);
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Mapeo de policies: Finalizado");
	}
	// GETERS DE LAS TABLAS MAPEADAS
	public Hashtable<String, ErrorDef> getErrorTable() {
		return ErrorTable;
	}
	public Hashtable<String, RouteDef> getRouteMap() {
		return RouteMap;
	}
	public Hashtable<String, RouteDef> getRouteTable() {
		return RouteTable;
	}
	public Hashtable<String, Class<?>> getModelTable() {
		return ModelTable;
	}
	public Hashtable<String, Class<?>> getControllerTable() {
		return ControllerTable;
	}
	public Hashtable<String, JSONObject> getPropertieTable() {
		return PropertieTable;
	}
	public Hashtable<String, Class<?>> getConsoleTable() {
		return ConsoleTable;
	}
	public Hashtable<String, Class<?>> getPolicieTable() {
		return PolicieTable;
	}
	public Hashtable<String, Class<?>> getDatabaseDriverTable() {
		return databaseDriverTable;
	}
}
