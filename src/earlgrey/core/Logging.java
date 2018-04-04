package earlgrey.core;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

public class Logging {
	private String name;
	private Kernel core;
	public Logging(String classname){
		this.name = classname;
		this.core = Kernel.getInstance();
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
		String logname = this.core.kernelname+"/logs/logfile_"+LocalDateTime.now().getDayOfMonth()+"."+LocalDateTime.now().getMonthValue()+"."+LocalDateTime.now().getYear();
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
		Kernel core = Kernel.getInstance();
		String logname = core.kernelname+"/logs/logfile_"+LocalDateTime.now().getDayOfMonth()+"."+LocalDateTime.now().getMonthValue()+"."+LocalDateTime.now().getYear();
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
	public static File[] getHistory(){
		Kernel core = Kernel.getInstance();
		String logpath = core.kernelname+"/logs";
		File[] files = new File(logpath).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return true;
		    }
		});
		Arrays.sort(files, new Comparator<File>(){
		    public int compare(File f1, File f2)
		    {
		        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
		    } 
		});
		return files;
	}
	public static JSONArray getFile(String name, int from){
		Kernel core = Kernel.getInstance();
		String logpath = core.kernelname+"/logs/"+name;
		File log = new File(logpath);
		JSONArray logtxt = new JSONArray();
		try (Stream<String> lines = Files.lines(log.toPath())) {
			Stream<String> line_aux = lines.skip(from);
			line_aux.forEach(line -> {
		    	logtxt.put(line);
		    });
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logtxt;
	}
}
