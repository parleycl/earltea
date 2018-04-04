package earlgrey.core;

import org.json.JSONObject;

public class PropertieSet {
	JSONObject set;
	public PropertieSet(JSONObject set){
		this.set = set;
	}
	public String getString(String key){
		if(set.has(key)){
			String value = set.getString(key);
			if(value.indexOf("[STRING]") != -1){
				value = value.replace("[STRING]||", "").split("||")[1];
				return value;
			}
			return null;
		}
		else
		{
			return null;
		}
	}
	public String getNumber(String key){
		if(set.has(key)){
			String value = set.getString(key);
			if(value.indexOf("[NUMBER]") != -1){
				value = value.replace("[NUMBER]||", "").split("||")[1];
				return value;
			}
			return null;
		}
		else
		{
			return null;
		}
	}
	public String getDate(String key){
		if(set.has(key)){
			String value = set.getString(key);
			if(value.indexOf("[DATE]") != -1){
				value = value.replace("[DATE]||", "").split("||")[1];
				return value;
			}
			return null;
		}
		else
		{
			return null;
		}
	}
	public String getOption(String key){
		if(set.has(key)){
			String value = set.getString(key);
			if(value.indexOf("[OPTION]") != -1){
				value = value.replace("[OPTION]||", "").split("||")[1];
				return value;
			}
			return null;
		}
		else
		{
			return null;
		}
	}
}
