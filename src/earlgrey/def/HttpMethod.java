package earlgrey.def;

public class HttpMethod {
	public static final int UNKNOW = -1;
	public static final int GET = 1;
	public static final int POST = 2;
	public static final int PUT = 3;
	public static final int DELETE = 4;
	public static final int PATCH = 5;
	public static final int OPTIONS = 6;
	public static final int PURGE = 7;
	public static final int HEAD = 8;
	
	public static String toString(int method) {
		switch(method) {
			case 1:
				return "GET";
			case 2:
				return "POST";
			case 3:
				return "PUT";
			case 4:
				return "DELETE";
			case 5:
				return "PATCH";
			case 6:
				return "OPTIONS";
			case 7:
				return "PURGE";
			case 8:
				return "HEAD";
			default:
				return "UNKNOW";
		}
	}
}
