package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class tpahere extends Command {

	BungeeSuite plugin;
	public tpahere(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpahere);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] args) {
		if(args.length<1){
			arg0.sendMessage(ChatColor.RED+"/tpahere (playername)");
			return;
		}
		ProxiedPlayer targetPlayer = (ProxiedPlayer) arg0;
		if(plugin.blockedTeleports.contains(targetPlayer.getServer().getInfo().getName())){
			arg0.sendMessage(ChatColor.RED+"This server does not have teleports enabled");
			return;
		}
		if(plugin.blockedTeleports.contains(targetPlayer.getServer().getInfo().getName())){
			arg0.sendMessage(ChatColor.RED+"This server does not have teleports enabled");
			return;
		}
		if(args.length<1){
			arg0.sendMessage(ChatColor.RED+"/tpahere (PlayerName)");
			return;
		}
		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(args[0]);
		if(player==null){
			arg0.sendMessage(ChatColor.RED+"That player is not online!");
			return;
		}
		plugin.getUtilities().sendTpHereRequest(player, targetPlayer);
		arg0.sendMessage(ChatColor.DARK_GREEN+"TP request sent!");
		return;

	}

}
