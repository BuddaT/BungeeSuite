package com.mcdimensions.BungeeSuite.banning;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class kickall extends Command {

	BungeeSuite plugin;
	public kickall(BungeeSuite bungeeSuite) {
		super(bungeeSuite.kickall);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		String message = "You have been kicked by "+ arg0.getName();
		if(arg1.length>0){
			message = "";
			for(String data :arg1){
				message+=data+" ";
			}
		}

			for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
				data.disconnect(message);
			}
			return;
	}

}
