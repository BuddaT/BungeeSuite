package com.mcdimensions.BungeeSuite.banning;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickAllCommand extends Command {

	BungeeSuite plugin;
	
	public KickAllCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.kickall);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin")){
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		String kmessage = plugin.DEFAULT_KICK_PLAYER;
		kmessage.replace("%sender", arg0.getName());
		String message = kmessage;
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
