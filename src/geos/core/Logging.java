package geos.core;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

public class Logging {
	private String name;
	public Logging(String classname){
		this.name = classname;
		//this.getError();
	}
	public void Info(String message) {
		Logger.getLogger(getClass().getName()).log(Level.INFO, message);
		this.loginFile("INFO", message);
	}
	public void Config(String message) {
		Logger.getLogger(getClass().getName()).log(Level.CONFIG, message);
		this.loginFile("CONFIG", message);
	}
	public void Warning(String message, int warning) {
		Logger.getLogger(getClass().getName()).log(Level.WARNING, message);
		this.loginFile("WARNING", message);
	}
	public void Critic(String message, int error) {
		Logger.getLogger(getClass().getName()).log(Level.SEVERE, message);
		this.loginFile("CRITIC", message);
	}
	public void loginFile(String type, String message){
		String logname = "kernel/logs/logfile_"+LocalDateTime.now().getDayOfMonth()+LocalDateTime.now().getMonthValue()+LocalDateTime.now().getYear();
		File log = new File(logname);
		try{
			if(!log.exists()) Logger.getLogger(getClass().getName()).log(Level.INFO, "Creado Archivo log "+logname); log.createNewFile();
		}
		catch(IOException e){
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No se ha podido crear el archivo de logging, "+logname);
		}
		try {
			FileWriter fichero = new FileWriter(log,true);
			PrintWriter pw = new PrintWriter(fichero);
			JSONObject json = new JSONObject();
			json.put("date", LocalDateTime.now().getDayOfMonth()+"/"+LocalDateTime.now().getMonthValue()+"/"+LocalDateTime.now().getYear()+" "+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());
			json.put("class", this.name);
			json.put("type", type);
			json.put("message", message);
			json.put("code", "No code");
			pw.println(json.toString());
			pw.close();
			fichero.close();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No se ha podido escribir en el archivo de logging, "+logname);
		}
	}
	public void loginFileWithCode(String type, String message, int ErrorCode){
		
		String logname = "logfile_"+LocalDateTime.now().getDayOfMonth()+LocalDateTime.now().getMonthValue()+LocalDateTime.now().getYear();
		File log = new File(logname);
		try{
			if(!log.exists()) Logger.getLogger(getClass().getName()).log(Level.INFO, "Creado Archivo log "+logname); log.createNewFile(); 
		}
		catch(IOException e){
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No se ha podido crear el archivo de logging, "+logname);
		}
		try {
			FileWriter fichero = new FileWriter(log,true);
			PrintWriter pw = new PrintWriter(fichero);
			JSONObject json = new JSONObject();
			json.put("date", LocalDateTime.now().getDayOfMonth()+"/"+LocalDateTime.now().getMonthValue()+"/"+LocalDateTime.now().getYear()+" "+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute());
			json.put("class", this.name);
			json.put("type", type);
			json.put("message", message);
			json.put("code", ErrorCode);
			pw.println(json.toString());
			pw.close();
			fichero.close();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No se ha podido escribir en el archivo de logging, "+logname);
		}
	}
	
}
