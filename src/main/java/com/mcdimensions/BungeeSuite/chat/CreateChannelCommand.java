package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CreateChannelCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.channels", "bungeesuite.chat.basic", 
		"bungeesuite.chat.*", "bungeesuite.createchannel", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };
	
	private static final String[] PERMISSION_NODES_OVERRIDE = { "bungeesuite.chat.channels.override", 
		"bungeesuite.admin", "bungeesuite.*" };

	public CreateChannelCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.createChannel, null, bungeeSuite.create);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		try {
			if(plugin.getUtilities().chatChannelExists(arg1[0])){
				sender.sendMessage(plugin.CHANNEL_ALREADY_EXISTS);
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (plugin.getChatPlayer(sender.getName()).getChannelsOwned() >= plugin.maxCustomChannels
				&& !CommandUtil.hasPermission(sender, PERMISSION_NODES_OVERRIDE)) {
			sender.sendMessage(plugin.CHANNEL_TOO_MANY);
			return;
		}
		if (arg1.length == 1) {
			String name = arg1[0];
			plugin.getUtilities().createChannel(name, plugin.defaultCustomChannelFormat, false, sender.getName());
			
			String chmsg = plugin.CHANNEL_CREATE_CONFIRM;
			chmsg = chmsg.replace("%channel", name);
			sender.sendMessage(chmsg);
		} else if (arg1.length == 2) {
			String name = arg1[0];
			String format = arg1[1];
			try {
				if(plugin.getUtilities().chatChannelExists(arg1[0])){
					sender.sendMessage(plugin.CHANNEL_ALREADY_EXISTS);
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			plugin.getUtilities().createChannel(name, format, false, sender.getName());
			
			String chmsg = plugin.CHANNEL_CREATE_CONFIRM;
			chmsg = chmsg.replace("%channel", name);
			sender.sendMessage(chmsg);
		} else
			sender.sendMessage(ChatColor.RED + "/" + plugin.create + " (channel) [format]");
	}

}
