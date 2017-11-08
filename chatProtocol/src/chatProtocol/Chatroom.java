package chatProtocol;

public class Chatroom {
	
	private String name;
	private int ip;
	private int port;
	
	Chatroom(int ip_num, int port_num, String room_name){
		this.ip = ip_num;
		this.port = port_num;
		this.name = room_name;
	}
	
	private int getIP(){
		return this.ip;
	}
	
	private int getPort(){
		return this.port;
	}
	
	private String getName(){
		return this.name;
	}
}
