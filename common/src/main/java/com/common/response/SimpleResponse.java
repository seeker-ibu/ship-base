package main.java.com.common.response;

import java.io.Serializable;
import java.util.Map;

public class SimpleResponse implements Response, Serializable {

	private static final long serialVersionUID = 1L;

	private int status;

	private Map<String, String> headers;

	private byte[] body;

	public SimpleResponse( int status, Map<String, String> headers, byte[] body ) {
		super();
		this.status = status;
		this.headers = headers;
		this.body = body;
	}

	public SimpleResponse() {
		super();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus( int status ) {
		this.status = status;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders( Map<String, String> headers ) {
		this.headers = headers;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody( byte[] body ) {
		this.body = body;
	}

}
