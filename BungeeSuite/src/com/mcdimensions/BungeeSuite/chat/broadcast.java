package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class broadcast extends Command {

	BungeeSuite plugin;
	public broadcast(BungeeSuite bungeeSuite) {
		super(bungeeSuite.broadcast);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		String message = "";
		for(String data:arg1){
			message+=data+" ";
		}
		message = message.substring(0, message.length()-1);
		for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
			data.sendMessage(ChatColor.GOLD+"["+ChatColor.DARK_RED+"Broadcast"+ChatColor.GOLD+"]"+ChatColor.GREEN+message);
		}
		if(plugin.logChat){
			plugin.cl.Color("[Broadcast]: "+message);
		}
	}

}
