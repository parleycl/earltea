package earlgrey.types;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import earlgrey.core.Logging;
import oracle.jdbc.OraclePreparedStatement;
import oracle.spatial.geometry.JGeometry;

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
	public static void SQLPrepareField(OraclePreparedStatement pstm, Integer number, ObraType data, Connection con){
		Logging log = new Logging(ObraType.class.getName());
		log.Info(data.gml);
		//Desmenusamos el json y hacemos lo que queremos.
		JSONArray lista = new JSONArray(data.gml);
		if(lista.length() == 1) {
			JSONObject feature = lista.getJSONObject(0);
			JSONObject inside = feature.getJSONObject("SPATIAL_OBJECT");
			if(inside.getString("type").equals("Point")) {
				try {
					int[] elemInfo = {1,1,1};
					JSONArray coordenadas = inside.getJSONArray("coordinates");
					double[] ordinates = new double[coordenadas.length()];
					for(int i=0; i<coordenadas.length(); i++) {
						ordinates[i] = coordenadas.getDouble(i);
					}
					JGeometry geometry = new JGeometry(JGeometry.GTYPE_POINT, 4326, elemInfo, ordinates);
					Struct struc = JGeometry.store(geometry, con);
					pstm.setObject(number, struc);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					int[] elemInfo = {1,2,1};
					JSONArray coordenadas = inside.getJSONArray("coordinates");
					double[] ordinates = new double[coordenadas.length()];
					for(int i=0; i<coordenadas.length(); i++) {
						ordinates[i] = coordenadas.getDouble(i);
					}
					JGeometry geometry = new JGeometry(JGeometry.GTYPE_CURVE, 4326, elemInfo, ordinates);
					Struct struc = JGeometry.store(geometry, con);
					pstm.setObject(number, struc);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			ArrayList<Integer> info = new ArrayList<>();
			ArrayList<Double> coords = new ArrayList<>();
			int ultimaPosicion = 1;
			for(int i=0; i< lista.length(); i++) {
				JSONObject feature = lista.getJSONObject(i);
				JSONObject inside = feature.getJSONObject("SPATIAL_OBJECT");
				if(inside.getString("type").equals("Point")) {
					info.add(ultimaPosicion);
					info.add(1);
					info.add(1);
					ultimaPosicion += 2;
				}else {
					info.add(ultimaPosicion);
					info.add(2);
					info.add(1);
					JSONArray coordenadas = inside.getJSONArray("coordinates");
					ultimaPosicion += coordenadas.length();
				}
				//genero multigeometria o coleccion
				JSONArray coordenadas = inside.getJSONArray("coordinates");
				for(int j=0; j<coordenadas.length(); j++) {
						coords.add(coordenadas.getDouble(j));
				}
			}
			int[] elemInfo = new int[info.size()];
			for(int i=0; i<info.size(); i++) {
				elemInfo[i] = info.get(i);
			}
			double[] ordinates = new double[coords.size()];
			for(int j=0; j<coords.size(); j++) {
				ordinates[j] = coords.get(j);
			}
			try {
				JGeometry geometry = new JGeometry(JGeometry.GTYPE_COLLECTION, 4326, elemInfo, ordinates);
				Struct struc = JGeometry.store(geometry, con);
				pstm.setObject(number, struc);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
