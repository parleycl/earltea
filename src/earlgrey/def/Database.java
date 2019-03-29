package earlgrey.def;

public class Database {
	public static final int UNKNOW = 0;
	public static final int DISK = 1;
	public static final int ORACLE = 2;
	public static final int MYSQL = 3;
	public static final int POSTGRES = 4;
	public static final int SQLITE = 5;
	public static final int MONGO = 6;
	public static final int MARIADB = 7;
	
	private static final String[] databases = {
			"UNKNOW",
			"DISK",
			"ORACLE",
			"MYSQL",
			"POSTGRES",
			"SQLITE",
			"MONGO",
			"MARIADB"
	};
	
	public static String toString(int type){
		return databases[type];
	}
}
