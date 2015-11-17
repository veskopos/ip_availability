package org.elsysbg.ip.availability;

import java.io.IOException;
import java.util.ArrayList;

public class ServerAppMain {
public static ArrayList <User> users = new ArrayList <User>();

	public static void main(String[] args) throws IOException {
		Server.startServer(1337);
		Server.ClientListener listener = new Server.ClientListener();
		listener.start();
		boolean shutdown = false;
		//Server.sendMessage().println("Enter command: ");
		while(shutdown == false){
			int client;
			do{
				client = Server.nextNewClient();
				if(client > -1){
					Server.sendMessage(client).println("Enter command: ");
					users.add(new User(client));
					users.get(users.size()-1).start();
				}
			}while(client!= -1);
			for(int i: Server.allClients()){
				int userIndex = -1;
				System.out.println(users.size());
				for(int a = 0; a < users.size(); ++a){
					//System.out.println("---------------------------------" + users.size() + " " + users.get(a).getID());
					if(users.get(a).getID()== i){
						userIndex = a;
					}
				}
				if(userIndex==-1){
					continue;
				}
				String message = users.get(userIndex).getNextMessage();
				//System.out.println(message);
				if(message != null){
					try{
						//TODO: FIX user to send the messege
						Handler.execute(message, Server.sendMessage(i), i);
					}catch(NullPointerException NPE){
						shutdown = true;
						//Server.shutdownServer();
					}
					
					if (shutdown == false)users.get(userIndex).sendMessage("Enter command: ");
				}
			}
			Handler.logoutOnDisconnect(Server.disconnectedClients());
			//logoutOnDisconnect
		}
		//listener.destroy();
		for(User u: users){
			u.shutdown();
		}
		Server.shutdownServer();
	}
}