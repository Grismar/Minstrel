package grismar.minstrel;

import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import grismar.common.Log;

public class MinstrelParams {
	// params with their defaults
	public int port = 8000;
    public Date refresh = null;
    public String argusURL = "localhost:8008";
    public String vlcURL = "localhost:8080";
    public String wwwroot = "./wwwroot";
	
    // parse arguments passed to the application, set params accordingly
	public void parseCommandLine(String[] args) {
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
					Log.error("Badly formatted port number, defaulting to 8000. Reason: " + e.getMessage());
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
			Log.error("Command line parsing failed. Reason: " + e.getMessage());
		}
	}
}
