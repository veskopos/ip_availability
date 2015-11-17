package org.elsysbg.ip.availability;

import java.io.IOException;
import java.util.ArrayList;

public class User extends Thread {
	private ArrayList <String> receivedMessages = new ArrayList <String> ();
	private int id;
	private boolean shutdown = false;
	public void shutdown(){
		shutdown = true;
	}

	User(int id){
		this.id = id;
	}

	public void run(){
		while(shutdown == false){
			try{
				receivedMessages.add(Server.getMessage(id));
				//System.out.println(receivedMessages.size());
				}catch(IOException e){
				}catch(NullPointerException e){
				}
		}
	}

	String getNextMessage(){
		if(receivedMessages.size() > 0){
			String msg = receivedMessages.get(0);
			receivedMessages.remove(0);
			return msg;
		}
		return null;
	}

	void sendMessage(String message) throws IOException{
		Server.sendMessage(id).println(message);
	}

	public int getID(){
		return id;
	}
}