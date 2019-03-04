package earlgrey.http;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import earlgrey.core.HttpRequest;
import earlgrey.core.HttpResponse;
import earlgrey.core.Logging;

public class CORSResponse {
	public static void CORS(HttpRequest req, HttpResponse res){
		String GET = req.getParam("GET");
		String POST = req.getParam("POST");
		String PUT = req.getParam("PUT");
		String DELETE = req.getParam("DELETE");
		String PATCH = req.getParam("PATCH");
		String HEAD = req.getParam("HEAD");
		String PURGE = req.getParam("PURGE");
		
		// Only allow the methods declared
		String methods = "OPTIONS";
		methods = (GET != null && GET.equals("true")) ? methods.concat(", GET") : methods;
		methods = (POST != null && POST.equals("true")) ?  methods.concat(", POST") : methods;
		methods = (PUT != null && PUT.equals("true")) ? methods.concat(", PUT") : methods;
		methods = (DELETE != null && DELETE.equals("true")) ? methods.concat(", DELETE") : methods;
		methods = (PATCH != null && PATCH.equals("true")) ? methods.concat(", PATCH") :methods ;
		methods = (HEAD != null && HEAD.equals("true")) ? methods.concat(", HEAD") : methods;
		methods = (PURGE != null && PURGE.equals("true")) ? methods.concat(", PURGE") : methods;	
		
		// Make the headers to allow cors
		res.setHeader("Connection", "Keep-alive");
		res.setHeader("Keep-Alive", "timeout=2, max=100");
		res.setHeader("Access-Control-Allow-Methods", methods);
		res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		res.setHeader("Access-Control-Max-Age", "86400");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		
		res.noContent();
		return;
	}
	
	public static void CORS(HttpServletRequest request, HttpServletResponse response){
		String clientOrigin = request.getHeader("Origin");
		if(clientOrigin != null && !clientOrigin.isEmpty()){
			response.setHeader("Access-Control-Allow-Origin", clientOrigin);
	        response.setHeader("Access-Control-Allow-Methods", "*");
	        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
	        response.setHeader("Access-Control-Max-Age", "86400");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
		}
	}
}
