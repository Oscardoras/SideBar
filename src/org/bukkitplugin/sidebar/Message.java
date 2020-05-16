package org.bukkitplugin.sidebar;

import org.bukkitutils.io.TranslatableMessage;

public class Message extends TranslatableMessage {
	
	public Message(String path, String... args) {
		super(SideBarPlugin.plugin, path, args);
	}
	
}