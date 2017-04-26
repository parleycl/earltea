package geos.core;

import java.util.ArrayList;
import java.util.Hashtable;
import geos.def.HttpActionDef;
import geos.interfaces.Process;

public class Engine {
	// ADMINISTRADOR DE PETICIONES Y TAREAS
	Hashtable<Integer, Process> process = new Hashtable<Integer, Process>();
	// LOG
	Logging log = new Logging(this.getClass().getName());
	public static Engine instance;
	// FREE AND PROCESS COUNTER
	ArrayList<Integer> space = new ArrayList();
	int process_counter = 1;
	//METODO DE INSTANCIA
	public static synchronized Engine getInstance(){
		if(instance == null) instance = new Engine();
		return instance;
	}
	public Engine(){
		instance = this;
		this.log.Info("Administrador de tareas en linea");
	}
	public HttpActionDef[] getList(){
		return null;
	}
	public int registerTask(HttpActionDef action){
		if(this.space.size() == 0){
			int process = this.process_counter++;
			this.process.put(process, action);
			return process;
		}
		else{
			int process = this.space.get(0);
			this.space.remove(0);
			this.process.put(process, action);
			return process;
		}
	}
	public void killProcess(int ID){
		Process p = this.process.get(ID);
		p = null;
		this.process.remove(ID);
		space.add(ID);
	}
}
