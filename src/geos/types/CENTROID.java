package geos.types;


import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import geos.utils.ConversorCoordenadas;

public class CENTROID extends Type{
	String gml;
	JSONObject coord = null;
	public CENTROID(String GML){
		super();
		this.gml = GML;
	}
	public void GMLCONVERT(String GML){
		this.extract_polygon(JSONML.toJSONObject(GML));
	}
	public void GMLCONVERT(){
		this.extract_polygon(JSONML.toJSONObject(this.gml));
	}
	public void extract_polygon(JSONObject poly){
		ConversorCoordenadas conversor = new ConversorCoordenadas();
		coord = new JSONObject();
		JSONObject temp = poly;
		String[] cor = temp.getJSONArray("childNodes").getJSONObject(0).getJSONArray("childNodes").getString(0).split(",");
		coord.put("lng", cor[0]);
		coord.put("lat", cor[1]);
	}
	public JSONObject getCoord() {
		return coord;
	}
	@Override
	public String toString(){
		if(this.gml != null){
			this.GMLCONVERT();
			return coord.toString();
		}
		else{
			coord = new JSONObject();
			return coord.toString();
		}
			
		
	}
	public static String GetSQL(String field) {
		// TODO Auto-generated method stub
		return "SDO_UTIL.TO_GMLGEOMETRY("+field+") AS "+field;
	}

}
