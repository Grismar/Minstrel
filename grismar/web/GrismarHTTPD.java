package grismar.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Embedded webserver based on NanoHTTPD, Copyright © 2001,2005-2012 Jarno Elonen <elonen@iki.fi> 
 * and Copyright © 2010 Konstantinos Togias <info@ktogias.gr> - see license.
 * @author Grismar
 * @version Minstrel 0.01
 */
public class GrismarHTTPD extends NanoHTTPD {
	private HashMap<String,ArrayList<RequestListener>> listeners = new HashMap<String,ArrayList<RequestListener>>();

	/**
	 * Constructor, starts the httpd at port <code>port</code> and serves files from <code>wwwroot</code> by default.  
	 * @param port Port number to serve at (http://localhost<code>port</code>/more).
	 * @param wwwroot Directory to serve files from by default (overriden by serve method).
	 * @throws IOException Thrown when wwwroot is not a valid directory.
	 */
	public GrismarHTTPD(int port, File wwwroot) throws IOException {
		super(port, wwwroot);
	}

	/**
	 * Adds a new listener to the httpd, listening for requests with the give <code>uri</code>.
	 * @param listener Class implementing the {@link RequestListener} interface.
	 * @param uri Part of uri after domain, starting with a double slash (//)
	 */
	public void addRequestListener(RequestListener listener, String uri){
		// reference to list of RequestListener
		ArrayList<RequestListener> list = listeners.get(uri);
		// if no list is available for the uri, add a new one
		if (list == null) {
			list = new ArrayList<RequestListener>();
			listeners.put(uri, list);
		}
		// add the listener to the list for the uri
		list.add(listener);
	}
	
	/**
	 * Handle any uri with a double slash (//) after the domain.
	 * @param uri the relevant part of the uri i.e. //some/command.
	 * @param method GET, POST, etc.
	 * @param header All passed headers in key/value pairs.
	 * @param parms All passed parameters in key/value pairs.
	 * @param files Attached files, passed as name/content pairs.
	 * @return Response as it will be served by the httpd.
	 */
	public Response switchedResponse (
			String uri, String method, Properties header, Properties parms, Properties files) {
		
		// reference to list of RequestListener
		ArrayList<RequestListener> list = listeners.get(uri);
		// if no list is available for the uri, return a null response
		if (list == null) {
			return null;
		} else {
			// else iterate over all listeners for the uri and return the final response
			Response response = null;
			for (RequestListener listener : list) {
				response = listener.handleRequest(uri, method, header, parms, files, response);
			}
			return response;
		}
	}
	
	/**
	 * Returns a HTTP response which will be served in response to the request in the parameters. A uri starting with
	 * a double slash (//) will be parsed as a special commmand (for example http://localhost:8000//control/stop). Any
	 * other uri will be passed to the superclass.
	 * @param uri the uri after the domain without parameters i.e. http://server.domain/uri/uri?param=value
	 * @param method GET, POST, etc.
	 * @param header All passed headers in key/value pairs.
	 * @param parms All passed parameters in key/value pairs.
	 * @param files Attached files, passed as name/content pairs.
	 * @return Response as it will be served by the httpd.
	 */
	public Response serve(String uri, String method, Properties header, Properties parms, Properties files){
		// Split uri to test for double slash after domain i.e. http://server.domain//someuri
        String[] uriParts = uri.split("/");
        if ((uriParts.length > 2) && (uriParts[1].equals(""))) {
        	return switchedResponse(uri, method, header, parms, files);
        } else {
        	return super.serve(uri, method, header, parms, files);
        }
	}
}
