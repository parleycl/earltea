package earlgrey.controller;

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
import earlgrey.model.CartografiaComunal;
import earlgrey.model.CartografiaRegional;
import earlgrey.model.Sica;

@Controller(description = "Controlador para obtener las actions de auditoria.", name = "Test", version = 1)
@Route(route = "/cartografia")
public class Cartografia extends ControllerCore{
	//CONTROLADOR DE PRUEBA PARA EFECTUAR DESARROLLO DE LA PLATAFORMA.
	@ControllerAction(description = "Acción del controlador utilizada para efectuar un test generico.", name = "prueba", version = 1)
	@Route(route = "/regional")
	@Policie(name = "AllPass")
	public static void regional(HttpRequest req, HttpResponse res){
		JSONObject quer = new JSONObject();
		JSONArray query = CartografiaRegional.Find(quer,CartografiaRegional.class).getJSON();
		res.json(query);
		return;
	}
	@ControllerAction(description = "Acción del controlador utilizada para efectuar un test generico.", name = "prueba", version = 1)
	@Route(route = "/comunal")
	@ParamRequire(name = "C_REG")
	@Policie(name = "AllPass")
	public static void comunal(HttpRequest req, HttpResponse res){
		String reg = req.getParam("C_REG");
		CartografiaComunal comunas = new CartografiaComunal();
		comunas.X_REGI = reg;
		JSONArray query = comunas.find().getJSON();
		res.json(query);
		return;
	}
}
