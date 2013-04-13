package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TPAHereCommand extends Command {

	BungeeSuite plugin;

	public TPAHereCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpaHere);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/tpahere (playername)");
			return;
		}
		
		ProxiedPlayer targetPlayer = (ProxiedPlayer) sender;
		
		if (plugin.blockedTeleports.contains(targetPlayer.getServer().getInfo().getName())) {
			sender.sendMessage(ChatColor.RED + "This server does not have teleports enabled");
			return;
		}
		
		if (plugin.blockedTeleports.contains(targetPlayer.getServer().getInfo().getName())) {
			sender.sendMessage(ChatColor.RED + "This server does not have teleports enabled");
			return;
		}
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/tpahere (PlayerName)");
			return;
		}
		
		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(ChatColor.RED + "That player is not online!");
			return;
		}
		
		plugin.getUtilities().sendTpHereRequest(player, targetPlayer);
		sender.sendMessage(ChatColor.DARK_GREEN + "TP request sent!");
	}

}
