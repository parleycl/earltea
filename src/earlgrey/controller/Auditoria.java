package earlgrey.controller;

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
import earlgrey.model.Sica;

@Controller(description = "Controlador para obtener las actions de auditoria.", name = "Test", version = 1)
@Route(route = "/auditoria")
public class Auditoria extends ControllerCore{
	//CONTROLADOR DE PRUEBA PARA EFECTUAR DESARROLLO DE LA PLATAFORMA.
	@ControllerAction(description = "Acción del controlador utilizada para obtener la metadata de regiones.", name = "auditoria_metadata_regiones", version = 1)
	@Route(route = "/regional/stats")
	@Policie(name = "AllPass")
	public static void regionalstats(HttpRequest req, HttpResponse res){
		String[] regiones = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"};
		JSONObject retorno = new JSONObject();
		int max = 0;
		int min = -1;
		int acumulado = 0;
		for(int i=0;i<regiones.length;i++){
			Sica informes = new Sica();
			informes.C_REG_SUBDERE = regiones[i];
			int numero = informes.count();
			if(min == -1) min = numero;
			if(numero > max) max = numero;
			acumulado += numero;
			retorno.put(regiones[i],(new JSONObject()).put("C_REG_SUBDERE", regiones[i]).put("TOTAL_INFORMES", numero));
		}
		JSONObject informes = new JSONObject();
		informes.put("REGIONES", retorno);
		informes.put("MIN", min);
		informes.put("MAX", max);
		informes.put("TOTAL", acumulado);
		res.json(informes);
		return;
	}
	@ControllerAction(description = "Acción del controlador utilizada para obtener la metadata de comunas.", name = "auditoria_metadata_comunas", version = 1)
	@Route(route = "/comunal/stats")
	@ParamRequire(name = "C_REG")
	@Policie(name = "AllPass")
	public static void comunalstats(HttpRequest req, HttpResponse res){
		String region = req.getParam("C_REG");
		JSONObject retorno = new JSONObject();
		int max = 0;
		int min = -1;
		int acumulado = 0;
		AuditoriaComunal informes = new AuditoriaComunal();
		informes.C_REG_SUBDERE = region;
		ArrayList<ModelCore> resul = informes.find().get();
		for(int i=0;i<resul.size();i++){
			AuditoriaComunal com = (AuditoriaComunal)resul.get(i);
			int numero = com.TOTAL;
			if(min == -1) min = numero;
			if(numero > max) max = numero;
			acumulado += numero;
			retorno.put(com.CODIGOSUBDERE,(new JSONObject()).put("C_COM_SUBDERE", com.CODIGOSUBDERE).put("TOTAL_INFORMES", numero));
		}
		JSONObject informe = new JSONObject();
		informe.put("COMUNAS", retorno);
		informe.put("MIN", min);
		informe.put("MAX", max);
		informe.put("TOTAL", acumulado);
		res.json(informe);
		return;
	}
	@ControllerAction(description = "Acción del controlador utilizada para obtener las auditorias de una comuna.", name = "auditoria_comunas", version = 1)
	@Route(route = "/comunal/get")
	@ParamRequire(name = "C_COM")
	@Policie(name = "AllPass")
	public static void getAuditoriasComunal(HttpRequest req, HttpResponse res){
		String comuna = req.getParam("C_COM");
		Sica informes = new Sica();
		informes.CODIGOSUBDERE = comuna;
		JSONArray resul = informes.find().getJSON();
		res.json(resul);
		return;
	}
}
