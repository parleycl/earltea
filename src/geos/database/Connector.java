package geos.database;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import geos.core.DatabasePool;
import geos.core.ModelCore;

public interface Connector {
	public boolean busy = false;
	public void setCredencial(String user,String password, String db, String host, String port);
	public void setPool(DatabasePool pool);
	public void connect();
	public boolean TestConector();
	public ResultSet query(String query);
	public boolean update(String query);
	public boolean delete(String query);
	public void close();
	public PreparedStatement prepare(String query);
	public boolean commit();
	public void complete(Hashtable<Field, Object> prepare_fields, ArrayList<Field> arrayList);
	public ResultSet execute();
}
