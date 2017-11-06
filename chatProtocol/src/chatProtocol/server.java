package chatProtocol;

import java.io.*;
import java.net.*;

public class server{
	public static void main(String argv[]) throws Exception {
		
		String clientSentence, capitalisedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);
		
		String ip = "134.226.50.41";
		int port =6789;
		int id = 14315135;
		String roomName = null;
		int client_ip;
		int tcpPort;
		String clientName= null;
		String temp = null, msg = null;
		int roomRef=0;
		int join_id =0;

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
				//msg received test
				System.out.println(capitalisedSentence);
				int type = typeCheck(capitalisedSentence);
				System.out.println(type);
				if(type ==0 ){
					outToClient.write(makeHELO(ip,port,id).getBytes());
				}
				else if(type==1){
					roomName = clientSentence.substring(clientSentence.indexOf("JOIN_CHATROOM: ")+15,clientSentence.length());
					//read next
					clientSentence = inFromClient.readLine();
					client_ip = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("CLIENT_IP: ")+11,clientSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					tcpPort = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("PORT: ")+6,clientSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					clientName =clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
					temp = "JOINED_CHATROOM: "+roomName+"\nSERVER_IP: "+ip+"\nPORT: "+port+"\nROOM_REF: "+roomRef+"\nJOIN_ID: "+join_id+"\n";
					System.out.println("temp=" + temp);
					outToClient.write(temp.getBytes());
					//test server msg
					temp = "CHAT:"+roomRef+"\nCLIENT_NAME: "+clientName+"\nMESSAGE: "+clientName+" JOINED\n\n";
					System.out.println("temp=" + temp);
					outToClient.write(temp.getBytes());
				}
				else if (type ==2){
					welcomeSocket.close();
					System.exit(0);
				}
				else if(type==3){
					roomRef = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("LEAVE_CHATROOM: ")+17,clientSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					join_id = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("JOIN_ID: ")+9,clientSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					clientName = clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
					temp = "LEFT_CHATROOM: "+roomRef+"\nJOIN_ID: "+join_id+"\n";
					System.out.println("temp=" + temp);
					outToClient.write(temp.getBytes());
					//test server msg
					String t = "CHAT:"+roomRef+"\nCLIENT_NAME: "+clientName+"\nMESSAGE: "+clientName+" LEFT\n\n";
					System.out.println("temp=" + t);
					outToClient.write(t.getBytes());
				}
				else if(type==5){
					roomRef = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("CHAT: ")+6,clientSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					join_id = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("JOIN_ID: ")+9,clientSentence.length()));
					//read next
					clientSentence = inFromClient.readLine();
					clientName =clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
					//read next
					clientSentence = inFromClient.readLine();
					msg =clientSentence.substring(clientSentence.indexOf("MESSAGE: ")+9,clientSentence.length());
					temp = "CHAT: "+roomRef+"\nCLIENT_NAME: "+clientName+"\nMESSAGE: "+msg+"\n\n";
					System.out.println("temp=" + temp);
					outToClient.write(temp.getBytes());
				}
		}		
	}	
	
	public static int typeCheck(String input){
		
		if(input.contains("HELO")){
			return 0;
		}
		else if(input.contains("JOIN_CHATROOM:")){
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