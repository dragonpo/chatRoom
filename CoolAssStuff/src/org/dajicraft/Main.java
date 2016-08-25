package org.dajicraft;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;

public class Main {
	public static void main(String args[]) {
		System.out.println("Server Started");
		/*
		 * Catching connections and running the
		 * all the things to set up that persons account
		 * or drop the connecting
		 */
		try {
			ServerSocket serverSock = new ServerSocket(100);
			System.out.println("Listening For Connections");
			while(true) {
				// Handlers function
				org.dajicraft.Server.sessionHandler(serverSock.accept());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}