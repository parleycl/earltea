package earlgrey.utils;

import org.json.JSONObject;

import earlgrey.annotations.AddConfig;

@AddConfig(defaultTo = "Earlgrey", earlgrey_name = "JSON Web token Secret", name = "JWT_SECRET")
public class JWT {
	public static String getJWT(JSONObject payload){
		return null;
	}
	public static JSONObject getJWTPayload(String JWT){
		return new JSONObject();
	}
}
