package main.java.com.royalcaribs.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import main.java.com.common.request.Request;
import main.java.com.common.request.SimpleRequest;
import main.java.com.common.response.Response;
import main.java.com.common.response.SimpleResponse;

public class ClientRequestProcessor implements RequestProcessor, Runnable {

	private Socket socket;

	private QueueManager queueManager;

	private Orchestrator orchestrator;

	public ClientRequestProcessor( Socket socket, QueueManager queueManager, Orchestrator orchestrator ) {
		super();
		this.socket = socket;
		this.queueManager = queueManager;
		this.orchestrator = orchestrator;
	}

	@Override
	public void process( ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream ) {
		try (InputStream inputStream = socket.getInputStream()) {
			OutputStream outputStream = socket.getOutputStream();

			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );
			String requestLine = bufferedReader.readLine();

			Request request = ( SimpleRequest ) prepareRequest( requestLine, bufferedReader );

			orchestrator.sendMessage( objectOutputStream, request );

			Response response = ( SimpleResponse ) orchestrator.recieveMessage( objectInputStream );

			sendBackToUsers( response, outputStream );

		}
		catch ( Exception e ) {
			System.out.println( "Error occured while processing" );
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		queueManager.add( this );

	}

	private Request prepareRequest( String requestLine, BufferedReader reader ) {
		Request request = null;
		try {
			if ( requestLine == null || requestLine.isEmpty() ) {
				return request;
			}
			String[] parts = requestLine.split( " " );
			String method = parts[0];
			String url = parts[1];

			Map<String, String> headers = new HashMap<>();
			String line;
			while ( !( line = reader.readLine() ).isEmpty() ) {
				int idx = line.indexOf( ":" );
				headers.put( line.substring( 0, idx ).trim(), line.substring( idx + 1 ).trim() );
			}
			System.out.println( "Requesting for url " + url );
			request = new SimpleRequest( method, url, headers, null );
		}
		catch ( Exception e ) {
			System.out.println( "Error occured while preapring the client request " + e );
			e.printStackTrace();
		}
		return request;
	}

	private void sendBackToUsers( Response response, OutputStream outputStream ) {
		try {
			PrintWriter writer = new PrintWriter( outputStream, false );
			writer.printf( "HTTP/1.1 %d OK\r\n", response.getStatus() );
			for ( Map.Entry<String, String> header : response.getHeaders().entrySet() ) {
				writer.printf( "%s: %s\r\n", header.getKey(), header.getValue() );
			}
			writer.printf( "Content-Length: %d\r\n", response.getBody().length );
			writer.print( "\r\n" );

			writer.flush();

			outputStream.write( response.getBody() );
			outputStream.flush();
		}
		catch ( Exception e ) {
			System.out.println( "Error occured while sending back response to the users" + e );
			e.printStackTrace();
		}

	}

}
