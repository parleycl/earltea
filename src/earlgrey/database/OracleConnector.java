package earlgrey.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.json.JSONException;

import earlgrey.annotations.DatabaseDriver;
import earlgrey.core.ConnectionPool;
import earlgrey.core.Logging;
import earlgrey.core.Properties;
import earlgrey.core.ResourceMaping;
import earlgrey.def.Database;
import earlgrey.error.Error800;
import earlgrey.types.IType;
import earlgrey.types.ObraType;
import oracle.jdbc.OraclePreparedStatement;
import oracle.sql.DATE;

@DatabaseDriver(type="SQL", name="Oracle", id = Database.ORACLE)
public class OracleConnector implements Connector{
	//DECLARAMOS LAS VARIABLES DE BASE DE DATOS
	private Connection con;
	private String user = null, password = null, db = null, host = null;
    private Integer port = null;
	private Statement stmt;
	private OraclePreparedStatement pstm;
	private ResultSet rset;
	private String query;
	private Logging log;
	private ConnectionPool Pool;
	private int prepared_fields = 1;
	private int conected = 0;
	private boolean on_demand = false;
	private boolean datasource_connection = false;
	private String datasource;
	private boolean DEV_MODE;
	//DECLARAMOS LOS CONSTRUCTORES
	public OracleConnector(){
		this.log = new Logging(this.getClass().getName());
		this.DEV_MODE = (Properties.getInstance().getConfigOption("DEVELOPER_MODE").equals("Si")) ? true : false;
	}
	
	public void connect() {
		if(datasource_connection) {
			this.datasourceConnect();
		} else {
			this.manualConnect();
		}
	}
	
	//METODO DE CONECCION -  DEVUELVE EL CONECTOR
	public void manualConnect(){
		if(this.host != null || this.port != null || this.user != null || this.password != null)
		{
			try{
				Class.forName("oracle.jdbc.OraclePreparedStatement", true, ResourceMaping.getInstance().getJARClassLoader());
				Driver driver = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver", true, ResourceMaping.getInstance().getJARClassLoader()).newInstance();
				DriverManager.registerDriver(new DelegatingDriver(driver));
				this.con = DriverManager.getConnection(
				"jdbc:oracle:thin:@"+this.host+":"+this.port+"/"+this.db, this.user,
				this.password);
				this.stmt = this.con.createStatement();
			}
			catch(Exception e){
				try {
					this.log.Info("Probado Source como SSID.");
					this.con = DriverManager.getConnection(
							"jdbc:oracle:thin:@"+this.host+":"+this.port+":"+this.db, this.user,
							this.password);
					this.stmt = this.con.createStatement();
				} catch (SQLException e1) {
					this.log.Critic(e.getMessage(), Error800.DATABASE_CONNECT_ERROR);
				}
			}
		}
		else
		{
			System.out.println("Faltan datos para iniciar una conección con la DB Oracle. ERROR 02");
		}
	}
	
	@Override
	public void datasourceConnect(){
		if(this.datasource_connection) {
			try{
				Context ctx = new InitialContext();
				DataSource ds=(DataSource)ctx.lookup(this.datasource);
				 
				
				this.con= (Connection)ds.getConnection();  
				this.stmt = this.con.createStatement();
			} catch(Exception e){
				this.log.Critic(e.getMessage(), Error800.DATABASE_CONNECT_ERROR);
			}

		} else {
			System.out.println("this Connection is not defined like a datasource connection.");
		}
	}
	
	public ResultSet query(String query){
		try{
			 if (this.DEV_MODE) this.log.Info(query);
			 this.rset = stmt.executeQuery(query);
			 return rset;
		}
		catch(Exception e){
			System.out.println("Consulta erronea: "+query+"\n\rRevise la consulta y vuelva a intentarlo\n\r"+e.getMessage());
			return null;
		}
	}
	//METODO PARA REALIZAR OPERACIONES DE UPDATE
	public boolean update(){
		try{
			 pstm.executeUpdate();
			 return true;
		}
		catch(SQLException e){
			System.out.print("Consulta erronea: "+this.query+"\n\rRevise la consulta y vuelva a intentarlo\n\rError Code:"+e.getErrorCode()+"\n\rSQL State:"+e.getSQLState());
			return false;
		}
	}
	public boolean update(String query){
		try {
			 if (this.DEV_MODE) this.log.Info(query);
			 stmt.executeUpdate(query);
			 return true;
		}
		catch(Exception e){
			System.out.print("Consulta erronea: "+query+"\n\rRevise la consulta y vuelva a intentarlo\n\r"+e.getMessage());
			return false;
		}
	}
	//METODO PARA REALIZAR OPERACIONES DE DELETE
		public boolean delete(String query){
			try {
				 if (this.DEV_MODE) this.log.Info(query);
				 stmt.executeUpdate(query);
				 return true;
			}
			catch(Exception e){
				System.out.print("Consulta erronea: "+query+"\n\rRevise la consulta y vuelva a intentarlo\n\r"+e.getMessage());
				return false;
			}
		}
	//METODO DE CIERRE DE CONECCION A LA DB
	public void closeLink(){
		try
		{
			if(this.con != null){
				this.con.close();
				this.con = null;
			}
		}
		catch(Exception e){
			System.out.println("No se puede cerrar el conector, ya se encuentra cerrado o existe un error. ERROR 03");
		}
	}
	public void closeStmt(){
		try
		{
			if(this.stmt != null){
				this.stmt.close();
				this.stmt = null;
			}
		}
		catch(Exception e){
			System.out.println("No se puede cerrar el Statement, ya se encuentra cerrado o existe un error. ERROR 04");
		}
	}
	public void closeRset(){
		try
		{
			if(this.rset != null){
				this.rset.close();
				this.rset = null;
			}
		}
		catch(Exception e){
			System.out.println("No se puede cerrar la consulta, ya se encuentra cerrado o existe un error. ERROR 035");
		}
	}
	public void close(){
		//System.out.println("Cerrando conector Oracle");
		try
		{
			this.closeStmt();
		}
		catch(Exception e){
		}
		try
		{
			this.closeRset();
		}
		catch(Exception e){
		}
		try
		{
			this.closeLink();
		}
		catch(Exception e){
		}
		this.Pool.closeConnection(this);
	}
	public void closeQuery(){
		try
		{
			this.closeStmt();
			this.closeLink();
		}
		catch(Exception e){
		}
	}
	public void closeOP(){
		try
		{
			this.closeStmt();
			this.closeLink();
		}
		catch(Exception e){
		}
	}
	//METODOS DE TESTEO
	public boolean TestConector(){
		this.log.Info("Oracle Driver: Connection test.");
		try {
			Driver driver = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver", true, ResourceMaping.getInstance().getJARClassLoader()).newInstance();
			DriverManager.registerDriver(new DelegatingDriver(driver));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			this.log.Critic("The JDBC Oracle driver is not present in the libraries loaded", Error800.DATABASE_ORACLE_JDBC);
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.log.Info("The JDCB driver is in the libraries loaded, testing connection.");
		Connection connection = null;
		try {
			int timeout = DriverManager.getLoginTimeout();
			DriverManager.setLoginTimeout(2);
			this.log.Info("Testing Source like service.");
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+this.host+":"+this.port+"/"+this.db, this.user,
					this.password);
			connection.close();
			DriverManager.setLoginTimeout(timeout);
		} catch (SQLException e) {
			try {
				this.log.Info("Testing Source like SSID.");
				int timeout = DriverManager.getLoginTimeout();
				DriverManager.setLoginTimeout(2);
				connection = DriverManager.getConnection(
						"jdbc:oracle:thin:@"+this.host+":"+this.port+":"+this.db, this.user,
						this.password);
				connection.close();
			} catch (SQLException e1) {
				this.log.Critic(e.getMessage(), Error800.DATABASE_CONNECT_ERROR);
				return false;
			}
		}
		if (connection != null) {
			this.log.Info("Oracle Driver: Connection Successful.");
			return true;
		} else {
			this.log.Critic("There is an error trying connecting to database", Error800.DATABASE_CONNECT_ERROR);
			return false;
		}
	}
	// NUEVAS FUNCIONES
	public OraclePreparedStatement prepare(String query, Field primarykey){
		 try {
			if (this.DEV_MODE) this.log.Info(query); 
			this.query = query;
			this.prepared_fields = 1;
			if(primarykey != null){
				this.pstm  = (OraclePreparedStatement)this.con.prepareStatement(query, new String[] {primarykey.getName()});
			}
			else
			{
				this.pstm  = (OraclePreparedStatement)this.con.prepareStatement(query);
			}
			return this.pstm;
		} catch (SQLException e) {
			System.out.println("IMPOSIBLE GENERAR STATMENT, ERROR DE CONSULTA: "+query+"\n\r"+e.getMessage());
			return null;
		}
	}
	// NUEVAS FUNCIONES
	public OraclePreparedStatement prepareInsert(String query){
		 try {
			if (this.DEV_MODE) this.log.Info(query); 
			this.query = query;
			this.prepared_fields = 1;
			this.pstm  = (OraclePreparedStatement)this.con.prepareStatement(query);
			return this.pstm;
		} catch (SQLException e) {
			System.out.println("IMPOSIBLE GENERAR STATMENT, ERROR DE CONSULTA: "+query+"\n\r"+e.getMessage());
			return null;
		}
	}
	public ResultSet execute(){
		try{
			 this.rset = pstm.executeQuery();
			 return rset;
		}
		catch(Exception e){
			System.out.println("Consulta erronea: "+e.getMessage());
		}
		return null;
	}
	public boolean commit(){
		try {
			this.con.commit();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	@Override
	public void setCredencial(String user, String password, String db, String host, int port) {
		// TODO Auto-generated method stub
		this.user = user;
		this.password = password;
		this.db = db;
		this.host = host;
		this.port = port;
	}
	@Override
	public void setPool(ConnectionPool pool) {
		this.Pool = pool;
	}
	@Override
	public void complete(Hashtable<Field, Object> prepare_fields, ArrayList<Field> arrayList, Hashtable<Field, Object> prepare_match_fields, ArrayList<Field> prepare_match_List) {
		// TODO Auto-generated method stub
		for(int i=0;i<arrayList.size();i++){
			Field campo = arrayList.get(i);
			if(campo.getType().equals(int.class) || campo.getType().equals(Integer.class)){
				try {
					this.pstm.setInt(this.prepared_fields++, (Integer)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(float.class) || campo.getType().equals(Float.class)){
				try {
					this.pstm.setFloat(this.prepared_fields++, (Float)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(double.class) || campo.getType().equals(Double.class)){
				try {
					this.pstm.setDouble(this.prepared_fields++, (Double)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(Timestamp.class)){
				try {
					this.pstm.setTimestamp(this.prepared_fields++, (Timestamp)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else if(campo.getType().equals(DATE.class)){
				try {
					this.pstm.setDATE(this.prepared_fields++, (DATE)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else if(campo.getType().equals(String.class)){
				try {
					this.pstm.setString(this.prepared_fields++, (String)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(IType.class.isAssignableFrom(campo.getType())){
				try {
					Method inv = campo.getType().getMethod("SQLPrepareField", OraclePreparedStatement.class, Integer.class);
					inv.invoke(null, this.pstm, this.prepared_fields++);
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
		for(int i=0;i<prepare_match_List.size();i++){
			Field campo = prepare_match_List.get(i);
			if(campo.getType().equals(int.class) || campo.getType().equals(Integer.class)){
				try {
					this.pstm.setInt(this.prepared_fields++, (Integer)prepare_match_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(float.class) || campo.getType().equals(Float.class)){
				try {
					this.pstm.setFloat(this.prepared_fields++, (Float)prepare_match_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(double.class) || campo.getType().equals(Double.class)){
				try {
					this.pstm.setDouble(this.prepared_fields++, (Double)prepare_match_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(Timestamp.class)){
				try {
					this.pstm.setTimestamp(this.prepared_fields++, (Timestamp)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(DATE.class)){
				try {
					this.pstm.setDATE(this.prepared_fields++, (DATE)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(String.class)){
				try {
					this.pstm.setString(this.prepared_fields++, "%"+(String)prepare_match_fields.get(campo)+"%");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(IType.class.isAssignableFrom(campo.getType())){
				try {
					Method inv = campo.getType().getMethod("SQLPrepareField", OraclePreparedStatement.class, Integer.class);
					inv.invoke(null, this.pstm, this.prepared_fields++);
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
	}
	@Override
	public void startTransaction() {
		// TODO Auto-generated method stub
		try {
			if (this.DEV_MODE) this.log.Info("Start transaction");
			this.con.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean finishTransaction() {
		// TODO Auto-generated method stub
		try {
			if (this.DEV_MODE) this.log.Info("Finish transaction");
			this.con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}
	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		try {
			this.con.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean executeUpdate() {
		try{
			 pstm.executeUpdate();
			 return true;
		}
		catch(Exception e){
			System.out.println("Consulta erronea: "+e.getMessage());
		}
		return false;
	}
	@Override
	public void closeTransaction() {
		// TODO Auto-generated method stub
		try {
			this.con.setAutoCommit(true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void complete(Hashtable<Field, Object> prepare_fields, ArrayList<Field> arrayList) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		for(int i=0;i<arrayList.size();i++){
			Field campo = arrayList.get(i);
			if(campo.getType().equals(int.class) || campo.getType().equals(Integer.class)){
				try {
					this.pstm.setInt(this.prepared_fields++, (Integer)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(float.class) || campo.getType().equals(Float.class)){
				try {
					this.pstm.setFloat(this.prepared_fields++, (Float)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(double.class) || campo.getType().equals(Double.class)){
				try {
					this.pstm.setDouble(this.prepared_fields++, (Double)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(Timestamp.class)){
				try {
					this.pstm.setTimestamp(this.prepared_fields++, (Timestamp)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(campo.getType().equals(DATE.class)){
				try {
					this.pstm.setDATE(this.prepared_fields++, (DATE)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else if(campo.getType().equals(String.class)){
				try {
					this.pstm.setString(this.prepared_fields++, (String)prepare_fields.get(campo));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(IType.class.isAssignableFrom(campo.getType())){
				try {
					Method inv = campo.getType().getMethod("SQLPrepareField", OraclePreparedStatement.class, Integer.class, ObraType.class, Connection.class);
					inv.invoke(null, this.pstm, this.prepared_fields++, prepare_fields.get(campo), this.con);
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
	}
	@Override
	public int getLastInsertedId() throws SQLException {
		ResultSet rs = this.pstm.getGeneratedKeys();
		if (rs.next()){
		    // The generated id
		    return rs.getInt(1);
		}
		return -1;
	}
	
	@Override
	public void setDemand(boolean demand) {
		this.on_demand = demand;
	}
	
	@Override
	public void setDatasource(String datasource) {
		this.datasource_connection = true;
		this.datasource = datasource;
	}
	
	@Override
	public void checkConnection() {
		if(this.con == null){
			this.connect();
		}
	}
	
	@Override
	public void checkCloseConnection() {
		if(this.on_demand){
			this.close();
		}
	}
}
