package earlgrey.policies;

import earlgrey.core.Session;
import earlgrey.interfaces.PolicieCore;

public class Admin implements PolicieCore{

	public boolean check(Session sesion) {
		return false;
	}
	
}
