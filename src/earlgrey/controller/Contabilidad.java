package earlgrey.controller;

import java.math.BigInteger;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import earlgrey.annotations.Controller;
import earlgrey.annotations.ControllerAction;
import earlgrey.annotations.ParamRequire;
import earlgrey.annotations.Policie;
import earlgrey.annotations.Route;
import earlgrey.core.ControllerCore;
import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.ModelCore;
import earlgrey.model.AuditoriaComunal;
import earlgrey.model.CartografiaRegional;
import earlgrey.model.Presupuesto;
import earlgrey.model.Sica;
import earlgrey.model.Sicogen;

@Controller(description = "Controlador para obtener las actions de contabilidad.", name = "Test", version = 1)
@Route(route = "/contabilidad")
public class Contabilidad extends ControllerCore{
	//CONTROLADOR DE PRUEBA PARA EFECTUAR DESARROLLO DE LA PLATAFORMA.
	@ControllerAction(description = "Acción del controlador utilizada para obtener la metadata de regiones.", name = "contabilidad_metadata_regiones", version = 1)
	@Route(route = "/regional/stats")
	@Policie(name = "AllPass")
	public static void regionalstats(HttpRequest req, HttpResponse res){
		JSONObject regiones = new JSONObject();
		Sicogen contabilidad = new Sicogen();
		ArrayList<ModelCore> resul = contabilidad.find().get();
		for(int i=0;i<resul.size();i++){
			Sicogen com = (Sicogen)resul.get(i);
			if(!regiones.has(com.C_REG_SUBDERE)){
				JSONObject region = new JSONObject();
				region.put("VERDE", 0);
				region.put("ROJO", 0);
				region.put("AMARILLO", 0);
				regiones.put(com.C_REG_SUBDERE, region);
			}
			switch(com.ESTADO){
				case 0:
					regiones.getJSONObject(com.C_REG_SUBDERE).put("VERDE", regiones.getJSONObject(com.C_REG_SUBDERE).getInt("VERDE")+1);
					break;
				case 1:
					regiones.getJSONObject(com.C_REG_SUBDERE).put("AMARILLO", regiones.getJSONObject(com.C_REG_SUBDERE).getInt("AMARILLO")+1);
					break;
				case 2:
					regiones.getJSONObject(com.C_REG_SUBDERE).put("ROJO", regiones.getJSONObject(com.C_REG_SUBDERE).getInt("ROJO")+1);
					break;
			}
		}
		res.json(regiones);
		return;
	}
	@ControllerAction(description = "Acción del controlador utilizada para obtener la metadata de comunas.", name = "contabilidad_metadata_comunas", version = 1)
	@Route(route = "/comunal")
	@ParamRequire(name = "C_REG")
	@Policie(name = "AllPass")
	public static void comunalstats(HttpRequest req, HttpResponse res){
		String region = req.getParam("C_REG");
		JSONObject retorno = new JSONObject();
		Sicogen informes = new Sicogen();
		informes.C_REG_SUBDERE = region;
		JSONArray resul = informes.find().getJSON();
		for(int i=0; i<resul.length(); i++){
			retorno.put(resul.getJSONObject(i).getString("CODIGO"), resul.getJSONObject(i));
		}
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Acción del controlador utilizada para obtener el presupuesto de comunas.", name = "presupuesto_metadata_comunas", version = 1)
	@Route(route = "/presupuesto/comunal")
	@ParamRequire(name = "C_REG")
	@Policie(name = "AllPass")
	public static void presupuestoComunal(HttpRequest req, HttpResponse res){
		String region = req.getParam("C_REG");
		JSONObject retorno = new JSONObject();
		Presupuesto informes = new Presupuesto();
		informes.REGION = region;
		JSONArray resul = informes.find().getJSON();
		for(int i=0; i<resul.length(); i++){
			if(!retorno.has(resul.getJSONObject(i).getString("C_COMUNA_SUBDERE"))){
				JSONObject p = new JSONObject();
				Long presupuesto = Long.valueOf(resul.getJSONObject(i).getString("PRESUPUESTO"));
				Long ejecutado = Long.valueOf(resul.getJSONObject(i).getString("EJECUTADO"));
				p.put("PRESUPUESTO", presupuesto);
				p.put("COMUNA", resul.getJSONObject(i).getString("MUNICIPALIDAD"));
				p.put("CINE_COM", resul.getJSONObject(i).getString("C_COMUNA_SUBDERE"));
				p.put("EJECUTADO", ejecutado);
				retorno.put(resul.getJSONObject(i).getString("C_COMUNA_SUBDERE"),p);
			}
			else{
				JSONObject p = retorno.getJSONObject(resul.getJSONObject(i).getString("C_COMUNA_SUBDERE"));
				p.put("PRESUPUESTO", p.getLong("PRESUPUESTO")+Long.valueOf(resul.getJSONObject(i).getString("PRESUPUESTO")));
				p.put("EJECUTADO", p.getLong("EJECUTADO")+Long.valueOf(resul.getJSONObject(i).getString("EJECUTADO")));
				retorno.put(resul.getJSONObject(i).getString("C_COMUNA_SUBDERE"),p);
			}
		}
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Acción del controlador utilizada para obtener el presupuesto de regiones.", name = "presupuesto_metadata_regiones", version = 1)
	@Route(route = "/presupuesto/regional")
	@Policie(name = "AllPass")
	public static void presupuestoRegional(HttpRequest req, HttpResponse res){
		JSONObject retorno = new JSONObject();
		Presupuesto informes = new Presupuesto();
		JSONArray resul = informes.find().getJSON();
		for(int i=0; i<resul.length(); i++){
			if(!retorno.has(resul.getJSONObject(i).getString("REGION"))){
				JSONObject p = new JSONObject();
				Long presupuesto = Long.valueOf(resul.getJSONObject(i).getString("PRESUPUESTO"));
				Long ejecutado = Long.valueOf(resul.getJSONObject(i).getString("EJECUTADO"));
				p.put("PRESUPUESTO", presupuesto);
				p.put("EJECUTADO", ejecutado);
				retorno.put(resul.getJSONObject(i).getString("REGION"),p);
			}
			else{
				JSONObject p = retorno.getJSONObject(resul.getJSONObject(i).getString("REGION"));
				p.put("PRESUPUESTO", p.getLong("PRESUPUESTO")+Long.valueOf(resul.getJSONObject(i).getString("PRESUPUESTO")));
				p.put("EJECUTADO", p.getLong("EJECUTADO")+Long.valueOf(resul.getJSONObject(i).getString("EJECUTADO")));
				retorno.put(resul.getJSONObject(i).getString("REGION"),p);
			}
		}
		res.json(retorno);
		return;
	}
	@ControllerAction(description = "Acción del controlador utilizada para obtener el presupuesto de regiones.", name = "presupuesto_metadata_regiones", version = 1)
	@Route(route = "/presupuesto/bycomuna")
	@ParamRequire(name = "C_COM")
	@Policie(name = "AllPass")
	public static void presupuestoByComuna(HttpRequest req, HttpResponse res){
		String comuna = req.getParam("C_COM");
		JSONObject retorno = new JSONObject();
		Presupuesto informes = new Presupuesto();
		informes.C_COMUNA_SUBDERE = comuna;
		JSONArray resul = informes.find().getJSON();
		res.json(resul);
		return;
	}
}
