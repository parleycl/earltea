package earlgrey.core;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import earlgrey.interfaces.Cacheable;
import earlgrey.utils.Hash;

public class CacheElement {
	private int time;
	private long init_time;
	private String content;
	private String path;
	private int type;
	private Cacheable origin;
	
	public CacheElement(Cacheable origin, String path, String content, int time, int type){
		this.path = path;
		this.content = content;
		this.time = time;
		this.origin = origin;
		this.init_time = (new Date()).getTime()/1000;
		this.startTimer();
	}
	
	public boolean checkExpire() {
		if((new Date()).getTime()/1000 > (this.init_time + this.time )){
			return false;
		}
		return true;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public int getType(){
		return this.type;
	}
	
	private void startTimer(){
		CacheElement self = this;
		TimerTask timerTask = new TimerTask() 
	     { 
	         public void run()  
	         { 
	        	 self.origin.cleanCache(Hash.MD5(self.path));
	         } 
	     }; 
	     // Creamos el timmer
	     Timer timer = new Timer(); 
	     // Activamos el timmer para el autokill del cache
	     timer.schedule(timerTask, this.time*1000);
	}
}
