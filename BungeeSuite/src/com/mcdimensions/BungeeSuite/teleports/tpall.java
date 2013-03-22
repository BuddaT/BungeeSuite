package com.mcdimensions.BungeeSuite.teleports;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class tpall extends Command {

	BungeeSuite plugin;
	public tpall(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpall);
		plugin = bungeeSuite;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		//tpall to player
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		if(arg1.length>1){
			String player = arg1[0];
				if(plugin.getProxy().getPlayer(player)!=null){
					for(ProxiedPlayer data :plugin.getProxy().getPlayers()){
						plugin.getUtilities().teleportToPlayer(data, plugin.getProxy().getPlayer(player));
						data.sendMessage(ChatColor.DARK_GREEN+"All players have been teleported to "+ player);
					}
					return;
				}else{
					arg0.sendMessage(ChatColor.RED+"That player is not online!");
					return;
				}
		}
		//tp to player who sent command
		else{
			for(ProxiedPlayer data :plugin.getProxy().getPlayers()){
				plugin.getUtilities().teleportToPlayer(data, (ProxiedPlayer) arg0);
				data.sendMessage(ChatColor.DARK_GREEN+"All players have been teleported to "+ arg0.getName());
			}
		}
	}

}
