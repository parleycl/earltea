package geos.def;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import geos.core.Gear;

public class ActionDef implements Runnable{
	// DECLARAMOS LAS PROPIEDADES DE LA PETICION
	private HttpServletRequest request; 
	private HttpServletResponse response;
	// DECLARAMOS LOS PARAMETROS
	private Hashtable<String,String> parametros;
	// DECLARAMOS LOS MOTORES
	private Gear motor;
	// ID DEL ACTION / HASH
	private String Hash;
	// OBJETO DE RUTA
	private RouteDef route;
	
	// DECLARAMOS LOS CONSTRUCTORES
	public ActionDef(String Hash, RouteDef route, HttpServletRequest request, HttpServletResponse response, 
			Hashtable<String,String> parametros, Gear motor){
		this.request = request;
		this.response = response;
		this.parametros = parametros;
		this.motor = motor;
		this.route = route;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.execute();
	}
	// DECLARAMOS EL METODO DE EJECUCIÓN
	private void execute() {
		//VERIFICAMOS QUE EL PATH TENGA UN METODO ASOCIADO, DE LO CONTRARIO ES GENERICO.
		if(this.route.getMetodo() != null){
			
		}
		else
		{
			
		}
		this.finalice();
	}
	// METODO PARA PROCESAR SIN METODO ASOCIADO
	private boolean without_method(){
		return false;
	}
	// METODO PARA PROCESAR CON METODO ASOCIADO
	private boolean with_method(){
		return false;
	}
	// METODO PARA PROCESAR CON METODO ASOACIADO
	// METODO DE LLAMADA PARA RETIRAR EL ACTION DEL TASKLIST
	private void finalice(){
		if(this.motor != null){
	//		this.motor.finalice(this.Hash);
		}
	}
}
