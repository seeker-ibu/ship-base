package main.java.com.serverproxy.service;

import java.io.ObjectOutputStream;

import main.java.com.common.response.Response;

public interface Orchestrator {

	void sendMessage( ObjectOutputStream outputStream, Response response );

}
