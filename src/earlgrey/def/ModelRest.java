package earlgrey.def;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.AutoIncrement;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.DELETE;
import earlgrey.annotations.GET;
import earlgrey.annotations.OPTIONS;
import earlgrey.annotations.PATCH;
import earlgrey.annotations.POST;
import earlgrey.annotations.PrimaryKey;
import earlgrey.annotations.Required;
import earlgrey.annotations.Unique;
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.Logging;
import earlgrey.core.ModelCore;
import earlgrey.error.Error500;

public class ModelRest extends ControllerCore{
	public static Logging log = new Logging("ModelRest");
	// ESTA CLASE ES CREADA COMO OBJETO DE ALMACENAR MODELOS CREADOS
	// EN FORMATO REST
	// ES UNA CLASE VACIA QUE SE INSTANCIA DESDE EL KERNEL DEL FRAMEWORK
	// USANDO LOS METODOS HEREDADOS POR REST
	@ControllerAction(description = "Method to get registers", name = "get_register", version = 1)
    @GET
	public static void find(HttpRequest req, HttpResponse res, Class<ModelCore> model){
		JSONObject params = req.getParams();
		try {
			ModelCore model_obj = model.newInstance();
			if(params.length() > 0) {
				Iterator<String> ite = params.keys();
				while(ite.hasNext()) {
					String key = ite.next();
					Field campo = null;
					try {
						campo = model.getField(key);
					} catch (NoSuchFieldException e) {
						try {
							campo = model.getField(key.toLowerCase());
						} catch (NoSuchFieldException e1) {
							try {
								campo = model.getField(key.toUpperCase());
							} catch (NoSuchFieldException e2) {
							}
						}
					}
					if(campo != null) {
						campo.set(model_obj, params.getString(key));
					}
				}
			}
			JSONArray retorno = model_obj.find().getJSON();
			res.json(retorno);
		} catch (InstantiationException | IllegalAccessException | SecurityException e) {
			// TODO Auto-generated catch block
			log.printException(e, Error500.MODELREST_GET_ERROR);
			res.serverError();
		}
		return;
	}
	@ControllerAction(description = "Method to get one register", name = "getOne_register", version = 1)
    @GET
	public static void findOne(HttpRequest req, HttpResponse res, Class<ModelCore> model){
		try {
			ModelCore model_obj = model.newInstance();
			Field[] campos = model.getFields();
			Field primary = null;
			for(int i=0; i<campos.length; i++) {
				if(campos[i].isAnnotationPresent(PrimaryKey.class)) {
					primary = campos[i];
					break;
				}
			}
			if(primary != null) {
				Integer id = Integer.parseInt(req.getParam("earl_model_aaa_id_grey"));
				primary.set(model_obj, id);
				JSONArray retorno = model_obj.find().getJSON();
				if(retorno.length() == 1) {
					res.json(retorno.getJSONObject(0));
				} else if(retorno.length() > 1) {
					if(primary.isAnnotationPresent(Unique.class)) {
						res.conflict();
						return;
					} else {
						res.json(retorno);
						return;
					}
				} else {
					res.noContent();
					return;
				}
			} else {
				log.Warning("You need define a Primarykey to use the findOne method in blueprint model");
				res.badRequest();
				return;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.printException(e, Error500.MODELREST_GET_ERROR);
			res.serverError();
			return;
		}
		return;
	}
	@ControllerAction(description = "Method to create a register", name = "create_register", version = 1)
    @POST
	public static void create(HttpRequest req, HttpResponse res, Class<ModelCore> model){
		try {
			ModelCore model_obj = model.newInstance();
			Field[] campos = model.getFields();
			Field primary = null;
			int values = 0;
			for(int i=0; i<campos.length; i++) {
				if(campos[i].isAnnotationPresent(PrimaryKey.class)) {
					primary = campos[i];
				}
				String param = (req.getParam(campos[i].getName().toLowerCase()) != null) ? req.getParam(campos[i].getName().toLowerCase()) : 
					(req.getParam(campos[i].getName().toUpperCase()) != null) ? req.getParam(campos[i].getName().toUpperCase()) : null;
				if(param != null) {
					if(campos[i].getType().equals(int.class) || campos[i].getType().equals(Integer.class)){
						campos[i].set(model_obj, Integer.parseInt(param));
					}
					else if(campos[i].getType().equals(float.class) || campos[i].getType().equals(Float.class)){
						campos[i].set(model_obj, Float.parseFloat(param));
					}
					else if(campos[i].getType().equals(double.class) || campos[i].getType().equals(Double.class)){
						campos[i].set(model_obj, Double.parseDouble(param));
					}
					else {
						campos[i].set(model_obj, param);
					}
					values++;
				} else {
					if(campos[i].isAnnotationPresent(Required.class)) {
						res.paramsRequired();
						return;
					}
				}
			}
			if(values > 0) {
				model_obj.insert();
				res.created();
				return;
			} else if (primary != null){
				if(primary.isAnnotationPresent(AutoIncrement.class)) {
					model_obj.insert();
					res.created();
					return;
				} else {
					log.Warning("To add an empty object you need define the PrimaryKey with AutoIncrement annotation");
					res.badRequest();
					return;
				}
			} else {
				log.Warning("To add an empty object you need define the PrimaryKey with AutoIncrement annotation");
				res.conflict();
				return;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.printException(e, Error500.MODELREST_GET_ERROR);
			res.serverError();
			return;
		}
	}
	@ControllerAction(description = "Method to update a register", name = "update_register", version = 1)
    @PATCH
	public static void update(HttpRequest req, HttpResponse res, Class<ModelCore> model){
		try {
			ModelCore model_obj = model.newInstance();
			Field[] campos = model.getFields();
			Field primary = null;
			int values = 0;
			for(int i=0; i<campos.length; i++) {
				if(campos[i].isAnnotationPresent(PrimaryKey.class)) {
					primary = campos[i];
				}
				String param = (req.getParam(campos[i].getName().toLowerCase()) != null) ? req.getParam(campos[i].getName().toLowerCase()) : 
					(req.getParam(campos[i].getName().toUpperCase()) != null) ? req.getParam(campos[i].getName().toUpperCase()) : null;
				if(param != null) {
					if(campos[i].getType().equals(int.class) || campos[i].getType().equals(Integer.class)){
						campos[i].set(model_obj, Integer.parseInt(param));
					}
					else if(campos[i].getType().equals(float.class) || campos[i].getType().equals(Float.class)){
						campos[i].set(model_obj, Float.parseFloat(param));
					}
					else if(campos[i].getType().equals(double.class) || campos[i].getType().equals(Double.class)){
						campos[i].set(model_obj, Double.parseDouble(param));
					}
					else {
						campos[i].set(model_obj, param);
					}
					values++;
				}
			}
			if(primary != null && values > 0) {
				Integer id = Integer.parseInt(req.getParam("earl_model_aaa_id_grey"));
				if(model_obj.update(id)) {
					res.ok("");
				} else {
					res.serverError("Exists an error while try to update the register");
				}
				return;
			} else {
				log.Warning("You need define the PrimaryKey and set values to update an object");
				res.conflict();
				return;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.printException(e, Error500.MODELREST_GET_ERROR);
			res.serverError();
			return;
		}
	}
	@ControllerAction(description = "Method to destroy a register", name = "destroy_register", version = 1)
    @DELETE
	public static void destroy(HttpRequest req, HttpResponse res, Class<ModelCore> model){
		try {
			ModelCore model_obj = model.newInstance();
			Field[] campos = model.getFields();
			Field primary = null;
			for(int i=0; i<campos.length; i++) {
				if(campos[i].isAnnotationPresent(PrimaryKey.class)) {
					primary = campos[i];
				}
			}
			if(primary != null) {
				Integer id = Integer.parseInt(req.getParam("earl_model_aaa_id_grey"));
				if(model_obj.delete(id)) {
					res.noContent();
				} else {
					res.serverError("Exists an error while try to delete the register");
				}
				return;
			} else {
				log.Warning("You need define the PrimaryKey in the model to destroy an object");
				res.conflict();
				return;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			log.printException(e, Error500.MODELREST_GET_ERROR);
			res.serverError();
			return;
		}
	}
	@ControllerAction(description = "Method get options", name = "options_register", version = 1)
    @OPTIONS
	public static void options(HttpRequest req, HttpResponse res, Class<ModelCore> model){
		res.notImplemented();
		return;
	}
}
