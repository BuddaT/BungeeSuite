package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class createchannel extends Command {

	private BungeeSuite plugin;

	public createchannel(BungeeSuite bungeeSuite) {
		super(bungeeSuite.createchannel);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!(arg0.hasPermission("BungeeSuite.createchannel")|| arg0
				.hasPermission("BungeeSuite.admin"))) {
			arg0.sendMessage(ChatColor.RED
					+ "You do not have permission to create channels");
			return;
		}
		if(plugin.getChatPlayer(arg0.getName()).getChannelsOwned()>=plugin.maxCustomChannels && !arg0.hasPermission("BungeeSuite.admin")){
			arg0.sendMessage(ChatColor.RED+"You own too many channels already");
			return;
		}
		if (arg1.length == 1) {
			String name = arg1[0];
			plugin.getUtilities().createChannel(name,
					plugin.defaultCustomChannelFormat, false, arg0.getName());
			arg0.sendMessage(ChatColor.DARK_GREEN + "Channel created");
			return;
		} else if (arg1.length == 2) {
			String name = arg1[0];
			String format = arg1[1];
			plugin.getUtilities().createChannel(name, format, false,
					arg0.getName());
			arg0.sendMessage(ChatColor.DARK_GREEN + "Channel created");
			return;
		} else
			arg0.sendMessage(ChatColor.RED
					+ "/createchannel (channel name) *(channel format)");
	}

}
