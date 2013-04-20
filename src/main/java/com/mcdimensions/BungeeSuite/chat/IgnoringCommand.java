package com.mcdimensions.BungeeSuite.chat;

import java.util.HashSet;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class IgnoringCommand extends Command {

	private BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.ignore", "bungeesuite.chat.basic",
		"bungeesuite.chat.*", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public IgnoringCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.ignoring);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		HashSet<String> list = plugin.getChatPlayer(sender.getName()).getIgnores();
		String message = ChatColor.DARK_AQUA + "Ignoring players: " + ChatColor.RESET;
		
		for (String data : list) {
			message += data + ", ";
		}
		
		sender.sendMessage(message);
	}

}
