package earlgrey.http;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import earlgrey.core.Logging;
import earlgrey.error.Error500;

public class Response500 {
	HttpServletResponse response;
	Logging log;
	public Response500(HttpServletResponse response){
		this.response = response;
		//this.log = new Logging("Response500");
	}
	public void send(String message){
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			response.getWriter().println(message);
		} catch (IOException e) {
			//this.log.Critic(Error500.RENDER_ERROR);
		}
	}
}
