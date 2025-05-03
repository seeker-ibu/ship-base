package main.java.com.serverproxy.service;

import java.io.ObjectOutputStream;

import main.java.com.common.response.Response;

public class ServerResponseOrchestrator implements Orchestrator {

	@Override
	public void sendMessage( ObjectOutputStream outputStream, Response response ) {
		try {
			outputStream.writeObject( response );
			outputStream.flush();
		}
		catch ( Exception e ) {
			System.out.println( "Orchestrtor sending message failed " + e );
			e.printStackTrace();
		}
	}

}
