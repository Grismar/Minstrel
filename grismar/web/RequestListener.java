package grismar.web;

import grismar.web.NanoHTTPD.Response;

import java.util.Properties;

/**
 * Interface for subscribers to command requests to {@link GrismarHTTPD}.
 * @author grismar
 *
 */
public interface RequestListener {
	/**
	 * Callback for subscribers to a specific command uri. Return a valid {@link NanoHTTPD#Response} or <code>null</code>.  
	 * {@link GrismarHTTPD#addRequestListener(RequestListener)}
	 * @param uri
	 * @param uriParts
	 * @param method
	 * @param header
	 * @param parms
	 * @param files
	 * @param response
	 * @return
	 */
	Response handleRequest(
			String uri, String method, Properties header, Properties parms, Properties files, Response response);
}
