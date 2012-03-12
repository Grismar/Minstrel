package grismar.minstrel;

import grismar.web.GrismarHTTPD;
import grismar.web.NanoHTTPD;
import grismar.web.NanoHTTPD.Response;
import grismar.web.RequestListener;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Once instantiated, this class will wait for the handleRequest callback to be called.
 * Calling it will release the lock, allowing the thread (and Minstrel) to continue and finish.
 * @author grismar
 *
 */
public class MinstrelLock implements RequestListener {
	final static Lock lock = new ReentrantLock();
	final static Condition stop = lock.newCondition();

	GrismarHTTPD httpd;
	
	/**
	 * Constructor sets up the RequestListener.
	 * @param httpd {@link GrismarHTTPD} on which //control/stop uri should be finished to release the lock.
	 * @throws InterruptedException
	 */
	MinstrelLock(GrismarHTTPD aHttpd) throws InterruptedException {
		httpd = aHttpd;
		httpd.addRequestListener(this, "//control/stop");

		lock.lock();
		try {
			// wait for signal, indefinitely
			stop.await();
			// wait for 500 milliseconds to allow Response
			stop.await(500, TimeUnit.MILLISECONDS);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Event handler, part of the {@link RequestListener} interface.
	 */
	public Response handleRequest(
			String uri, String method, Properties header, Properties parms, Properties files, Response response) {
		lock.lock();
		try {
			stop.signal();
		} finally {
			lock.unlock();
		}
		return httpd.new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_HTML, "<htm><head><title>Minstrel shutdown</title></head><body>Minstrel is shutting down...</body></html>");
	}
}
