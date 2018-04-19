package earlgrey.error;

import earlgrey.annotations.ErrorCode;

public class Error800 extends ErrorBase{
	@ErrorCode(description="The database driver could not be loaded dynamically.", code=801)
	public static int DATABASE_DRIVER_ERROR = 801;
	@ErrorCode(description="Earlgrey cant set connection to the database.", code=802)
	public static int DATABASE_CONNECT_ERROR = 802;
	@ErrorCode(description="The JDBC Oracle library is not available.", code=803)
	public static int DATABASE_ORACLE_JDBC = 803;
	@ErrorCode(description="There no a database driver to set a connection with database.", code=804)
	public static int DATABASE_NO_DRIVER = 804;
	@ErrorCode(description="There was an error in the Sql set when Earlgrey tryed to read it.", code=805)
	public static int DATABASE_SQL_SET = 805;
}
