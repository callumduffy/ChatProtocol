package chatProtocol;

import java.io.*;
import java.net.*;
import java.util.*;

public class server{
	public static void main(String[] args) throws IOException {
		//have to modify to use args for port number
		int portNumber = 6789;
	    boolean listening = true;

        
	    try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
	    	while (listening) {
	    		new ServerThread(serverSocket.accept()).start();
	    	}
	    } catch (IOException e) {
	    	System.err.println("Could not listen on port " + portNumber);
	    	System.exit(-1);
	    }
	}
}