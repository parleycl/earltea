package earlgrey.core;

import java.util.Hashtable;

import earlgrey.annotations.AddPropertie;
import earlgrey.annotations.AddPropertieArray;
import earlgrey.annotations.AddPropertieOption;

public class CacheCore {
	private Hashtable<String,String> cachetable;
	
	public static CacheCore instance;
	
	public static CacheCore getInstance(){
		if(instance == null){
			instance = new CacheCore();
		}
		return instance;
	}
}
