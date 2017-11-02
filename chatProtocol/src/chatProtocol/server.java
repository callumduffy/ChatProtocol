package chatProtocol;

import java.io.*;
import java.net.*;

public class server{
	public static void main(String argv[]) throws Exception {
		
		String clientSentence, capitalisedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		
		String ip = "134.226.50.35";
		int port =6789;
		int id = 14315135;
		String roomName = null;
		int client_ip;
		int tcpPort;
		String clientName= null;
		String temp = null;

		while(true) {
				//packet to send back to the client
				String packet;
				Socket conSocket = welcomeSocket.accept();
				//open reader
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
						conSocket.getInputStream()));
				
				DataOutputStream outToClient = new
						DataOutputStream(conSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				capitalisedSentence = clientSentence.toUpperCase();
				int type = typeCheck(capitalisedSentence);
				
				if(type ==0 ){
					outToClient.write(makeHELO(ip,port,id).getBytes());
				}
				else if(type==1){
					roomName = capitalisedSentence.substring(capitalisedSentence.indexOf("JOIN_CHATROOM: ")+15,capitalisedSentence.length());
					//read next
					clientSentence = inFromClient.readLine();
					capitalisedSentence = clientSentence.toUpperCase();
					client_ip = Integer.parseInt(capitalisedSentence.substring(capitalisedSentence.indexOf("CLIENT_IP: ")+11,capitalisedSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					capitalisedSentence = clientSentence.toUpperCase();
					tcpPort = Integer.parseInt(capitalisedSentence.substring(capitalisedSentence.indexOf("PORT: ")+6,capitalisedSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					capitalisedSentence = clientSentence.toUpperCase();
					clientName =capitalisedSentence.substring(capitalisedSentence.indexOf("CLIENT_NAME: ")+13,capitalisedSentence.length());
					temp = "JOINED_CHATROOM: "+roomName+"\nSERVER_IP: "+ip+"\nPORT: "+port+"\nROOM_REF: 123\nJOIN_ID: 0\n\n";
					outToClient.write(temp.getBytes());
				}
				
				//while(clientSentence!=null){
					//System.out.println(capitalisedSentence);
					
				//	packet = initialCheck(capitalisedSentence);
				//	if(packet == null){
				//		conSocket.close();
				//		System.exit(0);
				//	}
				//	outToClient.write(packet.getBytes());
				//	System.out.println("Got");
				//}
		}		
	}	
	
	public static int typeCheck(String input){
		
		if(input.contains("HELO")){
			return 0;
		}
		else if(input.contains("JOIN CHATROOM:")){
			return 1;
		}
		else if(input.contains("KILL_SERVICE:")){
			return 2;
		}
		else if(input.contains("LEAVE_SERVICE:")){
			return 3;
		}
		else if(input.contains("DISCONNECT:")){
			return 4;
		}
		else if(input.contains("CHAT:")){
			return 5;
		}
		else{
			return -1;
		}
	}
	
	public static String makeHELO(String ip, int port, int id){
		return "HELO BASE_TEST" + "\nIP:"+ip+"\nPort:"+port+"\nStudentID:"+id; 
	}
	
	public static String initialCheck(String input){
		String output = null;
		String roomName = null ,clientName = null;
		//for now type in IP, in future have it set as argument to run code
		if(input.contains("HELO")){
			output = input + "\nIP:134.226.50.35\nPort:6789\nStudentID:14315135";
		} //need to perform string manipulation on the input stream
		else if(input.contains("JOIN_CHATROOM:")){
			roomName = input.substring(input.indexOf("JOIN_CHATROOM: ")+15,input.length());
			System.out.println(roomName);
			clientName = input.substring(input.indexOf("CLIENT_NAME: "),input.length());
			output = "JOINED_CHATROOM: "+roomName+"\nSERVER_IP: 134.226.50.35\nPORT: 6789\nROOM_REF: 123\nJOIN_ID: 0\n\n";
		}
		else if(input.contains("CHAT:")){
			
			output = "CHAT: " + roomName +"\nCLIENT_NAME: " + clientName+ "";
		}
		return output;
	}
}