package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class muteall extends Command {

	BungeeSuite plugin;
	public muteall(BungeeSuite bungeeSuite) {
		super(bungeeSuite.muteAll);
		plugin = bungeeSuite;
		
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		if(plugin.allMuted){
			plugin.allMuted = false;
			for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
				data.sendMessage(plugin.PLAYER_ALL_UNMUTED);
			}
		}else{
			plugin.allMuted = true;
			for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
				data.sendMessage(plugin.PLAYER_ALL_UNMUTED);
			}
		}

	}

}
