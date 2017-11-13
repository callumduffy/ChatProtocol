package chatProtocol;

import java.util.*;

public class ServerObj {
	private static HashMap<Integer,Chatroom> server;
	private int size;
	
	ServerObj(){
		server = new HashMap<Integer,Chatroom>();
	}
	
	public static void addChatroom(int room_ref, Chatroom chat){
		server.put(room_ref,chat);
	}
	
	public Set<Map.Entry<Integer,Chatroom>> getMapEntries(){
		return server.entrySet();
	}
	
	public static void removeChatroom(int room_ref){
		server.remove(room_ref);
	}
	
	public int getSize(){
		return server.size();
	}
	
	public static Chatroom getRoom(int join_id){
		if(server.get(join_id)!=null){
			return server.get(join_id);
		}
		else return null;
	}
}