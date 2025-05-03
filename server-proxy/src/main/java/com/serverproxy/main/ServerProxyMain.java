package main.java.com.serverproxy.main;

import java.net.ServerSocket;
import java.net.Socket;

import main.java.com.serverproxy.service.Orchestrator;
import main.java.com.serverproxy.service.ServerRequestProcessor;
import main.java.com.serverproxy.service.ServerResponseOrchestrator;

public class ServerProxyMain {

	private static final int SERVER_PORT = 9090;

	public static void main( String[] args ) {
		ServerSocket serverListner = null;
		Socket clientSocket = null;
		try {
			serverListner = new ServerSocket( SERVER_PORT );
			System.out.println( "Relax, Offshore proxy up and running at port " + SERVER_PORT );

			clientSocket = serverListner.accept();
			System.out.println( "Proxy server started accepting requests" );

			Orchestrator orchestrator = new ServerResponseOrchestrator();

			ServerRequestProcessor processor = new ServerRequestProcessor( clientSocket, orchestrator );
			new Thread( processor ).start();

		}
		catch ( Exception e ) {
			System.out.println( "Error occured while bringing up server proxy" + e );
			e.printStackTrace();
			closeResources( serverListner, clientSocket );
		}

	}

	private static void closeResources( ServerSocket serverListner, Socket clientSocket ) {
		try {
			if ( serverListner != null )
				serverListner.close();
			if ( clientSocket != null )
				clientSocket.close();
		}
		catch ( Exception e ) {
			System.out.println( "Error while closing the resources" + e );
		}

	}

}
