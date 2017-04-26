package geos.utils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.json.JSONArray;

public class GeoAlgorithm {
	public GeoAlgorithm(){
	
	}
	public double[] douglasPeucker(double tolerance, int segment){
		
		return new double[2];
	}
	public static JSONArray cluster_simplfication(JSONArray coords, double tolerance){
		DecimalFormat df = new DecimalFormat("0.0000");
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(simbolos);
		double pivotx = coords.getDouble(0);
		double pivoty = coords.getDouble(1);
		JSONArray newcoords = new JSONArray();
		newcoords.put(new Float(df.format(pivotx)));
		newcoords.put(new Float(df.format(pivoty)));
		for(int i=2; i<coords.length(); i++){
			if(Math.sqrt(Math.pow((coords.getDouble(i)-pivotx), 2)+Math.pow((coords.getDouble(i+1)-pivoty), 2)) >= tolerance){
				pivotx = coords.getDouble(i);
				pivoty = coords.getDouble(i+1);
				newcoords.put(new Float(df.format(pivotx)));
				newcoords.put(new Float(df.format(pivoty)));
			}
			i++;
		}
		return newcoords;
	}
}
