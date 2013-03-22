package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class tpa extends Command {

	BungeeSuite plugin;
	public tpa(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpa);
		plugin = bungeeSuite;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute(CommandSender arg0, String[] args) {
		if(args.length<1){
			arg0.sendMessage(ChatColor.RED+"/tpa (playername)");
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) arg0;
		if(plugin.blockedTeleports.contains(player.getServer().getInfo().getName())){
			arg0.sendMessage(ChatColor.RED+"This server does not have teleports enabled");
			return;
		}
		if(args.length<1){
			arg0.sendMessage(ChatColor.RED+"/tpa (PlayerName)");
			return;
		}
		ProxiedPlayer targetPlayer = plugin.getUtilities().getClosestPlayer(args[0]);
		if(targetPlayer==null){
			arg0.sendMessage(ChatColor.RED+"That player is not online!");
			return;
		}
		plugin.getUtilities().sendTpRequest(player, targetPlayer);
		arg0.sendMessage(ChatColor.DARK_GREEN+"TP request sent!");
		return;

	}

}
