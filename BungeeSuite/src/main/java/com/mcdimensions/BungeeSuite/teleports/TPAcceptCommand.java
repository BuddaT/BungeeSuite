package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TPAcceptCommand extends Command {

	BungeeSuite plugin;

	public TPAcceptCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpAccept);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		if (plugin.blockedTeleports.contains(player.getServer().getInfo().getName())) {
			sender.sendMessage(ChatColor.RED + "This server does not have teleports enabled");
			return;
		}
		
		if (plugin.tpaList.containsKey(player)) {
			ProxiedPlayer targetPlayer = plugin.tpaList.get(player);
			plugin.tpaList.remove(player);
			plugin.getUtilities().teleportToPlayer(targetPlayer, player);
			return;
		}
		
		if (plugin.tpHereList.containsKey(player)) {
			ProxiedPlayer targetPlayer = plugin.tpHereList.get(player);
			plugin.tpaList.remove(player);
			plugin.getUtilities().teleportToPlayer(player, targetPlayer);
			return;
		}
	}

}
