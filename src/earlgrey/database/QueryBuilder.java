package earlgrey.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

import earlgrey.annotations.Model;
import earlgrey.core.ModelCore;
import earlgrey.def.Criteria;
import earlgrey.def.RelationDef;
import earlgrey.types.CentroideType;
import earlgrey.types.GeometriaType;
import earlgrey.types.IType;

public class QueryBuilder {
	private String table;
	private String query;
	private ModelCore modelCore;
	private ModelCore where;
	private Hashtable<Field,Object> prepared = new Hashtable<Field,Object>();
	protected ArrayList<Field> prepare_list = new ArrayList<Field>();
	private ArrayList<String> param_list = new ArrayList<String>();
	private Hashtable<Field,Object> prepared_match = new Hashtable<Field,Object>();
	protected ArrayList<Field> prepare_match_list = new ArrayList<Field>();
	private ArrayList<String> match_list = new ArrayList<String>();
	//CONSTRUCTOR
	public QueryBuilder(String table, ModelCore modelCore){
		this.table = table;
		this.modelCore = modelCore;
	}
	public void select(Hashtable<String,Criteria> params, Hashtable<String,RelationDef> relation){
		ArrayList<String> param_list = new ArrayList<String>(); 
		Enumeration<String> llaves = params.keys();
		while(llaves.hasMoreElements()){
			String llave = llaves.nextElement();
			if(IType.class.isAssignableFrom(params.get(llave).field.getType())){
				Method inv;
				try {
					inv = params.get(llave).field.getType().getMethod("GetSQLQuery", Object.class, Object.class);
					param_list.add((String) inv.invoke(null, llave, this.table));
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
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
			else
			{
				param_list.add(this.table+"."+llave);
			}
		}
		this.joinSelect(relation, param_list);
		String parametros = String.join(",", param_list);
		this.query = "SELECT "+parametros+" FROM "+this.table;
	}
	public void insert(Hashtable<String,Criteria> params, Hashtable<String,RelationDef> relation){
		ArrayList<String> param_list = new ArrayList<String>(); 
		Enumeration<String> llaves = params.keys();
		while(llaves.hasMoreElements()){
			String llave = llaves.nextElement();
			param_list.add(this.table+"."+llave);
		}
		this.joinSelect(relation, param_list);
		String parametros = String.join(",", param_list);
		String valores = String.join(",", Collections.nCopies(param_list.size(), "?"));
		this.query = "INSERT INTO "+this.table+" ("+parametros+") VALUES ("+valores+")";
	}
	public void delete(){
		this.query = "DELETE FROM "+this.table;
	}
	private void joinSelect(Hashtable<String,RelationDef> relation, ArrayList<String> params){
		Enumeration<String> join_llaves = relation.keys();
		while(join_llaves.hasMoreElements()){
			String llave = join_llaves.nextElement();
			Model model = (Model) relation.get(llave).model.getAnnotation(Model.class);
			String tabla = model.tableName();
			if(IType.class.isAssignableFrom(relation.get(llave).field.getType())){
				Method inv;
				try {
					inv = relation.get(llave).field.getType().getMethod("GetSQLQuery", Object.class, Object.class);
					params.add((String) inv.invoke(null, llave, tabla));
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				params.add(tabla+"."+llave);
			}
		}
	}
	public void where(Hashtable<String,Criteria> params){
		if(params.size() > 0){
			Enumeration<String> llaves = params.keys();
			while(llaves.hasMoreElements()){
				String llave = llaves.nextElement();
				this.param_list.add(llave+" = ?");
				try {
					prepare_list.add(params.get(llave).field);
					prepared.put(params.get(llave).field, params.get(llave).field.get(params.get(llave).model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void makeConditional(){
		if(this.param_list.size() > 0 || this.match_list.size() > 0){
			this.param_list.addAll(this.match_list);
			String parametros = String.join(" AND ", this.param_list);
			this.query = this.query+" WHERE "+parametros;
		}
	}
	public void one(){
		this.param_list.add("ROWNUM <= 1");
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
	public Hashtable<Field, Object> prepared_match() {
		return prepared_match;
	}
	public ArrayList<Field> prepared_match_list() {
		return prepare_match_list;
	}
	public void s_where(ArrayList<String> where_field) {
		// TODO Auto-generated method stub
		for(int i=0; i<where_field.size();i++){
			this.param_list.add(where_field.get(i));
		}
	}
	public void match(Hashtable<String, Criteria> params) {
		if(params.size() > 0){
			Enumeration<String> llaves = params.keys();
			while(llaves.hasMoreElements()){
				String llave = llaves.nextElement();
				this.match_list.add(llave+" LIKE ?");
				try {
					this.prepare_match_list.add(params.get(llave).field);
					this.prepared_match.put(params.get(llave).field, params.get(llave).field.get(params.get(llave).model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void join(Hashtable<String, RelationDef> join) {
		// TODO Auto-generated method stub
		ArrayList<String> join_list = new ArrayList<String>(); 
		Enumeration<String> llaves = join.keys();
		while(llaves.hasMoreElements()){
			String llave = llaves.nextElement();
			Model model = (Model) join.get(llave).model.getAnnotation(Model.class);
			if(model != null){
				if(IType.class.isAssignableFrom(join.get(llave).field.getType())){
					Method inv;
					try {
						inv = join.get(llave).field.getType().getMethod("GetSQLQuery", Object.class, Object.class);
						join_list.add("JOIN "+model.tableName()+" ON "+model.tableName()+"."+join.get(llave).field_name_to+"="+(String) inv.invoke(null, llave, this.table));
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
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
				else
				{
					join_list.add("JOIN "+model.tableName()+" ON "+model.tableName()+"."+join.get(llave).field_name_to+"="+this.table+"."+llave);
				}
			}
		}
		String joins = String.join(" ", join_list);
		this.query = this.query + " " +joins;
	}
	public void setField(Hashtable<String, Criteria> params) {
		// TODO Auto-generated method stub
		if(params.size() > 0){
			Enumeration<String> llaves = params.keys();
			while(llaves.hasMoreElements()){
				String llave = llaves.nextElement();
				try {
					prepare_list.add(params.get(llave).field);
					prepared.put(params.get(llave).field, params.get(llave).field.get(params.get(llave).model));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void update(Hashtable<String,Criteria> params) {
		ArrayList<String> param_list = new ArrayList<String>(); 
		Enumeration<String> llaves = params.keys();
		while(llaves.hasMoreElements()){
			String llave = llaves.nextElement();
			param_list.add(this.table+"."+llave+"=?");
		}
		String parametros = String.join(",", param_list);
		this.query = "UPDATE "+this.table+" SET "+parametros;
	}
}
