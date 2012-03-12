package grismar.common;

public class Log {
	public static void message(String msg) {
		System.out.println(msg);
	}
	
	public static void error(String msg) {
		System.err.println(msg);
	}
}
