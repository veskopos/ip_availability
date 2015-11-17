package org.elsysbg.ip.availability;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	private static int port;
	private static ServerSocket serverSocket;
	private static boolean shutdown = false;
	
	public static ArrayList <Client> clients = new ArrayList<Client>();
	private static ArrayList <Integer> newClients = new ArrayList<Integer>();
	
	public static class Client{
		private Socket sock;
		private PrintStream out;
		private Scanner in;
		public Socket getSock(){
			return sock;
		}
		public PrintStream getOut(){
			return out;
		}
		public Scanner getIn(){
			return in;
		}
		Client(Socket s) throws IOException{
			sock = s;
			out = new PrintStream(sock.getOutputStream());
			in =  new Scanner (sock.getInputStream());
		}
		public void closeClient() throws IOException{
			out.close();
			in.close();
			sock.close();
		}
	}

	public static class ClientListener extends Thread{
		public  void run(){
			while(shutdown==false){
				Socket s;
				try {
					s = serverSocket.accept();
					int i = clients.size();
					clients.add(new Client(s));
					newClients.add(i);
				}catch(IOException e){
					System.out.println("Server halted!");
				}
			}
		}
	}

	public static void startServer(int serverPort) throws IOException {
		port = serverPort;
		serverSocket = new ServerSocket(port);
	}

	public static  String getMessage(int clientID) throws IOException{
			Scanner scanner = clients.get(clientID).getIn();
		if(scanner.hasNextLine()){
			String line = scanner.nextLine();
			return line;
		}else{
			return null;
		}
	}

	public static void shutdownServer() throws IOException{
		for(Client c: clients){
			c.getSock().close();
		}
		shutdown = true;
		serverSocket.close();
	}

	public static  PrintStream sendMessage(int clientID) throws IOException{
		if(!clients.get(clientID).getSock().isClosed()){
			return new PrintStream(clients.get(clientID).getOut());
		}else{
			return null;
		}
	}

	public static  int nextNewClient(){
		int i;
		if (newClients.size() > 0){
			i = newClients.get(0);
			newClients.remove(0);
			return i;
		}
		return -1;
	}

	public static  ArrayList <Integer> allClients(){
		ArrayList <Integer> clientIDs = new ArrayList<Integer>();
		for(int i=0; i< clients.size(); ++i){
			if(!clients.get(i).getSock().isClosed()){
				clientIDs.add(i);
			}
		}
		return clientIDs;
	}

	public static ArrayList <Integer> disconnectedClients(){
		ArrayList <Integer> clientIDs = new ArrayList<Integer>();
		for(int i=0; i< clients.size(); ++i){
			if(clients.get(i).getSock().isClosed()){
				clientIDs.add(i);
			}
		}
		return clientIDs;
	}
}