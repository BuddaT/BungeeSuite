package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

	BungeeSuite plugin;
	
	public BroadcastCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.broadcast);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin")) return;
		
		String message = "";
		for(String data:arg1)
			message+=data+" ";
		
		message = message.substring(0, message.length()-1);
		String bmessage = plugin.BROADCAST_MESSAGE;
		bmessage = bmessage.replace("%message", message);
		bmessage= bmessage.replace("%sender", arg0.getName());
		
		for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
			data.sendMessage(bmessage);
		}
		
		if(plugin.logChat){
			plugin.cl.cLog("[Broadcast]: "+message);
		}
	}

}
