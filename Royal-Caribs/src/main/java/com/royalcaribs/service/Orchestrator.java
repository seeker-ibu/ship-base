package main.java.com.royalcaribs.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.java.com.common.request.Request;
import main.java.com.common.response.Response;

public interface Orchestrator {

	void sendMessage( ObjectOutputStream outputStream, Request request );

	Response recieveMessage( ObjectInputStream inputStream );

}
