package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class muteall extends Command {

	BungeeSuite plugin;
	public muteall(BungeeSuite bungeeSuite) {
		super(bungeeSuite.muteall);
		plugin = bungeeSuite;
		
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		if(plugin.allMuted){
			plugin.allMuted = false;
			for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
				data.sendMessage(ChatColor.DARK_GREEN+"All players unmuted!");
			}
		}else{
			plugin.allMuted = true;
			for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
				data.sendMessage(ChatColor.RED+"All players muted!");
			}
		}

	}

}
