package org.dajicraft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User implements ChatFunction {
	
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
	
	
	
	User(String username, String room, Socket sock,
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
			e.printStackTrace();
		}
		this.session = session;
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
	
	
	public BufferedWriter getBufferedWriter() {
		return out;
	}
	
	
	public void setSessoin(boolean state) {
		session = state;
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
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public String formatMsg(String prefix, String username,
			String suffix, String msg) {
		return prefix+username+suffix+msg;
	}

	@Override
	public void setPrefix(String prefix) {
		for(String permission: permissions) {
			if(permission == "set Prefix") this.prefix = prefix;
		}
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
		// TODO Auto-generated method stub
		
	}


	public String getSuffix() {
		return suffix;
	}


	public String getPrefix() {
		return prefix;
	}

	
	public void alert(String msg, User opUser) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(opUser.getSocket().getOutputStream()));
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
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