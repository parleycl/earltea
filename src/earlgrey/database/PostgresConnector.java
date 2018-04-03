package earlgrey.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import earlgrey.annotations.DatabaseDriver;
import earlgrey.core.ConnectionPool;
import earlgrey.core.ModelCore;
import oracle.jdbc.OraclePreparedStatement;

@DatabaseDriver(type="SQL", name="Postgresql")
public class PostgresConnector implements Connector{
	private Connection con;
	private String user = null, password = null, db = null, host = null, port = null;
	private Statement stmt;
	private OraclePreparedStatement pstm;
	private ResultSet rset;
	private String query;
	// CONSTRUCTOR
	public PostgresConnector(){
		this.user = user;
		this.password = password;
		this.db = db;
		this.host = host;
		this.port = port;
	}
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultSet query(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PreparedStatement prepare(String query, Field primarykey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean commit() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setCredencial(String user, String password, String db, String host, String port) {
		// TODO Auto-generated method stub
		this.user = user;
		this.password = password;
		this.db = db;
		this.host = host;
		this.port = port;
	}
	@Override
	public boolean TestConector() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setPool(ConnectionPool pool) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void complete(Hashtable<Field, Object> prepare_fields, ArrayList<Field> arrayList, Hashtable<Field, Object> prepare_match_fields, ArrayList<Field> prepare_match_List) {
		// TODO Auto-generated method stub
		Enumeration<Field> keys = prepare_fields.keys();
	}
	@Override
	public ResultSet execute() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean finishTransaction() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean executeUpdate() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void closeTransaction() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void complete(Hashtable<Field, Object> prepared, ArrayList<Field> prepared_list) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getLastInsertedId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
