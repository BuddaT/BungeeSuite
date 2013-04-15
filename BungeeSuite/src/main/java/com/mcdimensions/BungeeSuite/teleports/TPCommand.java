package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TPCommand extends Command {
	
	BungeeSuite plugin;
	public static final String[] PERMISSION_NODES = { "bungeesuite.teleport.tp",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };
	
	private static final String[] PERMISSION_NODES_P2P = { "bungeesuite.teleport.tp.p2p",
		"bungeesuite.admin", "bungeesuite.*" };

	public TPCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tp);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.tp + " (playername)");
			return;
		}
		
		if (args.length == 1) {
			ProxiedPlayer playerTo = plugin.getUtilities().getClosestPlayer(args[0]);
			
			if (playerTo == null) {
				sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
				return;
			}
			
			plugin.getUtilities().teleportToPlayer((ProxiedPlayer) sender, playerTo);
			return;
		}
		
		if (args.length > 1 && CommandUtil.hasPermission(sender, PERMISSION_NODES_P2P)) {
			ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(args[0]);
			ProxiedPlayer playerTo = plugin.getUtilities().getClosestPlayer(args[1]);
			
			if (player == null || playerTo == null) {
				sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
				return;
			}
			
			plugin.getUtilities().teleportToPlayer(player, playerTo);
			return;
		} else {
			sender.sendMessage(plugin.NO_PERMISSION);
		}

	}

}
