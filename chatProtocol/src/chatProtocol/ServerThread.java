package chatProtocol;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread {
    private Socket socket = null;
	private boolean alive;
	private ServerObj s;
	private static int runs=0, roomRef=0, join_id=0;
	private static ArrayList<Client> clientList = new ArrayList<Client>();

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        this.alive=true;
        s = new ServerObj();
    }
    
    public void run() {
    	runs++;
    	System.out.println("threads =" +runs);
        try (	
        		
        		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				
				DataOutputStream outToClient = new
						DataOutputStream(socket.getOutputStream());
        ) {
    		Client c1 = new Client("134.226.50.35",6789,this.socket);
    		clientList.add(c1);
    		
        	String clientSentence, capitalisedSentence;
        	String ip = "134.226.50.35";
    		int port =6789;
    		int id = 14315135, temp_ref=0, temp_id=0;
    		String roomName = null;
    		int client_ip;
    		int tcpPort;
    		String clientName= null;
    		String temp = null, msg = null;
    		boolean exists = false;
    		
    		while(alive){
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
				//temp chatroom variable
				Chatroom c = null;
				roomName = clientSentence.substring(clientSentence.indexOf("JOIN_CHATROOM: ")+15,clientSentence.length());
				//read next
				
				for(Map.Entry<Integer,Chatroom> e:  s.getMapEntries()){
					if(e.getValue().getName().equals(roomName)){
						exists=true;
						c=e.getValue();
						roomRef = e.getKey();
						break;
					}
				}
				
				clientSentence = inFromClient.readLine();
				client_ip = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("CLIENT_IP: ")+11,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				tcpPort = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("PORT: ")+6,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				clientName =clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
				c1.addName(clientName);
				
				//create chatroom or add to existing one
				if(exists){
					c.addClient(join_id,c1);
					join_id++;
				}
				else{
					c = new Chatroom(ip, port, roomName);
					ServerObj.addChatroom(roomRef,c);
					roomRef++;
					c.addClient(join_id,c1);
					join_id++;
				}
				exists=false;
				
				temp = "JOINED_CHATROOM: "+roomName+"\nSERVER_IP: "+c1.getIP()+"\nPORT: "+c1.getPort()+"\nROOM_REF: "+roomRef+"\nJOIN_ID: "+join_id+"\n";
				System.out.println("temp=" + temp);
				outToClient.write(temp.getBytes());
				//test server msg
				temp = "CHAT:"+roomRef+"\nCLIENT_NAME: "+c1.getHandle()+"\nMESSAGE: "+c1.getHandle()+" JOINED\n\n";
				System.out.println("temp=" + temp);
				outToClient.write(temp.getBytes());
				
				//needs to be fixed big time TODO
				
//				for(Client client:clientList){
//					if(ServerObj.getRoom(join_id-1).contains(client)){
//						temp = "CHAT:"+roomRef+"\nCLIENT_NAME: "+client.getHandle()+"\nMESSAGE: "+c1.getHandle()+" JOINED\n\n";
//						System.out.println("temp=" + temp);
//						outToClient.write(temp.getBytes());
//						
//						DataOutputStream out = new
//								DataOutputStream((client.getSocket()).getOutputStream());
//						out.write(temp.getBytes());
//					}
//				}
			}
			else if (type ==2){
				alive=false;
				socket.close();
				System.exit(0);
			}
			else if(type==3){
				Chatroom c = null;
				
				roomRef = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("LEAVE_CHATROOM: ")+16,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				join_id = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("JOIN_ID: ")+9,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				clientName = clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
				
				////leave chatroom or not
				//if(ServerObj.getRoom(temp_ref).getClientName(temp_id)==clientName){
				//	ServerObj.getRoom(temp_ref).removeClient(temp_id);
				//}
				
				String t = "LEFT_CHATROOM: "+roomRef+"\nJOIN_ID: "+join_id+"\n";
				System.out.println("temp=" + t);
				outToClient.write(t.getBytes());
				//test server msg
				temp = "CHAT:"+roomRef+"\nCLIENT_NAME: "+clientName+"\nMESSAGE: "+clientName+" LEFT\n\n";
				System.out.println("temp=" + t);
				outToClient.write(temp.getBytes());
//				
//				if(ServerObj.getRoom(join_id-1).getSize()>1){
//					for(Client client:clientList){
//						if(ServerObj.getRoom(join_id-1).contains(client)){
//							DataOutputStream out = new
//									DataOutputStream((client.getSocket()).getOutputStream());
//							out.write(temp.getBytes());
//						}
//					}
//				}
				
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
    		socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    public static int typeCheck(String input){
    	
    	if(input.contains("HELO")){
    		return 0;
    	}
    	else if(input.contains("JOIN_CHATROOM:")){
    		return 1;
    	}
    	else if(input.contains("KILL_SERVICE")){
    		return 2;
    	}
    	else if(input.contains("LEAVE_CHATROOM:")){
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

    