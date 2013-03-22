package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class tp extends Command {
	BungeeSuite plugin;
	public tp(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tp);
		plugin = bungeeSuite;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender arg0, String[] args) {
		if(args.length<1){
			arg0.sendMessage(ChatColor.RED+"/tp (playername)");
			return;
		}
		if(args.length==1 && (arg0.hasPermission("BungeeSuite.mod") || arg0.hasPermission("BungeeSuite.admin"))){
			ProxiedPlayer playerTo = plugin.getUtilities().getClosestPlayer(args[0]);
			if(playerTo==null){
				arg0.sendMessage(ChatColor.RED+"That player is not online!");
				return;
			}
			plugin.getUtilities().teleportToPlayer((ProxiedPlayer)arg0, playerTo);
			return;
		}
		if(args.length>1 && arg0.hasPermission("BungeeSuite.admin")){
			ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(args[0]);
			ProxiedPlayer playerTo =plugin.getUtilities().getClosestPlayer(args[1]);
			if(player==null || playerTo==null){
				arg0.sendMessage(ChatColor.RED+"That player is not online!");
				return;
			}
			plugin.getUtilities().teleportToPlayer(player,playerTo);
			return;
		}

	}

}
