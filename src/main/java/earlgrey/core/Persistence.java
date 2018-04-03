package earlgrey.core;

import earlgrey.annotations.Policie;
public class Persistence {
	Properties prop;
	public Persistence(){
		this.prop = Properties.getInstance();
	}
}
