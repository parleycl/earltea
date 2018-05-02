package earlgrey.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.apache.commons.lang3.ArrayUtils;
import org.jboss.vfs.VirtualFile;
import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.AddConfig;
import earlgrey.annotations.AddConfigArray;
import earlgrey.annotations.AddConfigOption;
import earlgrey.annotations.AddConfigSet;
import earlgrey.annotations.AddConfigSetTemplate;
import earlgrey.annotations.AddPropertie;
import earlgrey.annotations.AddPropertieArray;
import earlgrey.annotations.AddPropertieOption;
import earlgrey.annotations.AddPropertieSet;
import earlgrey.annotations.AddPropertieSetTemplate;
import earlgrey.annotations.Blueprints;
import earlgrey.annotations.CORS;
import earlgrey.annotations.Console;
import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.DELETE;
import earlgrey.annotations.DatabaseDriver;
import earlgrey.annotations.ErrorCode;
import earlgrey.annotations.GET;
import earlgrey.annotations.Model;
import earlgrey.annotations.PATCH;
import earlgrey.annotations.POST;
import earlgrey.annotations.PUT;
import earlgrey.annotations.Policie;
import earlgrey.annotations.Route;
import earlgrey.database.Connector;
import earlgrey.def.ActionDef;
import earlgrey.def.ErrorDef;
import earlgrey.def.HttpMethod;
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
	private ArrayList<Package> packages;
	private ArrayList<String> jar;
	private ArrayList<String> files;
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
	// CARGAMOS LA TABLA DE PROPERTIES
	private Hashtable<String,JSONObject> ConfigTable = new Hashtable<String,JSONObject>();
	// CARGAMOS LA TABLA DE
	private Hashtable<String,Class<?>> ConsoleTable = new Hashtable<String,Class<?>>();
	// CARGAMOS LOS CODIGOS DE ERROR
	private Hashtable<String,Class<?>> PolicieTable = new Hashtable<String,Class<?>>();
	// CARGAMOS LOS DRIVERS DE BASE DE DATOS
	private Hashtable<Integer,Class<?>> databaseDriverTable = new Hashtable<Integer,Class<?>>();
	// LOGGING MAP
	private Logging log = new Logging(this.getClass().getName());
	//CONSTRUCTOR
	public static synchronized ResourceMaping getInstance(){
		return instance;
	}
	//CONSTRUCTOR
	public ResourceMaping(String packages){
		instance = this;
		this.mapFiles();
		this.MapError();
		this.MapPackages(packages);
		this.loadJAR();
		this.MapClases();
		this.MapConfig();
		this.MapProperties();
		this.MapPolicies();
		this.MapConsole();
		this.MapControllers();
		this.MapModels();
		this.search_database_drivers();
		
	}
	public ResourceMaping() {
		// TODO Auto-generated constructor stub
		instance = this;
		this.mapFiles();
		this.MapError();
		this.MapPackages();
		this.loadJAR();
		this.MapClases();
		this.MapConfig();
		this.MapProperties();
		this.MapPolicies();
		this.MapConsole();
		this.MapControllers();
		this.MapModels();
		this.search_database_drivers();
	}
	private void mapFiles(){
		this.files = new ArrayList<String>();
		//Mapeamos todos los archivos del core.
		for (String file : FilesList.Error.files) {
			  this.files.add(file);
		}
	}
	// MAPEO DE LOS ERRORES
	private void MapError(){
		log.Info("Error Mapping: Initializing error mapping");
		String packageName = "earlgrey.error";
		// Find classes implementing ICommand.
		for (int i=0; i< this.files.size(); i++) {
			String className = this.files.get(i).replaceAll(".class$", "");
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
		log.Info("Error Mapping: Process successful");
	}
	// MAPEO DE LOS CONTROLADOES
	private void MapControllers(){
		log.Info("Controllers Mapping: Initializing controllers and routes mapping");
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
						/*if(ruta != null){
							String ruta_uri = ruta.route().trim();
							if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
							if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
							this.log.Info("Enrutando Controlador: "+ruta.route());
							RouteDef router = this.MapRoute(ruta_uri, cls);
							if(cls.getAnnotation(DELETE.class) != null) router.DELETE = true;
							if(cls.getAnnotation(GET.class) != null) router.GET = true;
							if(cls.getAnnotation(POST.class) != null) router.POST = true;
							if(cls.getAnnotation(PUT.class) != null) router.PUT = true;
							if(cls.getAnnotation(PATCH.class) != null) router.PATCH = true;
							if(cls.getAnnotation(CORS.class) != null) router.CORS = true;
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
						}*/
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
									RouteDef router = this.MapRoute(ruta_uri);
									ActionDef actionHttp; 
									if(metodos[g].getAnnotation(DELETE.class) != null) {
										this.log.Info("Routing Controller Action: DELETE "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.DELETE, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(GET.class) != null) {
										this.log.Info("Routing Controller Action: GET "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.GET, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(POST.class) != null) {
										this.log.Info("Routing Controller Action: POST "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.POST, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PUT.class) != null) {
										this.log.Info("Routing Controller Action: PUT "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PUT, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PATCH.class) != null) {
										this.log.Info("Routing Controller Action: PATCH "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PATCH, cls, metodos[g]);
									} else {
										actionHttp = router.createAction(HttpMethod.UNKNOW, cls, metodos[g]);
									}
									if(cls.getAnnotation(CORS.class) != null) router.CORS = true;
									//BUSCAMOS POLICIES
									Policie policie = metodos[g].getAnnotation(Policie.class);
									if(policie != null){
										Class<?> policie_class = this.PolicieTable.get(policie.name());
										if(policie_class != null){
											actionHttp.policie = policie_class;
										}
										else{
											this.log.Warning("Policie specify ("+policie.name()+") don't exists in the policies map.", Error500.POLICIE_NOT_LOADED);
										}
									}
								}
								else
								{
									String ruta_uri = ruta.route().trim();
									if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
									if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
									RouteDef router = this.MapRoute(ruta_uri);
									ActionDef actionHttp = null;
									if(metodos[g].getAnnotation(DELETE.class) != null) {
										this.log.Info("Routing Controller Action: DELETE "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.DELETE, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(GET.class) != null) {
										this.log.Info("Routing Controller Action: GET "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.GET, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(POST.class) != null) {
										this.log.Info("Routing Controller Action: POST "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.POST, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PUT.class) != null) {
										this.log.Info("Routing Controller Action: PUT "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PUT, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PATCH.class) != null) {
										this.log.Info("Routing Controller Action: PATCH "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PATCH, cls, metodos[g]);
									} else {
										this.log.Warning("The controller action don't have a valid route. It's mapped but don't have a route.", Error500.ROUTE_NOT_DECLARED);
									}
									
									// ACTIONS POST ROUTING
									if(actionHttp != null) {
										if(cls.getAnnotation(CORS.class) != null) router.CORS = true;
										//BUSCAMOS POLICIES
										Policie policie = metodos[g].getAnnotation(Policie.class);
										if(policie != null){
											Class<?> policie_class = this.PolicieTable.get(policie.name());
											if(policie_class != null){
												actionHttp.policie = policie_class;
											}
											else{
												this.log.Warning("Policie specify ("+policie.name()+") don't exists in the policies map.", Error500.POLICIE_NOT_LOADED);
											}
										}
									}
									
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
		log.Info("Controller Mapping: Process Finalized");
	}
	// MAPEAMOS UNA RUTA
	private RouteDef MapRoute(String ruta){
		if(!this.RouteTable.containsKey(ruta)) this.RouteTable.put(ruta, (new RouteDef(ruta)));
		String[] rutas = ruta.split("/");
		if(rutas.length > 0){
			if(RouteMap.containsKey(rutas[0])){
				RouteDef router = this.RouteMap.get(rutas[0]);
				rutas = ArrayUtils.remove(rutas,0);
				return router.digest_route(rutas);
			}
			else {
				RouteDef router = new RouteDef(rutas[0]);
				this.RouteMap.put(rutas[0], router);
				rutas = ArrayUtils.remove(rutas,0);
				return router.digest_route(rutas);
			}
		}
		else
		{
			log.Info("Route "+ruta+" incorrect. Please fix it");
			return null;
		}
	}
	public String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }
	// MAPEAMOS LOS PACKAGES
	private void MapPackages(String packagename){
		log.Info("Packages Mapping: Initializing Packages Mapping");
		try {
			URL root2 = Thread.currentThread().getContextClassLoader().getResource(packagename);
			this.packages = new ArrayList<Package>();
			this.packages.add(new Package("default", this.replaceLast(root2.toString(), "/WEB-INF/classes/.*/", "").concat("/WEB-INF/classes/")));
			for(int i=0; i<this.packages.size(); i++){
				root2 = new URL(this.packages.get(i).path);
				URLConnection conn2 = root2.openConnection();
				VirtualFile vf2 = (VirtualFile)conn2.getContent();
				File contentsFile2 = vf2.getPhysicalFile();
				File dir2 = contentsFile2;
				File[] packages_project2 = dir2.listFiles(new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				    	if (!name.endsWith(".class")) {
				    		return true;
				    	}
				    	return false;
				    }
				});
				if(packages_project2 != null){
					for(int l=0; l<packages_project2.length; l++){
						if(this.packages.get(i).packageName.equals("default")){
							this.packages.add(new Package(packages_project2[l].getName(), this.packages.get(i).path+packages_project2[l].getName()+"/"));
						} else {
							this.packages.add(new Package(this.packages.get(i).packageName+"."+packages_project2[l].getName(), this.packages.get(i).path+packages_project2[l].getName()+"/"));
						}
					}
				}
			}
			
			/*for(int =0;i<packages_project2.length;i++){
				this.packages.add(packagename+"."+packages_project2[i].getName()); 
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.Info("Packages Mapping: Mapping Finalized");
	}
	// MAPEAMOS LOS PACKAGES
	private void MapPackages(){
		log.Info("Packages Mapping: Initializing Packages Mapping");
		this.packages = new ArrayList<Package>();
		/*for(int i=0;i<FilesList.List.length;i++){
			this.packages.add(FilesList.List[i].packageName); 
		}*/
		log.Info("Packages Mapping: Mapping Finalized");
	}
	// CARGAMOS LOS .JAR
	private void loadJAR(){
		this.jar = new ArrayList<String>();
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
		log.Info("Class Mapping: Initializing class mapping");
		this.clases = new ArrayList<String>();
		try {
			for(int l=0;l<this.packages.size();l++){
				URL root = new URL(this.packages.get(l).path);
				try {
					URLConnection conn = root.openConnection();
					VirtualFile vf = (VirtualFile)conn.getContent();
					File contentsFile = vf.getPhysicalFile();
					File dir = contentsFile;
					File[] packages = dir.listFiles(new FilenameFilter() {
					    public boolean accept(File dir, String name) {
					    	if(name.endsWith(".class") || name.endsWith(".java")){
					    		return true;
					    	} else {
					    		return false;
					    	}
					    }
					});
					if(packages != null){
						for(int i=0;i<packages.length;i++){
							if(!packages[i].getName().contains("$")){
								this.clases.add(this.packages.get(l).packageName+"."+packages[i].getName());
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			for(int i=0;i<FilesList.List.length;i++){
				for(int l=0; l<FilesList.List[i].files.length; l++){
					this.clases.add(FilesList.List[i].packageName+"."+FilesList.List[i].files[l]); 
				}
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.Info("Class Mapping: Process Successful");
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
		log.Info("Properties Mapping: Finding declared properties.");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				// BUSCAMOS PROPERTIES
				AddPropertie properties[] = cls.getAnnotationsByType(AddPropertie.class);
				if(properties.length > 0){
					for(int l=0;l<properties.length;l++){
						this.log.Info("Mapping Propertie: "+properties[l].name());
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
						this.log.Info("Mapping Propertie Set: "+propertiesSet[l].name());
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
						this.log.Info("Mapping Propertie Option: "+propertiesOption[l].name());
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
						this.log.Info("Mapping Propertie Array: "+propertiesArray[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "array");
						prop.put("name", propertiesArray[l].name());
						prop.put("default", new JSONArray(Arrays.asList(propertiesArray[l].defaultTo())));
						this.PropertieTable.put(propertiesArray[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES TEMPLATES
				AddPropertieSetTemplate propertiesTemplate[] = cls.getAnnotationsByType(AddPropertieSetTemplate.class);
				if(propertiesTemplate.length > 0){
					for(int l=0;l<propertiesTemplate.length;l++){
						this.log.Info("Mapping Propertie Template: "+propertiesTemplate[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "template");
						prop.put("name", propertiesTemplate[l].name());
						String[] set = propertiesTemplate[l].set();
						String[] value = propertiesTemplate[l].defaultTo();
						JSONObject setObj = new JSONObject();
						for(int g=0;g<set.length;g++){
							String def = "";
							if(value.length > g) def = value[g];
							setObj.put(set[g], def);
						}
						prop.put("set", setObj);
						this.PropertieTable.put(propertiesTemplate[l].name(), prop);
					}
				}
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Properties Mapping: Process successful");
	}
	
	public void MapConfig(){
		log.Info("Config Mapping: Finding declared configs");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				// BUSCAMOS PROPERTIES
				AddConfig properties[] = cls.getAnnotationsByType(AddConfig.class);
				if(properties.length > 0){
					for(int l=0;l<properties.length;l++){
						this.log.Info("Mapping config: "+properties[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "single");
						prop.put("name", properties[l].name());
						prop.put("earlgrey_name", properties[l].earlgrey_name());
						prop.put("default", properties[l].defaultTo());
						this.ConfigTable.put(properties[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES
				AddConfigSet propertiesSet[] = cls.getAnnotationsByType(AddConfigSet.class);
				if(propertiesSet.length > 0){
					for(int l=0;l<propertiesSet.length;l++){
						this.log.Info("Mapping config: "+propertiesSet[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "set");
						prop.put("earlgrey_name", propertiesSet[l].earlgrey_name());
						String[] set = propertiesSet[l].set();
						String[] value = propertiesSet[l].defaultTo();
						JSONObject setObj = new JSONObject();
						for(int g=0;g<set.length;g++){
							String def = "";
							if(value.length > g) def = value[g];
							setObj.put(set[g], def);
						}
						prop.put("set", setObj);
						this.ConfigTable.put(propertiesSet[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES
				AddConfigOption propertiesOption[] = cls.getAnnotationsByType(AddConfigOption.class);
				if(propertiesOption.length > 0){
					for(int l=0;l<propertiesOption.length;l++){
						this.log.Info("Mapping config: "+propertiesOption[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "option");
						prop.put("name", propertiesOption[l].name());
						prop.put("earlgrey_name", propertiesOption[l].earlgrey_name());
						String[] options = propertiesOption[l].option();
						JSONArray option = new JSONArray();
						for(int g=0;g<options.length;g++){
							option.put(options[g]);
						}
						prop.put("options", options);
						prop.put("default", propertiesOption[l].defaultTo());
						this.ConfigTable.put(propertiesOption[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES
				AddConfigArray propertiesArray[] = cls.getAnnotationsByType(AddConfigArray.class);
				if(propertiesArray.length > 0){
					for(int l=0;l<propertiesArray.length;l++){
						this.log.Info("Mapping config: "+propertiesArray[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "array");
						prop.put("name", propertiesArray[l].name());
						prop.put("earlgrey_name", propertiesArray[l].earlgrey_name());
						prop.put("default", new JSONArray(Arrays.asList(propertiesArray[l].defaultTo())));
						this.PropertieTable.put(propertiesArray[l].name(), prop);
					}
				}
				// BUSCAMOS PROPERTIES TEMPLATES
				AddConfigSetTemplate propertiesTemplate[] = cls.getAnnotationsByType(AddConfigSetTemplate.class);
				if(propertiesTemplate.length > 0){
					for(int l=0;l<propertiesTemplate.length;l++){
						this.log.Info("Mapping config: "+propertiesTemplate[l].name());
						JSONObject prop = new JSONObject();
						prop.put("type", "template");
						prop.put("name", propertiesTemplate[l].name());
						prop.put("earlgrey_name", propertiesTemplate[l].earlgrey_name());
						String[] set = propertiesTemplate[l].set();
						String[] value = propertiesTemplate[l].defaultTo();
						JSONObject setObj = new JSONObject();
						for(int g=0;g<set.length;g++){
							String def = "";
							if(value.length > g) def = value[g];
							setObj.put(set[g], def);
						}
						prop.put("set", setObj);
						this.PropertieTable.put(propertiesTemplate[l].name(), prop);
					}
				}
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Config Mapping: Process successful");
	}
	
	public void MapModels(){
		log.Info("Models Mapping: Searching models declared");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				if (ModelCore.class.isAssignableFrom(cls)) {
					// BUSCAMOS LAS ANOTACIONES QUE DECLARAN COMO MODELO
					Model model = cls.getAnnotation(Model.class);
					if(model != null){
						this.log.Info("Mapping Model: "+model.name());
						this.ModelTable.put(model.name(), cls);
						// BUSCAMOS SI TIENE PROPIEDADES REST
						// DE LO CONTRARIO NO TIENE SENTIDO EL QUE TENGA UNA RUTA ASIGNADA
						// EN CASO DE QUE TENGA PROPIEDADES REST SE DEBE ASIGNAR UN OBJETO DE EJECUCIÓN PARA EL MODELO
						// SI EL MODELO AUTOINSTANCIADO NO POSEE RUTA DAREMOS UN WARNING INFORMANDO
						Blueprints blueprints = cls.getAnnotation(Blueprints.class);
						if(blueprints != null){
							Route route = cls.getAnnotation(Route.class);
							if(route != null){
								this.log.Info("Routing RESTFUL Model: "+route.route());
								String ruta_uri = route.route().trim();
								if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
								if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
								RouteDef router = this.MapRoute(ruta_uri);
								ActionDef actionHttp[] = new ActionDef[6];
								actionHttp[0] = router.createAction(HttpMethod.DELETE, ModelRest.class);
								actionHttp[1] = router.createAction(HttpMethod.GET, ModelRest.class);
								actionHttp[2] = router.createAction(HttpMethod.POST, ModelRest.class);
								actionHttp[3] = router.createAction(HttpMethod.PUT, ModelRest.class);
								actionHttp[4] = router.createAction(HttpMethod.PATCH, ModelRest.class);
								actionHttp[5] = router.createAction(HttpMethod.OPTIONS, ModelRest.class);
								// Set all actions like model rest
								for(int g=0; g< actionHttp.length; g++){
									actionHttp[g].ModelRest = true;
									actionHttp[g].Model = (Class<ModelCore> ) cls;
								}
								// Set CORS in petition
								if(cls.getAnnotation(CORS.class) != null) router.CORS = true;
								
								//BUSCAMOS POLICIES
								Policie policie = cls.getAnnotation(Policie.class);
								if(policie != null){
									Class<?> policie_class = this.PolicieTable.get(policie.name());
									if(policie_class != null){
										for(int g=0; g< actionHttp.length; g++){
											actionHttp[g].policie = policie_class;
										}
									}
									else{
										this.log.Warning("Policie specify ("+policie.name()+") don't exists in the policies map.", Error500.POLICIE_NOT_LOADED);
									}
								}
							}
							else
							{
								this.log.Warning("The model was defined like RESTFULL but don't have a route. It's declared but dont have a route", Error500.ROUTE_NOT_DECLARED);
							}
						}
					}
				}
			}
			catch (ClassNotFoundException e) {
				this.log.Critic("Error when Earlgrey tried to read a class defined in the resources map (MapModels).", Error500.CLASS_LOAD_ERROR);
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Model Mapping: Finalized");
	}
	private void MapConsole() {
		//INFORMAMOS
		log.Info("Console Actions Mapping: Searching Console Controllers");
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
						/*if(ruta != null){
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
						}*/
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
									RouteDef router = this.MapRoute(ruta_uri);
									ActionDef actionHttp; 
									if(metodos[g].getAnnotation(DELETE.class) != null) {
										this.log.Info("Routing Controller Action: DELETE "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.DELETE, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(GET.class) != null) {
										this.log.Info("Routing Controller Action: GET "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.GET, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(POST.class) != null) {
										this.log.Info("Routing Controller Action: POST "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.POST, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PUT.class) != null) {
										this.log.Info("Routing Controller Action: PUT "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PUT, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PATCH.class) != null) {
										this.log.Info("Routing Controller Action: PATCH "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PATCH, cls, metodos[g]);
									} else {
										actionHttp = router.createAction(HttpMethod.UNKNOW, cls, metodos[g]);
									}
									if(cls.getAnnotation(CORS.class) != null) router.CORS = true;
									//BUSCAMOS POLICIES
									Policie policie = metodos[g].getAnnotation(Policie.class);
									if(policie != null){
										Class<?> policie_class = this.PolicieTable.get(policie.name());
										if(policie_class != null){
											actionHttp.policie = policie_class;
										}
										else{
											this.log.Warning("Policie specify ("+policie.name()+") don't exists in the policies map.", Error500.POLICIE_NOT_LOADED);
										}
									}
								}
								else
								{
									String ruta_uri = "admin/console"+ruta.route().trim();
									if(ruta_uri.endsWith("/")) ruta_uri = ruta_uri.substring(0, (ruta_uri.length()-1));
									if(ruta_uri.startsWith("/")) ruta_uri = ruta_uri.substring(1);
									RouteDef router = this.MapRoute(ruta_uri);
									ActionDef actionHttp = null;
									if(metodos[g].getAnnotation(DELETE.class) != null) {
										this.log.Info("Routing Controller Action: DELETE "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.DELETE, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(GET.class) != null) {
										this.log.Info("Routing Controller Action: GET "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.GET, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(POST.class) != null) {
										this.log.Info("Routing Controller Action: POST "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.POST, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PUT.class) != null) {
										this.log.Info("Routing Controller Action: PUT "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PUT, cls, metodos[g]);
									} else if(metodos[g].getAnnotation(PATCH.class) != null) {
										this.log.Info("Routing Controller Action: PATCH "+ruta_uri);
										actionHttp = router.createAction(HttpMethod.PATCH, cls, metodos[g]);
									} else {
										this.log.Warning("The controller action don't have a valid route. It's mapped but don't have a route.", Error500.ROUTE_NOT_DECLARED);
									}
									
									// ACTIONS POST ROUTING
									if(actionHttp != null) {
										if(cls.getAnnotation(CORS.class) != null) router.CORS = true;
										//BUSCAMOS POLICIES
										Policie policie = metodos[g].getAnnotation(Policie.class);
										if(policie != null){
											Class<?> policie_class = this.PolicieTable.get(policie.name());
											if(policie_class != null){
												actionHttp.policie = policie_class;
											}
											else{
												this.log.Warning("Policie specify ("+policie.name()+") don't exists in the policies map.", Error500.POLICIE_NOT_LOADED);
											}
										}
									}
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
		log.Info("Console Actions Mapping: Finalized");
	}
	public void search_database_drivers() {
		//INFORMAMOS
		log.Info("Mapping database drivers.");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				if (Connector.class.isAssignableFrom(cls)) {
				    //MAPEAMOS QUE SEA UN CONTROLADOR HABILITADO
					DatabaseDriver database = cls.getAnnotation(DatabaseDriver.class);
					if(database != null){
						log.Info("Mapping driver: "+database.name());
						this.databaseDriverTable.put(database.id(), cls);
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Database Drivers Mapping: Finalized");
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
		log.Info("Policies Mapping: Searching Policies");
		for(int i=0;i<this.clases.size();i++){
			String className = this.clases.get(i).replaceAll(".class$", "");
			try {
				Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
				// BUSCAMOS PROPERTIES
				if (PolicieCore.class.isAssignableFrom(cls) && !cls.isInterface()) {
					this.log.Info("Policie Mapped: "+cls.getName().substring(cls.getName().lastIndexOf(".")+1));
					this.PolicieTable.put(cls.getName().substring(cls.getName().lastIndexOf(".")+1), cls);
				}
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				this.log.Critic("Error when Earlgrey tried to read a class defined in the resources map (MapPolicies).", Error500.CLASS_LOAD_ERROR);
				e.printStackTrace();
			}
		}
		//INFORMAMOS
		log.Info("Policies Mapping: Finalized");
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
	public Hashtable<String, JSONObject> getConfigTable() {
		return ConfigTable;
	}
	public Hashtable<String, Class<?>> getConsoleTable() {
		return ConsoleTable;
	}
	public Hashtable<String, Class<?>> getPolicieTable() {
		return PolicieTable;
	}
	public Hashtable<Integer, Class<?>> getDatabaseDriverTable() {
		return databaseDriverTable;
	}
}
