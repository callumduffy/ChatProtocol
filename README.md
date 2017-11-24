# Java Chat Server
Server made for Internet Applications module in 4th Year of Trinity College Dublin Computer Science  

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

Server also then sends a chat message to everyone in the chatroom to indicate that the client has joined the room.  

