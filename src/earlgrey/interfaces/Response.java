package earlgrey.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Response {
	public void file();
	public void xml(JSONObject obj);
	public void xml(JSONArray obj);
	public void json(JSONObject obj);
	public void json(JSONArray obj);
	public void ok(String message);
	public void noContent();
	public void accepted();
	public void send(String text, int code);
	public void serverError(JSONArray obj);
	public void setHeader(String key, String value);
	public void notFound();
	public void notAllowed();
	public void notImplemented();
	public void forbidden();
	public void customError(JSONArray obj, int code);
	public void customResponse(JSONArray obj, int code);
	public void created();
}
