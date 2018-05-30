package earlgrey.core;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import earlgrey.interfaces.Cacheable;
import earlgrey.utils.Hash;

public class CacheCore implements Cacheable{
	public static final int CACHE_JSON = 1;
	public static final int CACHE_XML = 1;
	public static final int CACHE_TEXT = 1;
	
	private Hashtable<String,CacheElement> cachetable;
	
	public static CacheCore instance;
	
	public CacheCore() {
		this.cachetable = new Hashtable<String,CacheElement>();
	}
	
	public static CacheCore getInstance(){
		if(instance == null){
			instance = new CacheCore();
		}
		return instance;
	}
	public void setCache(String key, String content, int time, int type) {
		this.cachetable.put(Hash.MD5(key), new CacheElement(this, key, content, time, type));
	}
	
	public CacheElement getCache(String key) {
		if(this.cachetable.containsKey(Hash.MD5(key))) {
			return this.cachetable.get(Hash.MD5(key));
		}
		return null;
	}
	
	public boolean hasCache(String key) {	
		if(this.cachetable.containsKey(Hash.MD5(key))) {
			return true;
		}
		return false;
	}
	
	public void saveToDisk(){
		
	}
	
	public void loadFromDisk(){
		
	}
	
	public Hashtable<String,CacheElement> getSummary(){
		return this.cachetable;
	}

	@Override
	public void cleanCache(String key) {
		this.cachetable.remove(key);
	}
	
}
