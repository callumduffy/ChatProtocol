# Java Chat Server
Server made for Internet Applications module in 4th Year of Trinity College Dublin Computer Science  
   
Current score on the test server is 102% somehow.  

## Compilation and Running  
>I have added a compile.sh file, just as it is in the spec, it does nothing but it will do you no harm to run...I promise.  
>Then run the run.sh, in the format "bash run.sh <port number>"  
>This executes the runnable .jar file, as I was having a strange error with the jre not recognising my main using regular javac and java commands.  

## Specifications
Written in java and made to handle multiple clients concurrently, handling each request and sending the appropriate responses  
Clients should be able to :  
- Join a chatroom  
- Leave a chatroom  
- Send chat messages to all members of each chatroom  
- Disconnect from the chatroom  
- Kill the service  

### Joining a chatroom  
Client sends a join request for a chatroom  
>JOIN_CHATROOM: [chatroom name]  
>CLIENT_IP: [IP Address of client if UDP | 0 if TCP]  
>PORT: [port number of client if UDP | 0 if TCP]  
>CLIENT_NAME: [string Handle to identifier client user]  

The server handles this and adds the client to the chatroom if it exists, or creates the room.  
Then sends back a message to indicate that the join has occured.  

>JOINED_CHATROOM: [chatroom name]  
>SERVER_IP: [IP address of chat room]  
>PORT: [port number of chat room]  
>ROOM_REF: [integer that uniquely identifies chat room on server]  
>JOIN_ID: [integer that uniquely identifies client joining]  

Server also then sends a chat message to everyone in the chatroom to indicate that the client has joined the room. This is of the form of the message sent to the chat room when a CHAT message is sent by a client.  
  
### Leaving a chatroom  
Client sends a leave request for a chatroom  
>LEAVE_CHATROOM: [ROOM_REF]  
>JOIN_ID: [integer previously provided by server on join]  
>CLIENT_NAME: [string Handle to identifier client user]  

The server handles this by removing the client from the chat room, and sends the client the following message as an acknowledgement  
>LEFT_CHATROOM: [ROOM_REF]  
>JOIN_ID: [integer previously provided by server on join]  

The server also sends a chat message to all clients in the chat room to update them that the client has left.  

### Chat Messages
Clients can send messages to all clients in any given chat room that they are in, they can also send messages to multiple chatrooms concurrently  
>CHAT: [ROOM_REF]  
>JOIN_ID: [integer identifying client to server]  
>CLIENT_NAME: [string identifying client user]  
>MESSAGE: [string terminated with '\n\n']  

The server then sends the following chat message to all members of the chat room. (Same format as join/leave messages)  
>CHAT: [ROOM_REF]  
>CLIENT_NAME: [string identifying client user]  
>MESSAGE: [string terminated with '\n\n']  

### Disconnecting  
Clients can disconnext from the chat server by sending the following message:  
>DISCONNECT: [IP address of client if UDP | 0 if TCP]  
>PORT: [port number of client it UDP | 0 id TCP]  
>CLIENT_NAME: [string handle to identify client user]  

The server then treats this as a LEAVE message for all of the chatroooms that the client is currently in, and carries out the neccessary actions from there. The server then closes the socket for this client to disable further communication.  

### Kill Service
The server as a whole can be killed at any given time by the clients by sending the kill message:  
>KILL_SERVICE  

This closes all sockets and kills the program execution in it's tracks.  


