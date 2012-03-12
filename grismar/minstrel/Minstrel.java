package grismar.minstrel;

import grismar.argus.Argus;
import grismar.common.Log;
import grismar.minstrel.MinstrelParams;
import grismar.web.GrismarHTTPD;
import java.io.File;

public class Minstrel {
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		MinstrelParams params = new MinstrelParams();
		params.parseCommandLine(args);
		
		try {
			// TODO for now, this is integrated
			Argus argus = new Argus();
			// TODO check argus

			// Check wwwroot
			File fwwwroot = new File(params.wwwroot);
			params.wwwroot = fwwwroot.getCanonicalPath();;
			if (!fwwwroot.exists()) {
				throw new Exception("Path not found (wwwroot) " + params.wwwroot);
			}
			
			// Start webserver
			GrismarHTTPD httpd = new GrismarHTTPD(params.port, fwwwroot);
			
			Log.message( "Listening on port " + params.port + ". Hit Enter to stop.\n" );

			MinstrelHandler handler = new MinstrelHandler(httpd);
			MinstrelLock lock = new MinstrelLock(httpd);
			
			// TODO replace simple wait with more graceful exit
			// try { System.in.read(); } catch( Throwable t ) {};
			Log.message( "Terminated OK.\n" );

		} catch (ClassNotFoundException e) {
			Log.error("Unable to initialize SQLite connection: " + e.getMessage());
		} catch (Exception e) {
			Log.error("Exception occurred: " + e.getMessage());
		}
	}
}
