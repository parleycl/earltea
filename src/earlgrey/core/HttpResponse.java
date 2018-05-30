package earlgrey.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import earlgrey.def.HttpActionDef;
import earlgrey.error.Error500;
import earlgrey.http.CORSResponse;
import earlgrey.interfaces.Response;

public class HttpResponse implements Response{
	// DEFINIMOS LAS VARIABLES
	private HttpServletResponse response;
	private HttpServletRequest request;
	private HttpActionDef httpActionDef;
	
	Logging log = new Logging(this.getClass().getName());
	// DEFINIMOS EL CONSTRUCTOR
	public HttpResponse(HttpServletResponse response, HttpServletRequest request, HttpActionDef httpActionDef, boolean CORS){
		this.response = response;
		this.httpActionDef = httpActionDef;
		this.request = request;
		if(CORS) this.setCORS();
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
			this.httpActionDef.checkCache(xml, CacheCore.CACHE_XML);
			this.response.getWriter().println(xml);
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send xml response", Error500.HTTP_RENDER_ERROR);
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
			this.httpActionDef.checkCache(xml, CacheCore.CACHE_XML);
			this.response.getWriter().println(xml);
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send xml response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void json(JSONObject obj) {
		this.response.setContentType("application/json");
		this.response.setCharacterEncoding("UTF-8");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			String json = obj.toString();
			this.httpActionDef.checkCache(json, CacheCore.CACHE_JSON);
			this.response.getWriter().println(json);
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send json response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void json(JSONArray obj) {
		this.response.setContentType("application/json");
		this.response.setCharacterEncoding("UTF-8");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			String json = obj.toString();
			this.httpActionDef.checkCache(json, CacheCore.CACHE_JSON);
			this.response.getWriter().println(json);
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta json", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}
	
	@Override
	public void noContent() {
		this.response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		try {
			this.httpActionDef.checkCache("", CacheCore.CACHE_JSON);
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta json", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}
	
	@Override
	public void accepted() {
		this.response.setStatus(HttpServletResponse.SC_ACCEPTED);
		try {
			this.httpActionDef.checkCache("", CacheCore.CACHE_JSON);
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send json response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}
	
	@Override
	public void created() {
		this.response.setStatus(HttpServletResponse.SC_CREATED);
		try {
			this.httpActionDef.checkCache("", CacheCore.CACHE_JSON);
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send json response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void ok(String message) {
		// TODO Auto-generated method stub
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			this.httpActionDef.checkCache(message, CacheCore.CACHE_TEXT);
			this.response.getWriter().println(message);
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void send(String text, int code) {
		// TODO Auto-generated method stub
		this.response.setContentType("text/plain");
		this.response.setStatus(code);
		try {
			this.httpActionDef.checkCache(text, CacheCore.CACHE_TEXT);
			this.response.getWriter().println(text);
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void serverError(JSONArray obj) {
		this.response.setContentType("application/json");
		this.response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			this.httpActionDef.checkCache(obj.toString(), CacheCore.CACHE_JSON);
			this.response.getWriter().println(obj.toString());
		} catch (IOException e) {
			this.log.Critic("No se ha podido escribir el buffer web http para la respuesta", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}
	
	public void serverError() {
		this.response.setContentType("application/json");
		this.response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void notFound() {
		// TODO Auto-generated method stub
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void notAllowed() {
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void notImplemented() {
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
		
	}

	@Override
	public void forbidden() {
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
		
	}
	
	@Override
	public void imTeaPot() {
		this.response.setContentType("text/plain");
		this.response.setStatus(418);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
		
	}

	@Override
	public void customError(JSONArray obj, int code) {
		this.response.setContentType("application/json");
		this.response.setStatus(code);
		try {
			this.response.getWriter().println(obj.toString());
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}

	@Override
	public void customResponse(JSONArray obj, int code) {
		this.response.setContentType("application/json");
		this.response.setStatus(code);
		try {
			this.response.getWriter().println(obj.toString());
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
		
	}
	@Override
	public void setHeader(String key, String value) {
		this.response.setHeader(key, value);		
	}
	
	private void setCORS(){
		CORSResponse.CORS(request, response);
	}
	@Override
	public void badRequest() {
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}
	@Override
	public void conflict() {
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_CONFLICT);
		try {
			this.response.getWriter().println("");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}
	@Override
	public void paramsRequired() {
		this.response.setContentType("text/plain");
		this.response.setStatus(422);
		try {
			this.response.getWriter().println("Parameters are required");
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
	}
	@Override
	public void serverError(String text) {
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			this.response.getWriter().println(text);
		} catch (IOException e) {
			this.log.Critic("Earlgrey can't write the web buffer to send the response", Error500.HTTP_RENDER_ERROR);
		}
		this.httpActionDef.finalice();
		
	}
}
