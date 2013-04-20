package com.mcdimensions.BungeeSuite.chat;

import java.util.ArrayList;
import java.util.Set;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ListChannelsCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = {
			"bungeesuite.chat.basic", "bungeeesuite.chat.channels",
			"bungeesuite.chat.*", "bungeesuite.mod", "bungeesuite.admin",
			"bungeesuite.*" };

	private static final String[] PERMISSION_NODES_OVERRIDE = {
			"bungeesuite.chat.list.admin", "bungeesuite.admin", "bungeesuite.*" };

	public ListChannelsCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.channelslist, null, bungeeSuite.channels);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		ArrayList<String> channelList = cp.getChannels();
		String current = cp.getCurrentChannel().getName();
		String message = ChatColor.DARK_AQUA
				+ "Channels you are currently in: ";
		for (String data : channelList) {
			if (data.equals(current)) {
				message += ChatColor.GOLD + data + ", ";
			} else {
				message += ChatColor.DARK_GRAY + data+", ";
			}
		}
		message = message.substring(0, message.length() - 2);
		sender.sendMessage(message);
		if (CommandUtil.hasPermission(sender, PERMISSION_NODES_OVERRIDE)) {
			message = ChatColor.DARK_AQUA + "All other channels: ";
			Set<String> allChannels = plugin.chatChannels.keySet();
			for (String data : allChannels) {
				if (!channelList.contains(data)) {
					message += ChatColor.DARK_GRAY + data + ", ";
				}
			}
			message = message.substring(0, message.length() - 2);
			sender.sendMessage(message);
		}
	}
}
