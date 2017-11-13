package chatProtocol;

import java.net.Socket;

public class Client implements AutoCloseable {
	private String client_ip;
	private int client_port;
	private String handle;
	private Socket sock;
	
	Client(String ip, int port, String name, Socket socket){
		this.client_ip = ip;
		this.client_port = port;
		this.handle = name;
		this.sock=socket;
	}
	//for initial opening of socket
	Client(String ip, int port, Socket socket){
		this.client_ip = ip;
		this.client_port = port;
		this.handle = null;
		this.sock=socket;
	}
	
	public String getIP(){
		return this.client_ip;
	}
	
	public int getPort(){
		return this.client_port;
	}
	
	public String getHandle(){
		return this.handle;
	}
	
	public void addName(String name){
		this.handle=name;
	}
	
	public Socket getSocket(){
		return this.sock;
	}
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
