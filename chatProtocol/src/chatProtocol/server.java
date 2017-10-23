package chatProtocol;

import java.io.*;
import java.net.*;

class chatProtocol{
	public static void main(String argv[]) throws Exception {
		
		String clientSentence, capitalisedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		
			while(true) {
				Socket conSocket = welcomeSocket.accept();
				
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
						conSocket.getInputStream()));
				
				DataOutputStream outToClient = new
						DataOutputStream(conSocket.getOutputStream());
						clientSentence = inFromClient.readLine();
						capitalisedSentence = clientSentence.toUpperCase() + "\n";
						outToClient.writeBytes(capitalisedSentence);
		}
	}				
}