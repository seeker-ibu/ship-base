package main.java.com.royalcaribs.main;

import java.net.ServerSocket;
import java.net.Socket;

import main.java.com.royalcaribs.service.ClientRequestProcessor;
import main.java.com.royalcaribs.service.MessageOrchestrator;
import main.java.com.royalcaribs.service.Orchestrator;
import main.java.com.royalcaribs.service.QueueManager;
import main.java.com.royalcaribs.service.RequestQueueManager;

public class ClientProxyMain {

	private static final int CLIENT_PORT = 8080;

	private static final int SERVER_PORT = 9090;

	private static final String SERVER_HOST = "127.0.0.1";

	private static ServerSocket proxyListner;

	public static void main( String[] args ) {
		try {
			Socket proxyServerSocket = new Socket( SERVER_HOST, SERVER_PORT );
			QueueManager requestQueueManager = new RequestQueueManager( proxyServerSocket );
			Orchestrator orchestrator = new MessageOrchestrator();

			proxyListner = new ServerSocket( CLIENT_PORT );
			System.out.println( "RELAX, SHIP PROXY IS UP! AND RUNNING AT PORT 8080..." );

			while ( true ) {
				Socket proxyClientSocket = proxyListner.accept();
				System.out.println( "Proxy client started accepting requests" );

				Thread proxyThread = new Thread( new ClientRequestProcessor( proxyClientSocket, requestQueueManager, orchestrator ) );
				proxyThread.start();
			}

		}
		catch ( Exception e ) {
			System.out.println( "Error in client proxy,looks like no internet today!" + e );
			e.printStackTrace();
		}
	}

}
