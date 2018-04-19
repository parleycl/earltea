package earlgrey.filters;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import earlgrey.core.Gear;
import oracle.sql.DATE;
/**
 * Servlet Filter implementation class Angular
 */
@WebFilter("/console/*")
public class Console extends Apicore implements Filter { 
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		this.ApiEngine(request, response, chain);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	protected void GET(HttpServletRequest request, HttpServletResponse response){
		if(request.getRequestURI().endsWith("/console")) {
			try {
				response.sendRedirect(request.getRequestURI().concat("/"));
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String filereq = request.getRequestURI().replace(request.getServletContext().getContextPath()+"/console", "");
		if(filereq.startsWith("/")) filereq = filereq.substring(1);
		if(!filereq.matches(".*[.]js|.*[.]png|.*[.]jpg|.*[.]svg|.*[.]ico|.*[.]css") || filereq.equals("")){
			filereq = "index.html";
		}
		InputStream file = request.getServletContext().getClassLoader().getResourceAsStream("console/"+filereq);
		if(filereq.matches(".*[.]png|.*[.]jpg|.*[.]svg|.*[.]ico|.*[.]jpge")){
			try{
				BufferedInputStream bis = new BufferedInputStream(file);
				byte[] bytes = new byte[bis.available()];
		        response.setContentType(URLConnection.guessContentTypeFromName(filereq));
		        OutputStream os = response.getOutputStream();
		        bis.read(bytes);
		        os.write(bytes);
			} 
			catch(IOException e){
				response.setStatus(500);
			}   
		} else {
			try {
				if(file != null){
					int content;
					while ((content = file.read()) != -1) {
						// convert to char and display it
						response.getWriter().print((char) content);
					}
					String mimeType = URLConnection.guessContentTypeFromName(filereq);
					if(filereq.matches(".*[.]css")) mimeType = "text/css; charset=utf-8";
					if(mimeType != null) response.setContentType(mimeType);
					response.getWriter().flush();
					file.close();
				}
				else
				{
					response.setStatus(404);
				}
			} catch (IOException e) {
				response.setStatus(500);
			}
		}
	}
	protected void POST(HttpServletRequest request, HttpServletResponse response){
		Gear engine = new Gear(request,response);
		engine.post(true);
	}
	protected void PUT(HttpServletRequest request, HttpServletResponse response){
		Gear engine = new Gear(request,response);
		engine.put(true);
	}
	protected void DELETE(HttpServletRequest request, HttpServletResponse response){
		Gear engine = new Gear(request,response);
		engine.delete(true);
	}
	protected void PATCH(HttpServletRequest request, HttpServletResponse response){
		Gear engine = new Gear(request,response);
		engine.patch(true);
	}
	protected void OPTIONS(HttpServletRequest request, HttpServletResponse response){
		response.setHeader("Connection", "Keep-alive");
		response.setHeader("Keep-Alive", "timeout=2, max=100");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
		Gear engine = new Gear(request,response);
		engine.options(true);
	}
}
