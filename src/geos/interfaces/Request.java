package geos.interfaces;

import org.json.JSONObject;
import geos.def.SessionDef;

public interface Request {
	public String getParam(String key);
	public JSONObject getParams();
	public SessionDef getSession();
	public JSONObject getHeader();
}
