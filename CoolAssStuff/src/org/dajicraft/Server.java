package org.dajicraft;

import java.awt.Robot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {
	private static ArrayList<User> users = new ArrayList<User>();
	private static String code = "abc987";
	
	
	public static ArrayList<User> getAllUsers() {
		return users;
	}
	
	public static void removeUser(User user) {
		users.remove(user);
	}
	
	
	
	
	public static boolean broadcast(String msg, String room) {
		for(User ruser: new Server().getAllUsers()) {
			if(ruser.getRoom().equals(room)) {
				try {
					BufferedWriter rout = ruser.getBufferedWriter();
					rout.write(msg+"\n");
					rout.flush();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}
	
	
	public static boolean sessionHandler(Socket sock) {
		System.out.println("User Has Connected Running Through Setup");
		BufferedWriter out = null;
		

		
		
		
		

		
		
		Thread userThread = new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader in = null;
				BufferedWriter out = null;
				try {
					String username = null;
					String room = null;
					String opCode = null;
					String timeStamp = new SimpleDateFormat(
							"HH:mm::ss").format(new Date());
					
					/*
					 * in and out are the input and output
					 * of this temp account
					 */
					
					
					in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
					/*
					 * reading the client info needs to be changed with a time out and
					 * a full info read instead of this trash that is only for developement
					 */
					String line;
					
					char c;
					int a = in.read();
					while(a != -1) {
						c = (char)a;
						System.out.print(c);
						a = in.read();
					}
					
					
					/* 
					 * Checks for network side cloning needs to be removed
					 */
					for(User user: users) {
						if(user.getSocket().getInetAddress().equals(sock.getInetAddress()) && 
								user.getName().equals(username)) {
							System.out.println(username+" attempted to clone themself");
							sock.close();
							System.out.println(username+" has left");
							return;
						}
					}
					
					// Getting SysOp code if any
					opCode = in.readLine();
					
					
					
					opCode = opCode.substring("oc: ".length(), opCode.length());
					
					// Checking if the System Operator Code is right
					if(opCode.length() == code.length()) {
						if(opCode.substring(0, code.length()).equals(code)) {
							
							
							//  This person is a SysOp
							Operator sysOp = new Operator(username, room, sock,timeStamp, in, out, true);
							// But also a user
							User opUser = new User(username, room, sock,timeStamp, in, out, true);
							// This adds it into the server user list
							users.add(opUser);
							sysOp.setPrefix("[System Operator] ");
							
							
							/*
							 * System Operators Session
							 */
							while(sysOp.getSession()) {
								try {
									System.out.println("[System Operator] "+username+" has joined "+room);
									opUser.broadcast(opUser.getPrefix()+opUser.getName()+opUser.getSuffix()+" has joined!", opUser);
									String msg;
									while(true) {
										String rawMsg = sysOp.getBufferedReader().readLine();
										
										/*
										 * Checking if it's a dispatched command
										 */
										
										if(rawMsg.substring(0, 1).equals("/")) {
											// Runs command executiong
											sysOp.commandExecution(rawMsg, opUser);
										} else {
											/*
											 * Formats message and sends to all connected users
											 */
											
											msg = "["+new SimpleDateFormat("mm:ss").format(new Date())+"] "+sysOp.getPrefix()+sysOp.getName()+sysOp.getSuffix()+
													" >> "+rawMsg;
											System.out.println(msg);
											sysOp.broadcast(msg, opUser);
											msg = "";
										}
									}
								} catch (IOException e) {
									/*
									 * Runs everything needed to stop
									 * the connection
									 */
									sysOp.setSessoin(false);
									System.out.println(sysOp.getPrefix()+sysOp.getName()+sysOp.getSuffix()+" has left");
									users.remove(opUser);
									broadcast(sysOp.getPrefix()+sysOp.getName()+sysOp.getSuffix()+" has left", sysOp.getRoom());
								}
							}
						}
					} else {
						// Creating the user
						User newUser = new User(username, room, sock,timeStamp, in, out, true);
						// Adding into the server user list
						users.add(newUser);
						
						
						
						while(newUser.getSession()) {
							try {
								System.out.println(username+" has joined "+room);
								newUser.broadcast(newUser.getPrefix()+newUser.getName()+newUser.getSuffix()+" has joined!", newUser);
								String msg;
								while(true) {
									// Formatting the message and broadcasting it to all online users
									msg = "["+new SimpleDateFormat("mm:ss").format(new Date())+"] "+newUser.getPrefix()+newUser.getName()+newUser.getSuffix()+" >> "+
											newUser.getBufferedReader().readLine();
									System.out.println(msg);
									newUser.broadcast(msg, newUser);
									msg = "";
									
								}
							} catch (IOException e) {
								/*
								 * Runs everything needed to stop
								 * the connection
								 */
								newUser.setSessoin(false);
								System.out.println(newUser.getPrefix()+newUser.getName()+newUser.getSuffix()+" has left");
								users.remove(newUser);
								broadcast(newUser.getName()+" has left", newUser.getRoom());
							}
						}
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		// Starts the Clients handling in a Thread
		userThread.start();
		return true;
	}
}