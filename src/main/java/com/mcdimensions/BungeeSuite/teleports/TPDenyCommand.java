package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TPDenyCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.teleport.deny", "bungeesuite.teleport.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public TPDenyCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpDeny);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		if (plugin.blockedTeleports.contains(player.getServer().getInfo().getName())) {
			sender.sendMessage(plugin.TELEPORTS_NOT_ENABLED);
			return;
		}
		
		if (plugin.tpaList.containsKey(player)) {
			ProxiedPlayer targetPlayer = plugin.tpaList.get(player);
			plugin.tpaList.remove(player);
			targetPlayer.sendMessage(ChatColor.RED + player.getDisplayName() + " has denied your request");
		}
		
		if (plugin.tpHereList.containsKey(player)) {
			ProxiedPlayer targetPlayer = plugin.tpHereList.get(player);
			plugin.tpHereList.remove(player);
			targetPlayer.sendMessage(ChatColor.RED + player.getDisplayName() + " has denied your request");
		}

	}

}
