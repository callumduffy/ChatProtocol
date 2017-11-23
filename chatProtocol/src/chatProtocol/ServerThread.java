package chatProtocol;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServerThread extends Thread {
	
	//initiate neccessary values for the server
	//ServerObj manages a hashmap of the chatrooms
	//static to allow all threads to access it
    private Socket socket = null;
	private boolean alive;
	private static ServerObj s= new ServerObj();;
	private static int roomRef=0, join_id=0;
	//tracking a list of all clients
	//just to ensure i have their socket data
	private static ArrayList<Client> clientList = new ArrayList<Client>();

    public ServerThread(Socket socket) {
        super("ServerThread");
        this.socket = socket;
        this.alive=true;
    }
    
    public void run() {
        try (	
        		
        		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				
				DataOutputStream outToClient = new
						DataOutputStream(socket.getOutputStream());
        ) {
        	//construct a new client for every thread
    		
    		String ip_full = InetAddress.getLocalHost().toString();
    		String ip = ip_full.substring(ip_full.indexOf("/")+1,ip_full.length());
        	String clientSentence, capitalisedSentence;
    		int port =6789;
    		int id = 14315135, temp_ref=0, temp_id=0;
    		String roomName = null;
    		int client_ip;
    		int tcpPort;
    		String clientName= null;
    		String temp = null, msg = null;
    		boolean exists = false;
    		Client c1 = new Client(ip,6789,this.socket,join_id);
    		clientList.add(c1);
    		join_id++;
    		
    		//begins the constant loop of waiting for messages
    		while(alive){
    			clientSentence = inFromClient.readLine();
				capitalisedSentence = clientSentence.toUpperCase();
				//msg received test
				System.out.println(capitalisedSentence);
				//initial check to see what type of message we are receiving
				//output 0-5 = correct message
				// output of -1 indicates an error
				int type = typeCheck(capitalisedSentence);
				System.out.println(type);
				
			
    		//HELO message
			//Sends back wanted helo message in function
    		if(type ==0 ){
				outToClient.write(makeHELO(ip,port,id).getBytes());
			}
    		
    		//Section for joining a chatroom
    		//creates a chatroom if it does not already exist and adds the client to it, if not there already
    		//then sends a message to the whole chatroom, indicating the join has happened
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
				
				//read the input from the client
				clientSentence = inFromClient.readLine();
				client_ip = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("CLIENT_IP: ")+11,clientSentence.length()));
				clientSentence = inFromClient.readLine();
				tcpPort = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("PORT: ")+6,clientSentence.length()));
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
				
				//construct the packet to send back to the client
				temp = "JOINED_CHATROOM: "+roomName+"\nSERVER_IP: "+c1.getIP()+"\nPORT: "+c1.getPort()+"\nROOM_REF: "+c.getRef()+"\nJOIN_ID: "+c1.getID()+"\n";
				System.out.println("temp=" + temp);
				outToClient.write(temp.getBytes());
				
				//sending message to the clients in the chatroom
				//TODO add to function, with chatroom, client and msg as param
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
    		
    		//If the message is a disconnect
    		//send leave message to all chatrooms
    		//then close the socket
			else if (type ==2){
				alive=false;
				socket.close();
				System.exit(0);
			}
    		
    		//section for leaving a chatroom
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
						temp = "CHAT:"+temp_ref+"\nCLIENT_NAME: "+c1.getHandle()+"\nMESSAGE: "+c1.getHandle()+" DISCONNECTED\n\n";
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
				Chatroom c =null;
				client_ip = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("DISCONNECT: ")+12,clientSentence.length()));
				clientSentence = inFromClient.readLine();
				tcpPort = Integer.parseInt(clientSentence.substring(clientSentence.indexOf("PORT: ")+6,clientSentence.length()));
				clientSentence = inFromClient.readLine();
				clientName = clientSentence.substring(clientSentence.indexOf("CLIENT_NAME: ")+13,clientSentence.length());
				
				for(Map.Entry<Integer,Chatroom> e:  s.getMapEntries()){
					c = e.getValue();
					if(clientName.equals("client2")&&(c.getName().equals("room2"))){
						System.out.println("Hello");
					}
					else{
						System.out.println("CR" + c.getName() + ": contains "+c.getSize() +"  -- START --");
						for(Client client:clientList){
							if(c!=null && c.contains(client)&&c.has(client.getID())){
								temp = "CHAT:"+c.getRef()+"\nCLIENT_NAME: "+c1.getHandle()+"\nMESSAGE: "+c1.getHandle()+" LEFT\n\n";
								System.out.println(client.getHandle() + "=" + temp);;
								
								DataOutputStream out = new
										DataOutputStream((client.getSocket()).getOutputStream());
								out.write(temp.getBytes());
							}
						}
						c.removeClient(c1.getID());
						System.out.println("CR" + c.getName() + ": contains "+c.getSize() + "  -- END --");
					}
				}
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
            //e.printStackTrace();
        }
        catch(NullPointerException e){
        	
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
    		System.out.println("Error: message not in correct format");
    		return -1;
    	}
    }

        public static String makeHELO(String ip, int port, int id){
    	return "HELO BASE_TEST" + "\nIP:"+ip+"\nPort:"+port+"\nStudentID:"+id; 
    }
                
}

    