package earlgrey.core;


import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import earlgrey.def.HttpActionDef;
import earlgrey.def.SessionDef;
import earlgrey.interfaces.Request;

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
		this.session = httpActionDef.getSession();
		this.readHeaders();
	}
	private void readHeaders(){
		this.header = new JSONObject();
		Enumeration<String> names = this.request.getAttributeNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			this.header.put(key, request.getHeader(key));
		}
	}
	@Override
	public String getParam(String key) {
		// TODO Auto-generated method stub
		if(params.has(key)) return params.getString(key);
		return null;
	}
	@Override
	public JSONObject getParams() {
		return this.params;
	}
	@Override
	public SessionDef getSession() {
		return this.session;
	}
	@Override
	public JSONObject getHeader() {
		return this.header;
	}
}