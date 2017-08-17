package earlgrey.admin;

import earlgrey.annotations.Policie;
import earlgrey.core.Kernel;
import earlgrey.core.Properties;
public class Persistence {
	Properties prop;
	public Persistence(){
		this.prop = Properties.getInstance();
	}
}
