package earlgrey.types;

import oracle.jdbc.OraclePreparedStatement;

public interface IType {
	public static String GetSQLQuery(String field, String table) {
		return null;
	}
	public static Object GetSQLResult() {
		return null;
	}
	public static void SQLPrepareField(OraclePreparedStatement pstm, int number){
	}
}
