package grismar.minstrel;

import java.io.File;
import java.util.Date;

import grismar.argus.Argus;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class Minstrel {
    private static int port = 8000;
    private static Date refresh = null;
    private static String argusURL = "localhost:8008";
    private static String vlcURL = "localhost:8080";
    private static String wwwroot = "./wwwroot";
	
	public static void log(String msg) {
		System.out.println(msg);
	}
	
	public static void error(String msg) {
		System.err.println(msg);
	}
	
	private static void parseCommandLine(String[] args) {
		// define command line options
		Options options = new Options();
		// refresh:
		options.addOption(new Option(
		  "refresh", 
		  "Tell Argus to start refreshing all files after Minstrel startup."));
		// port:
		OptionBuilder.withArgName("port");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription( "Run NanoHTTPD on this port instead of default 8000." );
		options.addOption(OptionBuilder.create("port"));
		// argus:
		OptionBuilder.withArgName("url");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription( "Use Argus at <url> instead of default localhost:8008." );
		options.addOption(OptionBuilder.create("argus"));
		// vlc:
		OptionBuilder.withArgName("url");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Use VLC at <url> instead of default localhost:8080.");
		options.addOption(OptionBuilder.create("vlc"));
		// wwwroot:
		OptionBuilder.withArgName("path");
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("Have NanoHTTPD serve files from <path> instead of default ./wwwroot.");
		options.addOption(OptionBuilder.create("wwwroot"));
		
		// parse command line options and adjust accordingly
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("refresh")) {
				refresh = new Date();
			}
			if (line.hasOption("port")) {
				try {
					port = Integer.parseInt( line.getOptionValue("port"));
				} catch (NumberFormatException e) {
					error("Badly formatted port number, defaulting to 8000. Reason: " + e.getMessage());
					port = 8000;
				}
			}
			if (line.hasOption("argus")) {
				argusURL = line.getOptionValue("argus");
			}
			if (line.hasOption("vlc")) {
				vlcURL = line.getOptionValue("vlc");
			}
			if (line.hasOption("wwwroot")) {
				wwwroot = line.getOptionValue("wwwroot");
			}
		} catch (ParseException e) {
			error("Command line parsing failed. Reason: " + e.getMessage());
		}
	}

    /**
	 * @param args
	 */
	public static void main(String[] args) {
		parseCommandLine(args);
		
		try {
			// TODO for now, this is integrated
			Argus argus = new Argus();
			// TODO check argus

			// Check wwwroot
			File fwwwroot = new File(wwwroot);
			wwwroot = fwwwroot.getCanonicalPath();;
			if (!fwwwroot.exists()) {
				throw new Exception("Path not found (wwwroot) " + wwwroot);
			}
			// Start webserver
			MinstrelHTTPD httpd = new MinstrelHTTPD(port, fwwwroot);

			log( "Listening on port " + port + ". Hit Enter to stop.\n" );
			try { System.in.read(); } catch( Throwable t ) {};
			log( "Terminated OK.\n" );

		} catch (ClassNotFoundException e) {
			error("Unable to initialize SQLite connection: " + e.getMessage());
		} catch (Exception e) {
			error("Exception occurred: " + e.getMessage());
		}
	}

}
