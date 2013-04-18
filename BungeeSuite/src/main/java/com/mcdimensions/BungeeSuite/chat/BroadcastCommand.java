package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.broadcast", "bungeesuite.chat.*",
		"bungeesuite.chat.admin", "bungeesuite.admin", "bungeesuite.*" };
	
	public BroadcastCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.broadcast);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		String message = "";
		for (String data : arg1)
			message += data + " ";

		message = message.substring(0, message.length() - 1);
		String bmessage = plugin.BROADCAST_MESSAGE;
		bmessage = bmessage.replace("%message", message+ChatColor.RESET);
		bmessage = bmessage.replace("%sender", sender.getName()+ChatColor.RESET);

		for (ProxiedPlayer data : plugin.getProxy().getPlayers()) {
			data.sendMessage(bmessage);
		}

		if (plugin.logChat) {
			plugin.cl.cLog("[Broadcast]: " + message);
		}
	}

}
