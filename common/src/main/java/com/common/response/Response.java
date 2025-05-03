package main.java.com.common.response;

import java.util.Map;

public interface Response {

	int getStatus();

	Map<String, String> getHeaders();

	byte[] getBody();

}
