package earlgrey.types;


import java.sql.Clob;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import earlgrey.utils.ConversorCoordenadas;
import oracle.jdbc.OraclePreparedStatement;

public class CentroideType implements IType{
	String gml;
	JSONObject coord = null;
	public CentroideType(String GML){
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
		return new CentroideType(data);
	}
	public static void SQLPrepareField(OraclePreparedStatement pstm, int number){
		
	}
}
