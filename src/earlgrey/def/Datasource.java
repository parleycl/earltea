package earlgrey.def;

import org.json.JSONObject;

public class Datasource {
	public int TYPE;
	public String HOST;
	public String USERNAME;
	public String PASSWORD;
	public String SOURCE;
	public int PORT;
	public int MAX_POOL;
	public boolean ON_DEMAND;
	
	// CONTAINER DATASOURCE TYPE;
	
	public String DATASOURCE;
	public boolean CONTAINER_DATASOURCE;
	
	// CONSTRUCTOR
	
	public Datasource(JSONObject datasource) {
		this.TYPE = datasource.getInt("TYPE");
		this.HOST = datasource.getString("HOST");
		this.USERNAME = datasource.getString("USERNAME");
		this.PASSWORD = datasource.getString("PASSWORD");
		this.SOURCE = datasource.getString("SOURCE");
		this.PORT = datasource.getInt("PORT");
		this.MAX_POOL = datasource.getInt("MAX_POOL");
		this.ON_DEMAND = datasource.getBoolean("ON_DEMAND");
		this.DATASOURCE = datasource.getString("DATASOURCE");
		this.CONTAINER_DATASOURCE = datasource.getBoolean("CONTAINER_DATASOURCE");
	}
	
	// METHODS
	
	public static JSONObject getDatasourceTemplate(){
		JSONObject datasource = new JSONObject();
		datasource.put("TYPE", 0);
		datasource.put("HOST", "");
		datasource.put("USERNAME", "");
		datasource.put("PASSWORD", "");
		datasource.put("SOURCE", "");
		datasource.put("PORT", 0);
		datasource.put("MAX_POOL", 1);
		datasource.put("ON_DEMAND", false);
		datasource.put("DATASOURCE", "");
		datasource.put("CONTAINER_DATASOURCE", false);
		return datasource;
	}
	
	public JSONObject toJSON() {
		JSONObject datasource = new JSONObject();
		datasource.put("TYPE", this.TYPE);
		datasource.put("HOST", this.HOST);
		datasource.put("USERNAME", this.USERNAME);
		datasource.put("PASSWORD", this.PASSWORD);
		datasource.put("SOURCE", this.SOURCE);
		datasource.put("PORT", this.PORT);
		datasource.put("MAX_POOL", this.MAX_POOL);
		datasource.put("ON_DEMAND", this.ON_DEMAND);
		datasource.put("DATASOURCE", this.DATASOURCE);
		datasource.put("CONTAINER_DATASOURCE", this.CONTAINER_DATASOURCE);
		return datasource;
	}
}
