package grismar.minstrel;

import java.util.Properties;

import grismar.web.GrismarHTTPD;
import grismar.web.NanoHTTPD;
import grismar.web.NanoHTTPD.Response;
import grismar.web.RequestListener;

public class MinstrelHandler implements RequestListener {
	GrismarHTTPD httpd;
	
	MinstrelHandler(GrismarHTTPD aHttpd){
		httpd = aHttpd;
		httpd.addRequestListener(this, "//control/hello");
	}
	
	@Override
	public Response handleRequest(
			String uri, String method, Properties header, Properties parms, Properties files, Response response) {
		if (uri.equals("//control/hello")) {
			return httpd.new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, "Hello, World!");
		}
		return null;
	}
}
