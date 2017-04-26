package geos.core;


import org.json.JSONObject;

public class Request {
	/////////////////////////////////////////////////////////////
	// PATRON ESTANDARD DE UN REQUEST
	// ESTA CLASE CONTIENE LA INFORMACION Y PAQUETE WEBSOCKET
	// DEFINIMOS LAS VARIABLES DEL OBJETO REQUEST
	/////////////////////////////////////////////////////////////
	private Session sesion;
	private String controller;
	private String action;
	private String callback;
	private JSONObject data;
	int code;
	JSONObject header;
	
	//CONSTRUCTOR
	public Request(JSONObject message, Session sesion, int code)  {
		// TODO Auto-generated constructor stub
		this.data = message.getJSONObject("data");
		this.callback = message.getString("callback");
		this.sesion = sesion;
		this.code = code;
		this.header = message.getJSONObject("header");
		if(this.splitRequest(message.getString("request"))){
			//sesion.SetAction(this.controller, this.action, data.toString());
		}
		else
		{
			//throw new Error500Exception();
		}
	}
	private boolean splitRequest(String request){
		if(request.indexOf("?",0) == -1){
			this.action = request.substring((request.lastIndexOf("/")+1), request.length());
			this.controller = request.substring(0,(request.lastIndexOf("/")));
			return true;
		}
		else
		{
			 String querystring = request.substring(request.indexOf("?",0), request.length());
			 request = request.substring(0,request.indexOf("?",0));
			 if(!PqueryString(querystring)){
				 return false;
			 }
			 this.action = request.substring((request.lastIndexOf("/")+1), request.length());
			 this.controller = request.substring(0,(request.lastIndexOf("/")));
			 return true;
		}
	}
	private boolean PqueryString(String query){
		try{
			data = new JSONObject();
			String p[] = query.split("&");
			for(String q:p){
				String keypair[] = q.split("=");
				data.put(p[0], p[1]);
			}
			return true;
		}catch(Exception e){
			return false;
		}		
	}
	//METODOS PUBLICOS DE INTERACCION
	public String getController() {
		return controller;
	}
	public String getAction() {
		return action;
	}
	public JSONObject getData() {
		return data;
	}
	public int getCode() {
		return code;
	}
	public JSONObject getHeader() {
		return header;
	}
	public String getCallback() {
		return callback;
	}
	public void prefix(String prefijo){
		this.controller = this.controller.replaceFirst(prefijo+"/", "");
	}
	public Session getSesion() {
		return sesion;
	}
	
}