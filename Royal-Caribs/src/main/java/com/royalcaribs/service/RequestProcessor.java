package main.java.com.royalcaribs.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface RequestProcessor {

	void process( ObjectOutputStream outputStream, ObjectInputStream inputStream );

}
