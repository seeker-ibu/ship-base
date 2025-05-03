package main.java.com.royalcaribs.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.java.com.common.request.Request;
import main.java.com.common.response.Response;
import main.java.com.common.response.SimpleResponse;

public class MessageOrchestrator implements Orchestrator {

	@Override
	public void sendMessage( ObjectOutputStream outputStream, Request request ) {
		try {
			outputStream.writeObject( request );
			outputStream.flush();
		}
		catch ( Exception e ) {
			System.out.println( "Orchestrtor sending message failed " + e );
			e.printStackTrace();
		}
	}

	@Override
	public Response recieveMessage( ObjectInputStream inputStream ) {
		Response response = null;
		try {
			response = ( SimpleResponse ) inputStream.readObject();
		}
		catch ( Exception e ) {
			System.out.println( "Orchestrtor reading message failed " + e );
		}
		return response;
	}

}
