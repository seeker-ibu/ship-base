package main.java.com.serverproxy.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.common.request.Request;
import main.java.com.common.request.SimpleRequest;
import main.java.com.common.response.SimpleResponse;

public class ServerRequestProcessor implements RequestProcessor, Runnable {

	public static final int EIGHT_KB = 8192;

	private ObjectInputStream objectInputStream;

	private ObjectOutputStream objectOutputStream;

	private Orchestrator orchestrator;

	public ServerRequestProcessor( Socket serverSocket, Orchestrator orchestrator ) {
		try {
			this.objectOutputStream = new ObjectOutputStream( serverSocket.getOutputStream() );
			objectOutputStream.flush();
			this.objectInputStream = new ObjectInputStream( serverSocket.getInputStream() );

			this.orchestrator = orchestrator;
		}
		catch ( Exception e ) {
			System.out.println( "Error occured at server request processor" );
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while ( true ) {
				Request request = ( SimpleRequest ) objectInputStream.readObject();
				String rawUrl = request.getUrl();
				if ( !rawUrl.startsWith( "http://" ) && !rawUrl.startsWith( "https://" ) ) {
					rawUrl = "https://" + rawUrl;
				}

				HttpURLConnection connection = ( HttpURLConnection ) new URL( rawUrl ).openConnection();
				connection.setRequestMethod( request.getMethod() );

				for ( Map.Entry<String, String> entry : request.getHeaders().entrySet() ) {
					connection.setRequestProperty( entry.getKey(), entry.getValue() );
				}

				connection.setDoInput( true );

				byte[] requestBody = request.getBody();
				if ( requestBody != null && requestBody.length > 0 ) {
					connection.setDoOutput( true );
					try (OutputStream reqOut = connection.getOutputStream()) {
						reqOut.write( requestBody );
						reqOut.flush();
					}
				}

				int statusCode = connection.getResponseCode();

				InputStream responseStream = ( statusCode >= 400 ) ? connection.getErrorStream() : connection.getInputStream();

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				byte[] temp = new byte[EIGHT_KB];
				int n;
				while ( ( n = responseStream.read( temp ) ) != -1 ) {
					buffer.write( temp, 0, n );
				}

				Map<String, List<String>> rawHeaders = connection.getHeaderFields();
				Map<String, String> responseHeaders = new HashMap<>();
				for ( Map.Entry<String, List<String>> entry : rawHeaders.entrySet() ) {
					String key = entry.getKey();
					List<String> value = entry.getValue();
					if ( key != null && value != null && !value.isEmpty() ) {
						responseHeaders.put( key, String.join( ", ", value ) );
					}
				}

				SimpleResponse response = new SimpleResponse();
				response.setStatus( statusCode );
				response.setBody( buffer.toByteArray() );
				response.setHeaders( responseHeaders );

				orchestrator.sendMessage( objectOutputStream, response );

				objectOutputStream.reset();
			}
		}
		catch ( Exception e ) {
			System.out.println( "Error occurred while processing the request or response" );
			e.printStackTrace();
		}
	}
}
