package earlgrey.core;

import java.util.Date;

public class CacheElement {
	private int time;
	private long init_time;
	private String content;
	private String path;
	private int type;
	
	public CacheElement(String path, String content, int time, int type){
		this.path = path;
		this.content = content;
		this.time = time;
		this.init_time = (new Date()).getTime()/1000;
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
}
