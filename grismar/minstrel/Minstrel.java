package grismar.minstrel;

import grismar.argus.Argus;

public class Minstrel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Argus argus = new Argus();
			argus.update();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
