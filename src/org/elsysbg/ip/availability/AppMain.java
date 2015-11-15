package org.logger;

import java.util.Scanner;

public class AppMain {
	public static boolean shutdown = false;
	public static void main(String[] args) {
		final Scanner in = new Scanner(System.in);
		while (!shutdown) {
			System.out.println("Enter command: ");
			final String command = in.next();
			CommandHandler.execute(command);
		}
		in.close();
	}	
}