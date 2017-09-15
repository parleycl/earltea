package earlgrey.core;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import java.util.Hashtable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import earlgrey.annotations.Model;
import earlgrey.database.Connector;
import earlgrey.database.QueryBuilder;
import earlgrey.types.CENTROID;
import earlgrey.types.GEOM;

public class ModelCore {
	protected ResultSet set;
	protected Object result;
	protected Class<?> model;
	protected String table;
	protected Connector conector;
	protected Hashtable<String,Field> fields = new Hashtable<String,Field>();
	protected Hashtable<String,Field> prepare_fields = new Hashtable<String,Field>();
	//PARTIMOS CON EL METODO FIND
	public ModelCore(){
		this.model = this.getClass();
		this.mapFields();
		Model modelo = this.getClass().getAnnotation(Model.class);
		this.table = modelo.tableName();
	}
	public static ModelCore Find(JSONObject query, Class<?> clase){
		try {
			ModelCore modelo = (ModelCore) clase.newInstance();
			if(query != null) modelo.transformParams(query);
			return modelo.find();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public ModelCore find(JSONObject query){
		if(query != null) this.transformParams(query);
		return this.find();
	}
	public ModelCore find() {
		// SE PREPARAN LOS PARAMETROS
		System.out.println("sdsd");
		this.prepareParams();
		System.out.println("sdsd1");
		this.conector = DatabasePool.getInstance().getConector();
		System.out.println("sdsd2");
		if(conector != null){
			this.mapFields();
			System.out.println("sdsd3");
			// BUSCAMOS EL NOMBRE DE LA TABLA
			// EN ESTE SEGMENTO VA EL CODIGO DE LA CONSULTA
			QueryBuilder q = new QueryBuilder(table,this);
			System.out.println("sdsd4");
			q.select(fields);
			System.out.println("sdsd5");
			q.where(prepare_fields);
			System.out.println("sdsd6");
			conector.prepare(q.getQuery());
			System.out.println("sdsd7");
			// PREPARAMOS LOS FIELDS
			conector.complete(q.prepared(), q.prepared_list());
			System.out.println("sdsd8");
			this.set = conector.execute();
			System.out.println("sdsd9");
			// SE ACABA EL CODIGO DE LA CONSULTA
			return this;
		}
		return null;
	}
	public static int Count(JSONObject query, Class<?> clase){
		try {
			ModelCore modelo = (ModelCore) clase.newInstance();
			if(query != null) modelo.transformParams(query);
			return modelo.count();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public int count() {
		// SE PREPARAN LOS PARAMETROS
		this.prepareParams();
		this.conector = DatabasePool.getInstance().getConector();
		if(conector != null){
			this.mapFields();
			// BUSCAMOS EL NOMBRE DE LA TABLA
			// EN ESTE SEGMENTO VA EL CODIGO DE LA CONSULTA
			QueryBuilder q = new QueryBuilder(table,this);
			q.count();
			q.where(prepare_fields);
			conector.prepare(q.getQuery());
			// PREPARAMOS LOS FIELDS
			conector.complete(q.prepared(), q.prepared_list());
			this.set = conector.execute();
			// SE ACABA EL CODIGO DE LA CONSULTA
			try {
				this.set.next();
				int result = this.set.getInt(1);
				conector.close();
				return result;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				conector.close();
				e.printStackTrace();
			}
		}
		return -1;
	}
	public ArrayList<ModelCore> get(int init,int limit){
		int limite = 0;
		ArrayList<ModelCore> retorno = new ArrayList<ModelCore>();
		try {
			while(this.set.next() && (limite++ < (init+limit) || limit == -1)){
				if(limite <= init) continue;
				Enumeration<String> keys = fields.keys();
				ModelCore m = (ModelCore) model.newInstance();
				while(keys.hasMoreElements()){
					String llave = keys.nextElement();
					Field campo = fields.get(llave);
					if(campo.getType().equals(int.class) || campo.getType().equals(Integer.class)){
						campo.set(m, set.getInt(llave));
					}
					else if(campo.getType().equals(float.class) || campo.getType().equals(Float.class)){
						campo.set(m, set.getFloat(llave));
					}
					else if(campo.getType().equals(double.class) || campo.getType().equals(Double.class)){
						campo.set(m, set.getDouble(llave));
					}
					else if(campo.getType().equals(String.class)){
						campo.set(m, set.getString(llave));
					}
					else if(campo.getType().equals(GEOM.class)){
						campo.set(m, new GEOM(set.getString(llave)));
					}
					else if(campo.getType().equals(CENTROID.class)){
						campo.set(m, new CENTROID(set.getString(llave)));
					}
				}
				retorno.add(m);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			conector.close();
			e.printStackTrace();
		}
		this.conector.close();
		return retorno;
	}
	public JSONArray getJSON(int init,int limit){
		int limite = 0;
		JSONArray retorno = new JSONArray();
		try {
			int k=0;
			while(this.set.next() && (limite++ < (init+limit) || limit == -1)){
				if(limite <= init) continue;
				Enumeration<String> keys = fields.keys();
				JSONObject objeto = new JSONObject();
				while(keys.hasMoreElements()){
					String llave = keys.nextElement();
					Field campo = fields.get(llave);
					if(campo.getType().equals(int.class) || campo.getType().equals(Integer.class)){
						objeto.put(llave, set.getInt(llave));
					}
					else if(campo.getType().equals(float.class) || campo.getType().equals(Float.class)){
						objeto.put(llave, set.getFloat(llave));
					}
					else if(campo.getType().equals(double.class) || campo.getType().equals(Double.class)){
						objeto.put(llave, set.getDouble(llave));
					}
					else if(campo.getType().equals(String.class)){
						objeto.put(llave, set.getString(llave));
					}
					else if(campo.getType().equals(GEOM.class)){
						objeto.put(llave, new GEOM(set.getString(llave)));
					}
					else if(campo.getType().equals(CENTROID.class)){
						objeto.put(llave, new CENTROID(set.getString(llave)));
					}
				}
				retorno.put(objeto);
			}
		} catch (SQLException e) {
			conector.close();
			e.printStackTrace();
		}
		this.conector.close();
		return retorno;
	}
	public ArrayList<ModelCore> get(){
		return this.get(0,-1);
	}
	public ArrayList<ModelCore> get(int limit){
		return this.get(0,limit);
	}
	public JSONArray getJSON(){
		return this.getJSON(0,-1);
	}
	public JSONArray getJSON(int limit){
		return this.getJSON(0,limit);
	}
	private void prepareParams(){
		Field[] campos = this.getClass().getFields();
		for(int i=0;i<campos.length;i++){
			try {
				if(campos[i].get(this) != null){
					this.prepare_fields.put(campos[i].getName(), campos[i]);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				conector.close();
				e.printStackTrace();
			}
		}
	}
	private void transformParams(JSONObject query) {
		Iterator<String> llaves = query.keys();
		while(llaves.hasNext()){
			String llave = llaves.next();
			try {
				Field campo = this.getClass().getField(llave);
				if(campo.getType().equals(int.class) || campo.getType().equals(Integer.class)){
					campo.set(this, query.getInt(llave));
				}
				else if(campo.getType().equals(float.class) || campo.getType().equals(Float.class)){
					campo.set(this, query.getDouble(llave));
				}
				else if(campo.getType().equals(double.class) || campo.getType().equals(Double.class)){
					campo.set(this, query.getDouble(llave));
				}
				else if(campo.getType().equals(String.class)){
					campo.set(this, query.getString(llave));
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	// INCLUIMOS LOS OTROS METODOS
	public static ModelCore findOne(){
		return null;
	}
	public static ModelCore match(){
		return null;
	}
	public static void update(){
		
	}
	public static void insert(){
		
	}
	public static void delete(){
		
	}
	public static void transaction(){
		
	}
	private void mapFields(){
		Field[] field_array = model.getFields();
		for(int i=0;i<field_array.length;i++){
			this.fields.put(field_array[i].getName(), field_array[i]);
		}
	}
	public void finalize() {
	    this.conector.close();
	}
}
