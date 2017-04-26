package geos.admin;

import geos.core.Properties;

public class Communication {
	private static Communication instance = null;
	//CONSTRUCTOR
	public static Communication getInstance(){
		if(instance == null) instance = new Communication();
		return instance;
	}
}
