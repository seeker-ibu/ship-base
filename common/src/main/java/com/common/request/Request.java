package main.java.com.common.request;

import java.util.Map;

public interface Request {

	String getUrl();

	String getMethod();

	Map<String, String> getHeaders();

	byte[] getBody();

}
