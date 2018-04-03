package earlgrey.filters;


import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet Filter implementation class Angular
 */
@WebFilter("/*")
public class Angular implements Filter {

    /**
     * Default constructor. 
     */
    public Angular() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		String uri = req.getRequestURI();
		if(uri.indexOf("/console") != -1 || uri.indexOf("/api") != -1 || uri.indexOf("/admin/console") != -1){
			chain.doFilter(request, response);
		}
		else
		{
			/*if(uri.matches(".*[.]js|.*[.]png|.*[.]jpg|.*[.]svg|.*[.]ico|.*[.]css")){
				chain.doFilter(request, response);
			}
			else
			{
				InputStream resindex = request.getServletContext().getResourceAsStream("/index.html");
				int content;
				while ((content = resindex.read()) != -1) {
					// convert to char and display it
					response.getWriter().print((char) content);
				}
				response.getWriter().flush();
				resindex.close();
			}*/
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
