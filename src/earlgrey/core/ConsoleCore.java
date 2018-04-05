package earlgrey.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Console
 */
public class ConsoleCore extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConsoleCore() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String filereq = request.getRequestURI().replace(request.getServletContext().getContextPath()+"/console", "");
		if(filereq.startsWith("/")) filereq = filereq.substring(1);
		if(!filereq.matches(".*[.]js|.*[.]png|.*[.]jpg|.*[.]svg|.*[.]ico|.*[.]css") || filereq.equals("")){
			filereq = "index.html";
		}
		InputStream file = request.getServletContext().getClassLoader().getResourceAsStream("console/"+filereq);
		if(file != null){
			int content;
			while ((content = file.read()) != -1) {
				// convert to char and display it
				response.getWriter().print((char) content);
			}
			
			response.getWriter().flush();
			file.close();
		}
		else
		{
			response.setStatus(404);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
