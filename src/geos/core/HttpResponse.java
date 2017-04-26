package geos.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import geos.def.HttpActionDef;
import geos.error.Error500;
import geos.interfaces.Response;

public class HttpResponse implements Response{
	// DEFINIMOS LAS VARIABLES
	private HttpServletResponse response;
	private HttpServletRequest request;
	private HttpActionDef httpActionDef;
	Logging log = new Logging(this.getClass().getName());
	// DEFINIMOS EL CONSTRUCTOR
	public HttpResponse(HttpServletResponse response, HttpServletRequest request, HttpActionDef httpActionDef){
		this.response = response;
		this.httpActionDef = httpActionDef;
		this.request = request;
		// BUSCAMOS SI TIENE ORIGEN 
		String clientOrigin = request.getHeader("origin");
		if(clientOrigin != null && !clientOrigin.isEmpty()){
			response.setHeader("Access-Control-Allow-Origin", clientOrigin);
	        response.setHeader("Access-Control-Allow-Methods", "*");
	        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	        response.setHeader("Access-Control-Max-Age", "86400");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
		}
	}
	@Override
	public void file() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void xml(JSONObject obj) {
		this.response.setContentType("application/xml");
		this.response.setCharacterEncoding("UTF-8");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			String xml = XML.toString(obj,"response");
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+xml;
			System.out.println(xml);
			this.response.getWriter().println(xml);
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta xml", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void xml(JSONArray obj) {
		this.response.setContentType("application/xml");
		this.response.setCharacterEncoding("UTF-8");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			String xml = XML.toString(obj,"response");
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"+xml;
			System.out.println(xml);
			this.response.getWriter().println(xml);
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta xml", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void json(JSONObject obj) {
		this.response.setContentType("application/json");
		this.response.setCharacterEncoding("UTF-8");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			this.response.getWriter().println(obj.toString());
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta json", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void json(JSONArray obj) {
		this.response.setContentType("application/json");
		this.response.setCharacterEncoding("UTF-8");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			this.response.getWriter().println(obj.toString());
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta json", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void ok(String message) {
		// TODO Auto-generated method stub
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			this.response.getWriter().println(message);
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta json", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void send(String text, int code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serverError(JSONArray obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notFound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notAllowed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notImplemented() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forbidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customError(JSONArray obj, int code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void customResponse(JSONArray obj, int code) {
		// TODO Auto-generated method stub
		
	}
	
}
