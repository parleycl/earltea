package geos.types;

import geos.utils.ConversorCoordenadas;
import geos.utils.GeoAlgorithm;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

public class GEOM extends Type{
	String gml;
	JSONArray[] coord = null;
	public GEOM(String GML){
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
		coord = new JSONArray[1];
		if(poly.getString("tagName").equals("gml:MultiPolygon")){
			JSONObject temp = poly;
			JSONArray temp1 = temp.getJSONArray("childNodes");
			int num = temp1.length();
			coord = new JSONArray[num];
			for(int i=0; i<num; i++){
				JSONObject temp_1 = temp1.getJSONObject(i);
				JSONArray temp_2 = temp_1.getJSONArray("childNodes");
				temp_1 = temp_2.getJSONObject(0);
				temp_2 = temp_1.getJSONArray("childNodes");
				temp_1 = temp_2.getJSONObject(0);
				temp_2 = temp_1.getJSONArray("childNodes");
				temp_1 = temp_2.getJSONObject(0);
				temp_2 = temp_1.getJSONArray("childNodes");
				temp_1 = temp_2.getJSONObject(0);
				String[] co = temp_1.getJSONArray("childNodes").getString(0).split(" ");
				JSONArray coords = new JSONArray();
				for(int l=0; l<co.length; l++){
					String[] cor = co[l].split(",");
					coords.put(new Double(cor[1]));
					coords.put(new Double(cor[0]));
				}
				
				coord[i] = GeoAlgorithm.cluster_simplfication(coords,0.01);
			}
		}
		else if(poly.getString("tagName").equals("gml:Polygon")){
			coord = new JSONArray[1];
			JSONObject temp = poly;
			JSONArray temp1 = temp.getJSONArray("childNodes");
			temp = temp1.getJSONObject(0);
			temp1 = temp.getJSONArray("childNodes");
			temp = temp1.getJSONObject(0);
			temp1 = temp.getJSONArray("childNodes");
			temp = temp1.getJSONObject(0);
			coord = new JSONArray[1];
			String[] co = temp.getJSONArray("childNodes").getString(0).split(" ");
			JSONArray coords = new JSONArray();
			for(int i=0; i< co.length; i++){
				String[] cor = co[i].split(",");
				coords.put(new Double(cor[1]));
				coords.put(new Double(cor[0]));
			}
			
			coord[0] = GeoAlgorithm.cluster_simplfication(coords,0.01);
		}
	}
	public JSONArray[] getCoord() {
		return coord;
	}
	@Override
	public String toString(){
		if(this.coord == null)
		{
			this.GMLCONVERT();
			JSONArray r = new JSONArray(coord);
			return r.toString();
		}
		else
		{
			JSONArray r = new JSONArray(coord);
			return r.toString();
		}
	}
	public static String GetSQL(String field) {
		// TODO Auto-generated method stub
		return "SDO_UTIL.TO_GMLGEOMETRY("+field+") AS "+field;
	}
	public void Simplify(double tolerance){
		for(int i=0; i < coord.length;i++){
			coord[i] = GeoAlgorithm.cluster_simplfication(coord[i],tolerance);
		}
	}
}
