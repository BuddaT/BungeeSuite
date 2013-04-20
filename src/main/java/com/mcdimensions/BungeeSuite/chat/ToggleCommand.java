package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ToggleCommand extends Command {
	
	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.channels", "bungeesuite.chat.*",
		"bungeesuite.chat.basic", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public ToggleCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.toggle);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length == 0) {
			ArrayList<String> channels = null;
			channels = plugin.getChatPlayer(sender.getName()).getChannels();
			if (channels.isEmpty()) {
				sender.sendMessage(ChatColor.RED
						+ "You have no channels to toggle to");
				return;
			}

			ChatPlayer cp = plugin.getChatPlayer(sender.getName());
			ChatChannel current = cp.getCurrentChannel();
			boolean next = false;

			for (String data : channels) {
				String newchannel = data;
				
				if (data.equals(channels.get(channels.size() - 1))
						&& next == false) {
					cp.setCurrentChannel(plugin.getChannel(channels.get(0)));
					return;
				}
				
				if (newchannel.equals(current.getName())) {
					next = true;
				} else {
					if (next) {
						cp.setCurrentChannel(plugin.getChannel(newchannel));
						return;
					}
				}
				
			}
		}
		
		String channel = arg1[0];
		try {
			if (plugin.getUtilities().chatChannelExists(channel)) {
				ChatChannel cc = plugin.getChannel(channel);
				ChatPlayer cp = plugin.getChatPlayer(sender.getName());
				
				if (cc.containsMember(cp.getName())) {
					cp.setCurrentChannel(cc);
				} else {
					sender.sendMessage(plugin.CHANNEL_TOGGLE_PERMISSION);
				}
			} else {
				sender.sendMessage(plugin.CHANNEL_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}