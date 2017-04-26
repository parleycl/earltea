package geos.core;


import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import geos.def.HttpActionDef;
import geos.def.SessionDef;
import geos.interfaces.Request;

public class HttpRequest implements Request{
	/////////////////////////////////////////////////////////////
	// PATRON ESTANDARD DE UN REQUEST
	// ESTA CLASE CONTIENE LA INFORMACION NECESARIA PARA EFECTUAR EL REQUEST EN UN ACTION
	// DEFINIMOS LAS VARIABLES DEL OBJETO REQUEST
	/////////////////////////////////////////////////////////////
	JSONObject header;
	JSONObject params;
	SessionDef session;
	HttpServletRequest request;
	HttpActionDef action;
	
	//CONSTRUCTOR
	public HttpRequest(JSONObject params, HttpServletRequest request, HttpActionDef httpActionDef)  {
		this.params = params;
		this.request = request;
		this.action = httpActionDef;
	}
	@Override
	public String getParam(String key) {
		// TODO Auto-generated method stub
		if(params.has(key)) return params.getString(key);
		return null;
	}
	@Override
	public JSONObject getParams() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SessionDef getSession() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public JSONObject getHeader() {
		// TODO Auto-generated method stub
		return null;
	}
}