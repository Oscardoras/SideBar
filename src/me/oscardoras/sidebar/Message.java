package me.oscardoras.sidebar;

import me.oscardoras.spigotutils.io.TranslatableMessage;

public class Message extends TranslatableMessage {
	
	public Message(String path, String... args) {
		super(SideBarPlugin.plugin, path, args);
	}
	
}