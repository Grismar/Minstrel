package grismar.argus;

import grismar.argus.ArgusData;
import java.sql.Array;

public class Argus {
	private ArgusData data;
	
	public Argus() throws ClassNotFoundException {
		data = new ArgusData();
	}
	
	public Array loadFileTypes() {
		return data.getFirstColumn("select extension from filetypes");
	}
	
	public Array loadLocations() {
		return data.getFirstColumn("select path from locations");
	}
	
	public void update() {
		Array filetypes = loadFileTypes();
		Array locations = loadLocations();
		
		/**/
		
		System.out.println("Success?");
	}
}
