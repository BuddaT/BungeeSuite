package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TPAHereCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.teleport.tpahere", "bungeesuite.teleport.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public TPAHereCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpaHere);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "/"+plugin.tpaHere+" (playername)");
			return;
		}
		
		ProxiedPlayer targetPlayer = (ProxiedPlayer) sender;
		
		if (plugin.blockedTeleports.contains(targetPlayer.getServer().getInfo().getName())) {
			sender.sendMessage(plugin.TELEPORTS_NOT_ENABLED);
			return;
		}
		
		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(args[0]);
		if (player == null) {
			sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
			return;
		}
		
		plugin.getUtilities().sendTpHereRequest(player, targetPlayer);
		sender.sendMessage(plugin.TELEPORT_REQUEST_SENT);
	}

}
