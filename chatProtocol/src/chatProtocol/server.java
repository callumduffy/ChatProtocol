package chatProtocol;

import java.io.*;
import java.net.*;

public class Server{
	public static void main(String argv[]) throws Exception {
		
		String clientSentence, capitalisedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		int test=0;
		
		while(true) {
				if(test==0){
					System.out.println("Running");
					test++;
				}
				//packet to send back to the client
				String packet;
				Socket conSocket = welcomeSocket.accept();
				
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
						conSocket.getInputStream()));
				
				DataOutputStream outToClient = new
						DataOutputStream(conSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				capitalisedSentence = clientSentence.toUpperCase();
				System.out.println(capitalisedSentence);
				packet = check(capitalisedSentence);
				outToClient.write(packet.getBytes());
				System.out.println("Got");
		}
			
	}	
	
	public static String check(String input){
		String output = null;
		//for now type in IP, in future have it set as argument to run code
		if(input.contains("HELO")){
			output = input + "\nIP:134.226.50.38\nPort:6789\nStudentID:14315135";
		} //need to perform string manipulation on the input stream
		else if(input.contains("JOIN_CHATROOM:")){
			output = "JOINED_CHATROOM: room1 SERVER_IP: 0\nPORT: 0\nROOM_REF: 123\nJOIN_ID: 0";
		}
		
		return output;
	}
}