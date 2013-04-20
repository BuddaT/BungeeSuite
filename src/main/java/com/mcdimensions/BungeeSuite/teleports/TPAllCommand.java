package com.mcdimensions.BungeeSuite.teleports;



import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TPAllCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.teleport.tpall",
		"bungeesuite.admin", "bungeesuite.*" };

	public TPAllCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpAll);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length > 1) {
			String player = arg1[0];
			if (plugin.getProxy().getPlayer(player) != null) {
				for (ProxiedPlayer data : plugin.getProxy().getPlayers()) {
					plugin.getUtilities().teleportToPlayer(data, plugin.getProxy().getPlayer(player));
					data.sendMessage(ChatColor.DARK_GREEN + "All players have been teleported to " + player);
				}
			} else {
				sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
			}
		} else {
			for (ProxiedPlayer data : plugin.getProxy().getPlayers()) {
				plugin.getUtilities().teleportToPlayer(data, (ProxiedPlayer) sender);
				data.sendMessage(ChatColor.DARK_GREEN + "All players have been teleported to " + sender.getName());
			}
		}
	}

}
