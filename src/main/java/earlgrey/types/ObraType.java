package earlgrey.types;

import java.sql.Clob;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import oracle.jdbc.OraclePreparedStatement;

public class ObraType implements IType{
	String gml;
	JSONObject coord = null;
	public ObraType(String GML){
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
		coord = new JSONObject();
		if(poly.getString("tagName").equals("gml:LineString")){
			coord.put("type", "Polyline");
			JSONArray coor = new JSONArray();
			JSONObject temp = poly;
			String[] cor = temp.getJSONArray("childNodes").getJSONObject(0).getJSONArray("childNodes").getString(0).split(" ");
			for(int k=0; k< cor.length; k++){
				String[] cord = cor[k].split(",");
				coor.put(Double.valueOf(cord[0]));
				coor.put(Double.valueOf(cord[1]));
			}
			coord.put("coordinates", coor);
		}
		else if(poly.getString("tagName").equals("gml:Point")){
			coord.put("type", "Point");
			JSONArray coor = new JSONArray();
			JSONObject temp = poly;
			String[] cor = temp.getJSONArray("childNodes").getJSONObject(0).getJSONArray("childNodes").getString(0).split(",");
			coor.put(Double.valueOf(cor[0]));
			coor.put(Double.valueOf(cor[1]));
			coord.put("coordinates", coor);
		}
		else if(poly.getString("tagName").equals("gml:MultiGeometry")){
			coord.put("type", "Multigeometry");
			JSONArray l = new JSONArray();
			JSONObject temp = poly;
			JSONArray geos = temp.getJSONArray("childNodes");
			for(int h=0; h<geos.length(); h++){
				JSONObject geo = geos.getJSONObject(h);
				String type = geo.getJSONArray("childNodes").getJSONObject(0).getString("tagName");
				if(type.equals("gml:Point")){
					JSONObject element = new JSONObject();
					JSONArray cor = new JSONArray();
					element.put("type", "Point");
					String[] cord = geo.getJSONArray("childNodes").getJSONObject(0).getJSONArray("childNodes").getJSONObject(0).getJSONArray("childNodes").getString(0).split(",");
					cor.put(Double.valueOf(cord[0]));
					cor.put(Double.valueOf(cord[1]));
					element.put("coordinates", cor);
					l.put(element);
				}
				else if(type.equals("gml:LineString")){
					JSONObject element = new JSONObject();
					JSONArray cor = new JSONArray();
					element.put("type", "Polyline");
					String[] coordenadas = geo.getJSONArray("childNodes").getJSONObject(0).getJSONArray("childNodes").getJSONObject(0).getJSONArray("childNodes").getString(0).split(" ");
					for(int d=0; d<coordenadas.length; d++){
						String[] coo = coordenadas[d].split(",");
						cor.put(Double.valueOf(coo[0]));
						cor.put(Double.valueOf(coo[1]));
					}
					element.put("coordinates", cor);
					l.put(element);
				}
			}
			coord.put("features", l);
		}
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
			coord.put("type", "notreference");
			return coord.toString();
		}
			
		
	}
	public static String GetSQLQuery(Object field, Object table) {
		// TODO Auto-generated method stub
		return "SDO_UTIL.TO_GMLGEOMETRY("+(String)table+"."+(String)field+") AS "+(String)field;
	}
	public static Object GetSQLResult(Object GML) {
		// TODO Auto-generated method stub
		Clob result = (Clob)GML;
		String data = "";
		try {
			data = result.getSubString(1, ((Long)result.length()).intValue());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ObraType(data);
	}
	public static void SQLPrepareField(OraclePreparedStatement pstm, int number){
		
	}
}
