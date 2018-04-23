package earlgrey.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import earlgrey.annotations.ModelField;
import earlgrey.annotations.ModelJoin;
import earlgrey.annotations.ModelRelation;
import earlgrey.annotations.PrimaryKey;
import earlgrey.database.Connector;
import earlgrey.database.QueryBuilder;
import earlgrey.def.RelationDef;
import earlgrey.error.Error500;
import earlgrey.error.Error70;
import earlgrey.error.Error800;
import earlgrey.types.IType;

public class ModelCore {
	protected ResultSet set;
	protected Object result;
	protected Class<?> model;
	protected String table;
	protected String datasource;
	protected Connector conector;
	protected Field primaryKey;
	protected Hashtable<String,Field> fields = new Hashtable<String,Field>();
	protected Hashtable<String,RelationDef> relation = new Hashtable<String,RelationDef>();
	protected Hashtable<String,RelationDef> join = new Hashtable<String,RelationDef>();
	protected Hashtable<String,Field> prepare_fields = new Hashtable<String,Field>();
	protected ArrayList<String> where_field = new ArrayList<String>();
	protected boolean transaction;
	protected static Hashtable<String,Connector> conector_transaction = new Hashtable<String,Connector>();
	// LOGGING
	private Logging log = new Logging(this.getClass().getName());
	//PARTIMOS CON EL METODO FIND
	public ModelCore(){
		this.model = this.getClass();
		this.mapFields();
		Model modelo = this.getClass().getAnnotation(Model.class);
		this.table = modelo.tableName();
		this.datasource = modelo.datasource();
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
	public ModelCore findOne(JSONObject query){
		if(query != null) this.transformParams(query);
		return this.findOne();
	}
	public ModelCore find() {
		// SE PREPARAN LOS PARAMETROS
		this.prepareParams();
		if(this.conector != null) this.conector.close();
		this.conector = DatasourceManager.getInstance().getConnection(this.datasource).getConector();
		if(conector != null){
			this.mapFields();
			// BUSCAMOS EL NOMBRE DE LA TABLA
			// EN ESTE SEGMENTO VA EL CODIGO DE LA CONSULTA
			QueryBuilder q = new QueryBuilder(table,this);
			q.select(fields, this.relation);
			q.join(this.join);
			q.where(prepare_fields);
			q.s_where(this.where_field);
			q.makeConditional();
			conector.prepare(q.getQuery(), this.primaryKey);
			// PREPARAMOS LOS FIELDS
			conector.complete(q.prepared(), q.prepared_list(), q.prepared_match(), q.prepared_match_list());
			this.set = conector.execute();
			// SE ACABA EL CODIGO DE LA CONSULTA
			return this;
		}
		return null;
	}
	public ModelCore match() {
		// SE PREPARAN LOS PARAMETROS
		this.prepareParams();
		if(this.conector != null) this.conector.close();
		this.conector = DatasourceManager.getInstance().getConnection(this.datasource).getConector();
		if(conector != null){
			this.mapFields();
			// BUSCAMOS EL NOMBRE DE LA TABLA
			// EN ESTE SEGMENTO VA EL CODIGO DE LA CONSULTA
			QueryBuilder q = new QueryBuilder(table,this);
			q.select(fields, this.relation);
			q.join(this.join);
			q.match(prepare_fields);
			q.s_where(this.where_field);
			q.makeConditional();
			conector.prepare(q.getQuery(), this.primaryKey);
			// PREPARAMOS LOS FIELDS
			conector.complete(q.prepared(), q.prepared_list(), q.prepared_match(), q.prepared_match_list());
			this.set = conector.execute();
			// SE ACABA EL CODIGO DE LA CONSULTA
			return this;
		}
		return null;
	}
	public ModelCore findOne() {
		// SE PREPARAN LOS PARAMETROS
		this.prepareParams();
		if(this.conector != null) this.conector.close();
		this.conector = DatasourceManager.getInstance().getConnection(this.datasource).getConector();
		if(conector != null){
			this.mapFields();
			// BUSCAMOS EL NOMBRE DE LA TABLA
			// EN ESTE SEGMENTO VA EL CODIGO DE LA CONSULTA
			QueryBuilder q = new QueryBuilder(table,this);
			q.select(fields, this.relation);
			q.where(prepare_fields);
			q.s_where(this.where_field);
			q.one();
			q.makeConditional();
			conector.prepare(q.getQuery(), this.primaryKey);
			// PREPARAMOS LOS FIELDS
			conector.complete(q.prepared(), q.prepared_list(), q.prepared_match(), q.prepared_match_list());
			this.set = conector.execute();
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
		if(this.conector != null) this.conector.close();
		this.conector = DatasourceManager.getInstance().getConnection(this.datasource).getConector();
		if(conector != null){
			this.mapFields();
			// BUSCAMOS EL NOMBRE DE LA TABLA
			// EN ESTE SEGMENTO VA EL CODIGO DE LA CONSULTA
			QueryBuilder q = new QueryBuilder(table,this);
			q.count();
			q.where(prepare_fields);
			q.s_where(this.where_field);
			q.makeConditional();
			conector.prepare(q.getQuery(), this.primaryKey);
			// PREPARAMOS LOS FIELDS
			conector.complete(q.prepared(), q.prepared_list(), q.prepared_match(), q.prepared_match_list());
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
					else if(IType.class.isAssignableFrom(campo.getType())){
						try {
							Method inv = campo.getType().getMethod("GetSQLResult", Object.class);
							campo.set(m, inv.invoke(null, set.getObject(llave)));
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				Enumeration<String> r_keys = relation.keys();
				while(r_keys.hasMoreElements()){
					String llave = r_keys.nextElement();
					Field campo = relation.get(llave).field;
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
					else if(IType.class.isAssignableFrom(campo.getType())){
						try {
							Method inv = campo.getType().getMethod("GetSQLResult", Object.class);
							campo.set(m, inv.invoke(null, set.getObject(llave)));
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				retorno.add(m);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			StackTraceElement[] stack = e.getStackTrace();
			this.log.Critic(e.getCause().getMessage(), 510);
			this.log.Critic("Earlgrey detected an error while examinate the SQL data set", Error500.METHOD_INVOCATION_ERROR);
			for(int i=0;i<stack.length;i++){
				this.log.Critic(stack[i].toString(), Error800.DATABASE_SQL_SET);
			}
			conector.close();
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
					else if(IType.class.isAssignableFrom(campo.getType())){
						try {
							Method inv = campo.getType().getMethod("GetSQLResult", Object.class);
							objeto.put(llave, inv.invoke(null, set.getObject(llave)));
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				Enumeration<String> r_keys = relation.keys();
				while(r_keys.hasMoreElements()){
					String llave = r_keys.nextElement();
					Field campo = relation.get(llave).field;
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
					else if(IType.class.isAssignableFrom(campo.getType())){
						try {
							Method inv = campo.getType().getMethod("GetSQLResult", Object.class);
							objeto.put(llave, inv.invoke(null, set.getObject(llave)));
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
	public JSONObject getOneJSON(){
		JSONArray result = this.getJSON(0,1);
		return (result.length() > 0) ? result.getJSONObject(0) : new JSONObject();
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
				if(query.has(llave)){
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
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// INCLUIMOS LOS OTROS METODOS
	public static ModelCore FindOne(){
		return null;
	}
	public static void update(){
		
	}
	public boolean insert(){
		this.prepareParams();
		if(conector_transaction.contains(this.datasource)){
			this.conector = conector_transaction.get(this.datasource);
            this.transaction = true;
		}
		else
		{
			this.conector = DatasourceManager.getInstance().getConnection(this.datasource).getConector();
		}
		if(conector != null){
			this.mapNNFields();
			// BUSCAMOS EL NOMBRE DE LA TABLA
			// EN ESTE SEGMENTO VA EL CODIGO DE LA CONSULTA
			QueryBuilder q = new QueryBuilder(table,this);
			q.insert(fields, this.relation);
			q.setField(prepare_fields);
			conector.prepare(q.getQuery(), this.primaryKey);
			// PREPARAMOS LOS FIELDS
			conector.complete(q.prepared(), q.prepared_list());
			if(!conector.executeUpdate()){
				// HANDLEREAMOS EL INSERT
				return false;
			}
			return true;
		}
		return false;
	}
	public int getLastId(){
		// SE ACABA EL CODIGO DE LA CONSULTA
		try {
			return conector.getLastInsertedId();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	public static void delete(){
		
	}
	public static void openTransaction(Class clase){
		Model modelo = (Model) clase.getAnnotation(Model.class);
		String datasource = modelo.datasource();
		if(!conector_transaction.containsKey(datasource)){
			Connector con = DatasourceManager.getInstance().getConnection(datasource).getConector();
			con.startTransaction();
			conector_transaction.put(datasource, con);
		}
	}
	public static boolean finishTransaction(Class clase) {
		Model modelo = (Model) clase.getAnnotation(Model.class);
		String datasource = modelo.datasource();
		if(!conector_transaction.containsKey(datasource)){
			Connector con = conector_transaction.get(datasource);
			conector_transaction.remove(datasource);
			boolean resultado = con.finishTransaction();
			con.closeTransaction();
			con.close();
			return resultado;
		}
		return false;
	}
	public static boolean finishAllTransaction() {
		Enumeration<String> keys = conector_transaction.keys();
		ArrayList<Connector> conectores = new ArrayList<Connector>();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			Connector con = conector_transaction.get(key);
			if(con.finishTransaction()){
				conectores.add(con);
			}
			else
			{
				for(int i=0; i<conectores.size(); i++){
					Connector con_rollback = conectores.get(i);
					con_rollback.rollback();
					con.close();
				}
				return false;
			}
		}
		// Finiquitamos las transacciones dejandolas en true autocommit
		for(int i=0; i<conectores.size(); i++){
			Connector con = conectores.get(i);
			con.closeTransaction();
			con.close();
		}
		return true;
	}
	private void mapFields(){
		this.fields = new Hashtable<String,Field>();
		Field[] field_array = model.getFields();
		for(int i=0;i<field_array.length;i++){
			Annotation[] anotaciones = field_array[i].getDeclaredAnnotations();
			boolean field = false;
			boolean relation = false;
			for(int l=0; l<anotaciones.length;l++){
				try{
					if(PrimaryKey.class.isAssignableFrom(anotaciones[l].getClass())){
						if(this.primaryKey != null) {
							if(this.primaryKey.getName() != field_array[i].getName()) throw new Exception("Only can declare one primary key");
						}
						this.primaryKey = field_array[i];
					}
					else if(ModelField.class.isAssignableFrom(anotaciones[l].getClass())){
						if(relation) throw new Exception("You can't declare a field like ModelField if previously was declared like ModelRelation");
						this.fields.put(field_array[i].getName(), field_array[i]);
						field = true;
					}
					else if(ModelRelation.class.isAssignableFrom(anotaciones[l].getClass())){
						if(field) throw new Exception("You can't declare a field like ModelRelation if previously was declared like ModelField");
						ModelRelation relacion = (ModelRelation) anotaciones[l];
						this.relation.put(field_array[i].getName(), new RelationDef(field_array[i], relacion.model()));
						relation = true;
					}
					else if(ModelJoin.class.isAssignableFrom(anotaciones[l].getClass())){
						ModelJoin join = (ModelJoin) anotaciones[l];
						this.join.put(field_array[i].getName(), new RelationDef(field_array[i], join.field(), join.model()));
					}
				}
				catch(Exception e){
					StackTraceElement[] stack = e.getCause().getStackTrace();
					this.log.Critic("Exists an error stablising the data model", Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Cause: "+e.getCause().getMessage(), Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Cause: ", Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Localized Message: "+e.getLocalizedMessage(), Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Message:"+e.getMessage(), Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("-------------------- STACK --------------------", Error70.FIELD_OVERLAYED_DEFINITION);
					for(int k=0;k<stack.length;k++){
						this.log.Critic(stack[k].toString(), Error70.FIELD_OVERLAYED_DEFINITION);
					}
				}
				
			}
		}
	}
	private void mapNNFields() {
		this.fields = new Hashtable<String,Field>();
		Field[] field_array = model.getFields();
		for(int i=0;i<field_array.length;i++){
			Annotation[] anotaciones = field_array[i].getDeclaredAnnotations();
			boolean field = false;
			boolean relation = false;
			for(int l=0; l<anotaciones.length;l++){
				try{
					if(PrimaryKey.class.isAssignableFrom(anotaciones[l].getClass())){
						if(this.primaryKey != null) {
							if(this.primaryKey.getName() != field_array[i].getName()) throw new Exception("Only can declare one primary key");
						}
						this.primaryKey = field_array[i];
					}
					else if(ModelField.class.isAssignableFrom(anotaciones[l].getClass())){
						if(relation) throw new Exception("You can't declare a field like ModelField if previously was declared like ModelRelation");
						if(field_array[i].get(this) != null){
							this.fields.put(field_array[i].getName(), field_array[i]);
							field = true;
						}
					}
					else if(ModelRelation.class.isAssignableFrom(anotaciones[l].getClass())){
						if(field) throw new Exception("You can't declare a field like ModelRelation if previously was declared like ModelField");
						ModelRelation relacion = (ModelRelation) anotaciones[l];
						if(field_array[i].get(this) != null){
							this.relation.put(field_array[i].getName(), new RelationDef(field_array[i], relacion.model()));
							relation = true;
						}
					}
					else if(ModelJoin.class.isAssignableFrom(anotaciones[l].getClass())){
						ModelJoin join = (ModelJoin) anotaciones[l];
						if(field_array[i].get(this) != null){
							this.join.put(field_array[i].getName(), new RelationDef(field_array[i], join.field(), join.model()));
						}
					}
				}
				catch(Exception e){
					StackTraceElement[] stack = e.getCause().getStackTrace();
					this.log.Critic("Exists an error stablising the data model", Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Cause: "+e.getCause().getMessage(), Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Cause: ", Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Localized Message: "+e.getLocalizedMessage(), Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("Message:"+e.getMessage(), Error70.FIELD_OVERLAYED_DEFINITION);
					this.log.Critic("-------------------- STACK --------------------", Error70.FIELD_OVERLAYED_DEFINITION);
					for(int k=0;k<stack.length;k++){
						this.log.Critic(stack[k].toString(), Error70.FIELD_OVERLAYED_DEFINITION);
					}
				}
			}
		}
	}
	public void mayorOrEqual(String string, String from) {
		// TODO Auto-generated method stub
		
	}
	public void minorOrEqual(String string, String from) {
		// TODO Auto-generated method stub
	}
	public void where(String key, String operator, String value){
		this.where_field.add(key+" "+operator+" "+value);
	}
	@Override
	protected void finalize() throws Throwable {
	     try {
	    	 this.conector.close();
	     } finally {
	         super.finalize();
	     }
	}
}
