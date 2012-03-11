package grismar.minstrel;

import java.io.File;
import java.io.IOException;

import grismar.web.NanoHTTPD;

public class MinstrelHTTPD extends NanoHTTPD {

	public MinstrelHTTPD(int port, File wwwroot) throws IOException {
		super(port, wwwroot);
		// TODO Auto-generated constructor stub
	}

}
