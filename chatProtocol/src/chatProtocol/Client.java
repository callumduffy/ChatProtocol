package chatProtocol;

public class Client {
	private int client_ip;
	private int client_port;
	private String handle;
	
	Client(int ip, int port, String name){
		this.client_ip = ip;
		this.client_port = port;
		this.handle = name;
	}
	
	private int getIP(){
		return this.client_ip;
	}
	
	private int getPort(){
		return this.client_port;
	}
	
	private String getHandle(){
		return this.handle;
	}
}
