package earlgrey.filters;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import earlgrey.core.Logging;
import earlgrey.error.Error500;

public class Apicore {
	private Logging log;
	public Apicore(){
		this.log = new Logging(this.getClass().getName());
	}
	protected void ApiEngine(ServletRequest request, ServletResponse response, FilterChain chain){
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.addHeader("Server-Framework", "Ealgrey");
		httpResponse.setHeader("Connection", "Keep-alive");
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("text/html;charset=UTF-8");
		httpResponse.setHeader("Date", (new Date()).toString());
		String method = httpRequest.getMethod();
		switch (method) {
			case "GET":
				this.GET(httpRequest, httpResponse);
				break;
			case "POST":
				this.POST(httpRequest, httpResponse);
				break;
			case "PUT":
				this.PUT(httpRequest, httpResponse);
				break;
			case "PATCH":
				this.PATCH(httpRequest, httpResponse);
				break;
			case "DELETE":
				this.DELETE(httpRequest, httpResponse);
				break;
			case "OPTIONS":
				this.OPTIONS(httpRequest, httpResponse);
				break;
			default:
				try {
					chain.doFilter(request, response);
				} catch (IOException | ServletException e) {
					// TODO Auto-generated catch block
					this.log.Warning("We can't process the chain filter, please review the code", Error500.CHAIN_FILTER_ERROR);
				}
				break;
		}
	}
	protected void GET(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setStatus(405);
			response.getWriter().print("Earlgrey Engine: Get method, not defined yet for this path: "+request.getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void POST(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setStatus(405);
			response.getWriter().print("Earlgrey Engine: Get method, not defined yet for this path: "+request.getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void PUT(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setStatus(405);
			response.getWriter().print("Earlgrey Engine: Post method, not defined yet for this path: "+request.getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void DELETE(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setStatus(405);
			response.getWriter().print("Earlgrey Engine: Delete method, not defined yet for this path: "+request.getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void PATCH(HttpServletRequest request, HttpServletResponse response){
		try {
			response.setStatus(405);
			response.getWriter().print("Earlgrey Engine: Patch method, not defined yet for this path: "+request.getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void OPTIONS(HttpServletRequest request, HttpServletResponse response){
		try {
			HttpServletResponse httpRes = (HttpServletResponse) response;
			HttpServletRequest httpReq = (HttpServletRequest) request;
			httpRes.setStatus(405);
			httpRes.getWriter().print("Earlgrey Engine: Options method, not defined yet for this path: "+httpReq.getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
