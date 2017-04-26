package geos.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import geos.core.ModelCore;
import geos.types.CENTROID;
import geos.types.GEOM;
import jdk.nashorn.internal.codegen.types.Type;

public class QueryBuilder {
	private String table;
	private String query;
	private ModelCore modelCore;
	private Hashtable<Field,Object> prepared = new Hashtable<Field,Object>();
	protected ArrayList<Field> prepare_list = new ArrayList<Field>();
	//CONSTRUCTOR
	public QueryBuilder(String table, ModelCore modelCore){
		this.table = table;
		this.modelCore = modelCore;
	}
	public void select(Hashtable<String,Field> params){
		ArrayList<String> param_list = new ArrayList<String>(); 
		Enumeration<String> llaves = params.keys();
		while(llaves.hasMoreElements()){
			String llave = llaves.nextElement();
			if(GEOM.class.isAssignableFrom(params.get(llave).getType())){
				param_list.add(GEOM.GetSQL(llave));
			}
			else if(CENTROID.class.isAssignableFrom(params.get(llave).getType())){
				param_list.add(CENTROID.GetSQL(llave));
			}
			else
			{
				param_list.add(llave);
			}
		}
		String parametros = String.join(",", param_list);
		this.query = "SELECT "+parametros+" FROM "+this.table;
	}
	public void where(Hashtable<String,Field> params){
		if(params.size() > 0){
			ArrayList<String> param_list = new ArrayList<String>(); 
			Enumeration<String> llaves = params.keys();
			while(llaves.hasMoreElements()){
				String llave = llaves.nextElement();
				param_list.add(llave+" = ?");
				try {
					prepare_list.add(params.get(llave));
					prepared.put(params.get(llave), params.get(llave).get(this.modelCore));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String parametros = String.join(" AND ", param_list);
			this.query = this.query+" WHERE "+parametros;
		}
	}
	public void count() {
		// TODO Auto-generated method stub
		this.query = "SELECT COUNT(*) FROM "+this.table;
	}
	public String getQuery(){
		return query;
	}
	public Hashtable<Field, Object> prepared() {
		return prepared;
	}
	public ArrayList<Field> prepared_list() {
		return prepare_list;
	}
	
}
