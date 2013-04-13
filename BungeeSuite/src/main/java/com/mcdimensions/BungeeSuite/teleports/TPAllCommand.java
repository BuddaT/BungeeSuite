package com.mcdimensions.BungeeSuite.teleports;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TPAllCommand extends Command {

	BungeeSuite plugin;

	public TPAllCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpAll);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.admin"))
			return;
		
		if (arg1.length > 1) {
			String player = arg1[0];
			if (plugin.getProxy().getPlayer(player) != null) {
				for (ProxiedPlayer data : plugin.getProxy().getPlayers()) {
					plugin.getUtilities().teleportToPlayer(data, plugin.getProxy().getPlayer(player));
					data.sendMessage(ChatColor.DARK_GREEN + "All players have been teleported to " + player);
				}
			} else {
				arg0.sendMessage(ChatColor.RED + "That player is not online!");
			}
		} else {
			for (ProxiedPlayer data : plugin.getProxy().getPlayers()) {
				plugin.getUtilities().teleportToPlayer(data, (ProxiedPlayer) arg0);
				data.sendMessage(ChatColor.DARK_GREEN + "All players have been teleported to " + arg0.getName());
			}
		}
	}

}
