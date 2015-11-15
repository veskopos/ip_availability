package org.logger;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

public class CommandHandler {
	public final static Map<String, Integer> NameToCount = new HashMap<String, Integer>();
	public final static LinkedList<String> invited = new LinkedList<String>();
	public static void execute(String command){
		final String[] split = command.split(":");
		if (split.length > 1){
			switch (split[1]){
				case "login":
					login(split);
			break;
				case "logout":
					logout(split);
			break;
				case "info" :
					info(split);
			break;
				case "listavailable" :
					listavailable(split);
			break;
				case "shutdown" :
					shutdown(split);
			break;
				default:
					System.out.println("error:unknown command");
			}
		}else{
			System.out.println("error:not enough arguments");
		}
		
	}

	private static void login(String[] split){
		System.out.println("ok");
		if (!invited.contains(split[0])){
			int logCounter;
			if(NameToCount.containsKey(split[0])){
				logCounter = NameToCount.get(split[0]) + 1;
			}else{
				logCounter = 1;
			}
			invited.add(split[0]);
			NameToCount.put(split[0], logCounter);
		}
	}
		
	private static void logout(String[] split){
		if (invited.contains(split[0])){
			invited.remove(split[0]);
			System.out.println("ok");
		}else{
			System.out.println("error: not loggedin");
		}
	}
	
	private static void info(String[] split){
		if(!(split.length == 3)){
			System.out.println("error:notEnoughParameters");
		}
		if (invited.contains(split[0])){
			int logCount = 0;
			if(NameToCount.get(split[2])!=null){
				logCount = NameToCount.get(split[2]);
			}
			System.out.println("ok: " + split[2] + ":" + invited.contains(split[2]) + ":" + logCount);
			}else{
				System.out.println("error:notlogged");
			}
		}
		
	private static void listavailable(String[] split){
		if (invited.contains(split[0])){
			ListIterator <String> it = invited.listIterator();
			String available = "ok";
			while(it.hasNext()){
				available += ":" + it.next(); 
			}
			System.out.println(available);
		}else{
			System.out.println("error:notlogged");
		}
		
	}
	
	private static void shutdown(String[] split){
		if (invited.contains(split[0])){
			AppMain.shutdown = true;
			System.out.println("ok");
		}else{
			System.out.println("error:notlogged");
		}
	}
}