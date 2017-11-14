package chatProtocol;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread {
    private Socket socket = null;
	private boolean alive;
	private static ServerObj s= new ServerObj();;
	private static int runs=0, roomRef=0, join_id=0;
	private static ArrayList<Client> clientList = new ArrayList<Client>();

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        this.alive=true;
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
    		Client c1 = new Client("134.226.50.35",6789,this.socket,join_id);
    		clientList.add(c1);
    		join_id++;
    		
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
						temp_ref = e.getKey();
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
					c.addClient(c1.getID(),c1);
					clientList.set(c1.getID(), c1);
				}
				else{
					c = new Chatroom(ip, port, roomName,roomRef);
					ServerObj.addChatroom(roomRef,c);
					roomRef++;
					clientList.set(c1.getID(), c1);
					c.addClient(c1.getID(),c1);
				}
				exists=false;
				System.out.println(roomRef);
				temp = "JOINED_CHATROOM: "+roomName+"\nSERVER_IP: "+c1.getIP()+"\nPORT: "+c1.getPort()+"\nROOM_REF: "+c.getRef()+"\nJOIN_ID: "+c1.getID()+"\n";
				System.out.println("temp=" + temp);
				outToClient.write(temp.getBytes());
				//test server msg
				
				for(Client client:clientList){
					if(c.contains(client)){
						temp = "CHAT:"+c.getRef()+"\nCLIENT_NAME: "+c1.getHandle()+"\nMESSAGE: "+c1.getHandle()+" JOINED\n\n";
						System.out.println(client.getHandle() + "=" + temp);
						//outToClient.write(temp.getBytes());
						
						DataOutputStream out = new
								DataOutputStream((client.getSocket()).getOutputStream());
						out.write(temp.getBytes());
					}
				}
			}
			else if (type ==2){
				alive=false;
				socket.close();
				System.exit(0);
			}
			else if(type==3){
				Chatroom c = null;
				
				temp_ref = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("LEAVE_CHATROOM: ")+16,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				temp_id = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("JOIN_ID: ")+9,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				clientName = clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
				
				////leave chatroom or not
				//if(ServerObj.getRoom(temp_ref).getClientName(temp_id)==clientName){
				//	ServerObj.getRoom(temp_ref).removeClient(temp_id);
				//}
				
				String t = "LEFT_CHATROOM: "+temp_ref+"\nJOIN_ID: "+c1.getID()+"\n";
				System.out.println("temp=" + t);
				outToClient.write(t.getBytes());
				//test server msg
				
				c= ServerObj.getRoom(temp_ref);
				for(Client client:clientList){
					if(c!=null && c.contains(client)){
						temp = "CHAT:"+temp_ref+"\nCLIENT_NAME: "+c1.getHandle()+"\nMESSAGE: "+c1.getHandle()+" LEFT\n\n";
						System.out.println(client.getHandle() + "=" + temp);
						//outToClient.write(temp.getBytes());
						
						DataOutputStream out = new
								DataOutputStream((client.getSocket()).getOutputStream());
						out.write(temp.getBytes());
					}
				}
				c.removeClient(c1.getID());
			}
			else if(type==4){
				Client temp_cli = null;
				clientSentence = inFromClient.readLine();
				client_ip = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("DISCONNECT: ")+12,clientSentence.length()));
				clientSentence = inFromClient.readLine();
				tcpPort = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("PORT: ")+6,clientSentence.length()));
				clientSentence = inFromClient.readLine();
				clientName =clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
				
				for(Client client:clientList){
					if(client.getHandle().equals(clientName)){
						temp_cli=client;
						//leave chats etc
					}
				}
				temp_cli.getSocket().close();
			}
			else if(type==5){
				Chatroom c=null;
				temp_ref = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("CHAT: ")+6,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				temp_id = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("JOIN_ID: ")+9,clientSentence.length()));
				//read next
				clientSentence = inFromClient.readLine();
				clientName =clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
				//read next
				clientSentence = inFromClient.readLine();
				msg =clientSentence.substring(clientSentence.indexOf("MESSAGE: ")+9,clientSentence.length());
				
				c= ServerObj.getRoom(temp_ref);
				if(c!=null){
					for(Client client:clientList){
						if(c.contains(client)){
							temp = "CHAT:"+c.getRef()+"\nCLIENT_NAME: "+c1.getHandle()+"\nMESSAGE: "+msg+"\n\n";
							System.out.println(client.getHandle() + " CHAT =" + temp);
							//outToClient.write(temp.getBytes());
							
							DataOutputStream out = new
									DataOutputStream((client.getSocket()).getOutputStream());
							out.write(temp.getBytes());
						}
					}
				}
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

    