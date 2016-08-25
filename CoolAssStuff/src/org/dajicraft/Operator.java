package org.dajicraft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Operator implements ChatFunction {
	
	private String username;
	private String prefix = "";
	private String suffix = "";
	private String room;
	private Socket sock;
	private String lastInteraction;
	private BufferedReader in;
	private BufferedWriter out;
	private boolean session;
	private ArrayList<String> permissions = new ArrayList<String>();
	
	
	
	Operator(String username, String room, Socket sock,
			String lastInteraction, BufferedReader in,
			BufferedWriter out, boolean session) {
		this.username = username;
		this.room = room;
		this.sock = sock;
		this.lastInteraction = lastInteraction;
		try {
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.session = session;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getName() {
		return username;
	}
	
	public String getRoom() {
		return room;
	}
	
	public Socket getSocket() {
		return sock;
	}
	
	public BufferedReader getBufferedReader() {
		return in;
	}
	
	
	public boolean getSession() {
		return session;
	}
	
	
	public void setSessoin(boolean state) {
		session = state;
	}
	
	
	public boolean universalBroadcast(String msg, User user) {
		for(User ruser: new Server().getAllUsers()) {
			try {
				BufferedWriter rout = ruser.getBufferedWriter();
				rout.write(msg+"\n");
				rout.flush();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}
		return false;
	}
	
	private void alert(String msg, User opUser) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(opUser.getSocket().getOutputStream()));
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public boolean broadcast(String msg, User user) {
		for(User ruser: new Server().getAllUsers()) {
			if(ruser.getRoom().equals(user.getRoom())) {
				try {
					BufferedWriter rout = ruser.getBufferedWriter();
					rout.write(msg+"\n");
					rout.flush();
				} catch (IOException e) {
					try {
						ruser.getSocket().close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Server.removeUser(ruser);
					Server.broadcast(ruser.getPrefix()+ruser.getName()+ruser.getSuffix()+" has left!", ruser.getRoom());
					System.out.println(ruser.getPrefix()+ruser.getName()+ruser.getSuffix()+" has left!");
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	
	public boolean commandExecution(String cmd, User opUser) {
		try {
			if(cmd.substring(0, "/kick".length()).equals("/kick")) {
				String name = cmd.substring("/kick ".length(), cmd.length());
				for(User user: Server.getAllUsers()) {
					if(user.getName().equals(name)) {
						try {
							user.alert("You have been Kick!", user);
							user.getSocket().close();
							Server.removeUser(user);
							System.out.println(user.getPrefix()+user.getName()+user.getSuffix()+" has been kicked!");
							Server.broadcast(user.getPrefix()+user.getName()+user.getSuffix()+" has been kicked!", user.getRoom());
							alert(user.getPrefix()+user.getName()+user.getSuffix()+" has successfully been kicked!", opUser);
							return true;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				opUser.message(name+" has been successfully kicked", opUser, opUser);
				System.out.println("No user named "+name);
				return false;
			}
		} catch(StringIndexOutOfBoundsException e) {
			
		}
		return true;
	}
	
	@Override
	public String formatMsg(String prefix, String username,
			String suffix, String msg) {
		return prefix+username+suffix+msg;
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void setSuffix(String suffix) {
		for(String permission: permissions) {
			if(permission == "set Prefix") this.suffix = suffix;
		}
	}


	@Override
	public void setName(String newName) {
		username = newName;
	}


	@Override
	public void setRoom(String roomName) {
		room = roomName;
	}

	@Override
	public boolean message(String msg, User sender, User recipient) {
		BufferedWriter out = recipient.getBufferedWriter();
		try {
			out.write(sender.getPrefix()+sender.getName()+sender.getSuffix()+" >> "+msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}