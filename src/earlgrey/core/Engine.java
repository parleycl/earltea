package earlgrey.core;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import earlgrey.def.HttpActionDef;
import earlgrey.interfaces.Process;
import earlgrey.interfaces.PropertiesDepend;

public class Engine {
	// ADMINISTRADOR DE PETICIONES Y TAREAS
	Hashtable<Integer, Process> process = new Hashtable<Integer, Process>();
	// LOG
	Logging log = new Logging(this.getClass().getName());
	public static Engine instance;
	// FREE AND PROCESS COUNTER
	ArrayList<Integer> space = new ArrayList();
	int process_counter = 1;
	private boolean restarting = false;
	//METODO DE INSTANCIA
	public static synchronized Engine getInstance(){
		if(instance == null) instance = new Engine();
		return instance;
	}
	public Engine(){
		instance = this;
		this.log.Info("Task manager online");
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
	public void restartByProperties(){
		this.log.Info("Restarting tasks that depends on properties.");
		this.restarting = true;
		Enumeration<Integer> en = this.process.keys();
		while(en.hasMoreElements()){
			Integer index = en.nextElement();
			if(PropertiesDepend.class.isAssignableFrom(this.process.get(index).getClass())){
				PropertiesDepend prop = (PropertiesDepend)this.process.get(index);
				prop.propertiesRestart();
			}
		}
		this.log.Info("Restart finalised.");
		this.restarting = false;
	}
	public boolean getRestartStatus(){
		return this.restarting;
	}
	public void registerTask(Process prop){
		if(this.space.size() == 0){
			int process = this.process_counter++;
			this.process.put(process, prop);
		}
		else{
			int process = this.space.get(0);
			this.space.remove(0);
			this.process.put(process, prop);
		}
	}
}
