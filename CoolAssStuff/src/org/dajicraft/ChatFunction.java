package org.dajicraft;

public interface ChatFunction {
	// This will be used for private messaging in later versions
	public boolean message(String msg, User sender, User recipient);
	// This is for sending messages to everyone used in basic chatroom messages
	public boolean broadcast(String msg, User user);
	// Formats message
	public String formatMsg(String prefix, String username,
			String suffix, String msg);
	// Setting prefix
	public void setPrefix(String newPrefix);
	// Setting suffix
	public void setSuffix(String newSuffix);
	// Setting name
	public void setName(String newName);
	// Setting room
	public void setRoom(String roomName);
}