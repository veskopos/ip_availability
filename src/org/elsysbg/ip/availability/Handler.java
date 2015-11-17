package org.elsysbg.ip.availability;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.ListIterator;

	public class Handler {
		public final static Map<String, Integer> NameToCount = new HashMap<String, Integer>();
		public final static LinkedList<String> invited = new LinkedList<String>();
		public final static Map<String, Integer> NameToID = new HashMap <String, Integer>();
		public final static ArrayList <String> logouted = new ArrayList <String> ();
		public final static Map <String, UserHandler> Dates = new HashMap <String, UserHandler>();
		private static PrintStream stream;

		public static void execute(String command, PrintStream new_stream, int ID){
			stream = new_stream;
			final String[] split = command.split(":");
			if (split.length > 1){
				switch (split[1]){
					case "login":
						login(split, ID);
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
					case "listabsent" :
						listabsent(split);
				break;
					case "shutdown" :
						shutdown(split);
				break;
					default:
						stream.println("error:unknown command");
				}
			}else{
				stream.println("error:not enough arguments");
			}
		}

		private static void login(String[] split, int ID){
			stream.println("ok");
			if(!invited.contains(split[0])){
				int logCounter;
				if(NameToCount.containsKey(split[0])){
					logCounter = NameToCount.get(split[0]) + 1;
				}else{
					logCounter = 1;
				}
				invited.add(split[0]);
				NameToCount.put(split[0], logCounter);
				Interval interval = new Interval();
				interval.from = new Date();
				if(Dates.containsKey(split[0])){
					UserHandler UH = Dates.get(split[0]);
					UH.intervals.add(interval);
					Dates.put(split[0], UH);
				}else{
					UserHandler UH = new UserHandler();
					UH.intervals.add(interval);
					Dates.put(split[0], UH);
				}
			}
			NameToID.put(split[0], ID);
			if(logouted.contains(split[0])){
				logouted.remove(split[0]);
			}
		}

		private static void logout(String[] split){
			if(invited.contains(split[0])){
				invited.remove(split[0]);
				stream.println("ok");
				NameToID.remove(split[0]);
				logouted.add(split[0]);
				Dates.get(split[0]).intervals.get(Dates.get(split[0]).intervals.size()-1).to = new Date();
			}else{
				stream.println("error: not loggedin");
			}
		}

		public static void logoutOnDisconnect(ArrayList<Integer> disconnected){
			for(int i: disconnected){
				if(NameToID.containsValue(i)){
					String s = null;
					for (Map.Entry<String, Integer> entry : NameToID.entrySet()){
						if(entry.getValue()==i){
							s = entry.getKey();
						}
					}
					if(s!=null){
						invited.remove(s);
						NameToID.remove(s);
						logouted.add(s);
						Dates.get(s).intervals.get(Dates.get(s).intervals.size()-1).to = new Date();
						break;
					}
				}
			}
		}

		private static void info(String[] split){
			if(!(split.length == 3)){
				stream.println("error:notEnoughParameters");
				return;
			}
			if(invited.contains(split[0])){
				int logCount = 0;
				if(NameToCount.get(split[2])!=null){
					logCount = NameToCount.get(split[2]);
				}
				stream.print("ok:" + split[2] + ":" + invited.contains(split[2]) + ":" + logCount);
				UserHandler UH = Dates.get(split[2]);
				for(Interval i: UH.intervals){
					
					SimpleDateFormat loginData = new SimpleDateFormat("yyyy-MM-dd'T'HH'_'mm'_'ss.SSSZ");
					String loginFormat = loginData.format(i.from);
					
					SimpleDateFormat logoutData = new SimpleDateFormat("yyyy-MM-dd'T'HH'_'mm'_'ss.SSSZ");
					String logoutFormat = null; 
					if(i.to!=null){
						logoutFormat = logoutData.format(i.to);
					}
					
					stream.print(":" + loginFormat);
					stream.println(i.to == null ? "" : (":" + logoutFormat));
				}
			}else{
				stream.println("error:notlogged");
			}
		}

		private static void listavailable(String[] split){
			if(invited.contains(split[0])){
				ListIterator <String> it = invited.listIterator();
				String available = "ok";
				while(it.hasNext()){
					available += ":" + it.next();
				}
				stream.println(available);
			}else{
				stream.println("error:notlogged");
			}
		}

		private static void listabsent (String[] split){
			if (invited.contains(split[0])){
				ListIterator <String> it = logouted.listIterator();
				String available = "ok";
				while(it.hasNext()){
					available += ":" + it.next();
				}
				stream.println(available);
			}else{
				stream.println("error:notlogged");
			}
		}

		private static void shutdown(String[] split){
			if(invited.contains(split[0])){
				stream.println("ok");
				throw new NullPointerException();
			}else{
				stream.println("error:notlogged");
			}
		}
	}