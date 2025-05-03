package main.java.com.royalcaribs.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueueManager implements QueueManager {

	private final BlockingQueue<RequestProcessor> queue = new LinkedBlockingQueue<>();

	private ObjectOutputStream objectOutputStream;

	private ObjectInputStream objectInputStream;

	public RequestQueueManager( Socket socket ) {
		try {
			this.objectOutputStream = new ObjectOutputStream( socket.getOutputStream() );
			this.objectOutputStream.flush();
			this.objectInputStream = new ObjectInputStream( socket.getInputStream() );
			initiate();
		}
		catch ( Exception e ) {
			System.out.println( "Error occured while initiating request manager" + e );
			e.printStackTrace();
		}
	}

	private void initiate() {
		Thread thread = new Thread( () -> {
			while ( true ) {
				try {
					RequestProcessor processor = queue.take();
					processor.process( objectOutputStream, objectInputStream );
				}
				catch ( Exception e ) {
					System.out.println( "Error occured while initiating the procesors " + e );
				}
			}
		} );
		thread.setDaemon( true );
		thread.start();

	}

	@Override
	public void add( RequestProcessor processor ) {
		queue.add( processor );
	}

}
