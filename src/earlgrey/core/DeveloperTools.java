package earlgrey.core;

import earlgrey.annotations.AddPropertie;

@AddPropertie(defaultTo = "true", name = "DEVELOPER_MODE")
public class DeveloperTools {
	public static int flag_counter = 1;
	public static boolean dev_mode = true;
	public static void flag(){
		if(dev_mode) System.out.println(flag_counter++);
	}
	public static void flag(Class<?> clase){
		if(dev_mode) System.out.println(clase.getName()+":"+flag_counter++);
	}
	public static void flag(String clase){
		if(dev_mode) System.out.println(clase+":"+flag_counter++);
	}
}
