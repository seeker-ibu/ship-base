package main.java.com.common.request;

import java.io.Serializable;
import java.util.Map;

public class SimpleRequest implements Request, Serializable {

	private static final long serialVersionUID = 1L;

	private String method;

	private String url;

	private Map<String, String> headers;

	private byte[] body;

	public SimpleRequest() {
	}

	public SimpleRequest( String method, String url, Map<String, String> headers, byte[] body ) {
		this.method = method;
		this.url = url;
		this.headers = headers;
		this.body = body;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod( String method ) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
