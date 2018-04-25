package earlgrey.core;

public class FilesList {
	// Core files
	public static FilePackage Core = new FilePackage("earlgrey.core", new String[]{"CacheCore", "Communication", "ConnectionPool", "ControllerCore", "DatasourceManager",
			"Engine", "Gear", "HttpRequest", "HttpResponse", "Earlgrey", "Logging", "ModelCore", "Packet", "Persistence", "Properties", "ResourceMaping", "Router", 
			"Session"});
	
	// Console Files
	public static FilePackage Console = new FilePackage("earlgrey.admin", new String[]{"LogsConsole", "PropertiesManager", "UserConsole", "Datasources"});
	// Anotations Files
	public static FilePackage Annotations = new FilePackage("earlgrey.annotations", new String[]{"AddPropertie", "AddPropertieArray", "AddPropertieOption", "AddPropertieSet", "AddPropertieSetTemplate", "AutoIncrement", "Cache", "Console", "Controller",
    "ControllerAction", "Cronwork", "DatabaseDriver", "ErrorCode", "GroupParamOptional", "GroupParamRequire", "GroupPropertieOption", "GroupProperties", "GroupPropertiesArray",
    "GroupPropertieSet", "GroupPropertieSetTemplate", "Model", "ModelAutoIncrement", "ModelCollection", "ModelField", "ModelJoin", "ModelJoins", "ModelRelation", "ModelSpecialQuery", 
    "ModelTransaction", "ParamOptional", "ParamRequire", "Policie", "PrimaryKey", "Required", "Route", "Unique"});
    // Database files
	public static FilePackage Database = new FilePackage("earlgrey.database", new String[]{"Connector", "ConnectorDescriptor", "DelegatingDriver", "Mysql", "OracleConnector", "PostgresConnector", "QueryBuilder"});
    // Def files
	public static FilePackage Def = new FilePackage("earlgrey.def", new String[]{"ActionDef", "ErrorDef", "HttpActionDef", "JobDef", "ModelRest", "RelationDef", "RouteDef", "SessionDef"});
    // Error Files
	public static FilePackage Error = new FilePackage("earlgrey.error", new String[]{"Error500", "Error60", "Error70", "Error700", "Error800", "ErrorBase"});
	// Gateway FilesSS
	public static FilePackage Gateway = new FilePackage("earlgrey.gateway", new String[]{"Channel", "Websocket"});
    // Http files
	public static FilePackage Http = new FilePackage("earlgrey.http", new String[]{"Response200", "Response300", "Response500"});
    // Interface files
	public static FilePackage Interface = new FilePackage("earlgrey.interfaces", new String[]{"ModelInterface", "ModelTransactionInterface", "PolicieCore", "Process", "PropertiesDepend", "Request", "Response"});
    // Policies Files
	public static FilePackage Policies = new FilePackage("earlgrey.policies", new String[]{"Admin", "AllPass", "SignIn"});
	// Security files
	public static FilePackage Security = new FilePackage("earlgrey.security", new String[]{"Keys"});	
	// Types Files
	public static FilePackage Types = new FilePackage("earlgrey.types", new String[]{"CentroideType", "GeometriaType", "IType", "ObraType", "Type"});
    // Utils files
	public static FilePackage Utils = new FilePackage("earlgrey.utils", new String[]{"ConversorCoordenadas", "GeoAlgorithm", "Hash", "JWT"});
	// LIST
	public static FilePackage [] List = { Core, Console, Annotations, Database, Def, Error, Gateway, Http, Interface, Policies, Security, Types, Utils }; 
}