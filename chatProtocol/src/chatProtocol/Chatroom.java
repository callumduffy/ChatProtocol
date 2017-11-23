package chatProtocol;

import java.util.HashMap;

public class Chatroom {
	
	private String name;
	private String ip;
	private int port;
	private HashMap<Integer,Client> room;
	private int ref;
	
	Chatroom(String ip_num, int port_num, String room_name, int room_ref){
		this.ip = ip_num;
		this.port = port_num;
		this.name = room_name;
		this.room = new HashMap<Integer,Client>();
		this.ref=room_ref;
	}
	
	public String getIP(){
		return this.ip;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getRef(){
		return this.ref;
	}
	
	public String getClientName(int id){
		if(this.room.get(id)!=null){
			return this.room.get(id).getHandle();
		}
		else return null;
	}
	
	public void addClient(int join_id,Client c){
		this.room.put(join_id,c);
	}
	
	public void removeClient(int id){
		if(this.room.get(id)!=null){
			this.room.remove(id);
		}
	}
	
	public boolean contains(Client c){
		if (this.room.containsValue(c)){
			return true;
		}
		else return false;
	}
	
	public boolean has(int id){
		if(this.room.get(id)!=null){
			return true;
		}
		return false;
	}
	
	public int getSize(){
		return this.room.size();
	}
}
