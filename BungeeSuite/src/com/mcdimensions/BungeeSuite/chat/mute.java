package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class mute extends Command {

	BungeeSuite plugin;
	public mute(BungeeSuite bungeeSuite) {
		super(bungeeSuite.mute);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.mod") || !arg0.hasPermission("BungeeSuite.admin")){
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/"+plugin.mute+" (player)");
			return;
		}
		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(arg1[0]);
		if(player!=null){
			ChatPlayer cp = plugin.getChatPlayer(player.getName());
			cp.mute();
			if(cp.isMute()){
			player.sendMessage(plugin.PLAYER_MUTE);
			arg0.sendMessage(plugin.PLAYER_MUTED);
			}else{
				player.sendMessage(plugin.PLAYER_UNMUTE);
				arg0.sendMessage(plugin.PLAYER_UNMUTED);
			}
			return;
		}else{
			arg0.sendMessage(plugin.PLAYER_NOT_EXIST);
			return;
		}
	}

}
