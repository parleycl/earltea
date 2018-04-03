package earlgrey.interfaces;

import org.json.JSONObject;

import earlgrey.def.SessionDef;

public interface Request {
	public String getParam(String key);
	public JSONObject getParams();
	public SessionDef getSession();
	public JSONObject getHeader();
}
