package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DeleteChannelCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.channels", "bungeesuite.chat.basic", 
		"bungeesuite.chat.*", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };
	
	private static final String[] PERMISSION_NODES_OVERRIDE = { "bungeesuite.chat.channels.override", 
		"bungeesuite.admin", "bungeesuite.*" };

	public DeleteChannelCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.deleteChannel, null, bungeeSuite.delete);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length == 0) {
			ChatPlayer cp = plugin.getChatPlayer(sender.getName());
			ChatChannel current = cp.getCurrentChannel();
			if(current.isServerChannel()){
				sender.sendMessage(plugin.CHANNEL_INVITE_NOPERM);
				return;
			}
			if (sender.getName().equalsIgnoreCase(current.getOwner()) || CommandUtil.hasPermission(sender, PERMISSION_NODES_OVERRIDE)) {
				plugin.getUtilities().deleteChannel(current.getName());
				sender.sendMessage(plugin.CHANNEL_DELETE_CONFIRM);
			} else {
				sender.sendMessage(plugin.CHANNEL_NO_PERMISSION);
			}
		} else if (arg1.length == 1) {
			ChatChannel cc = plugin.getChannel(arg1[0]);
			if(cc.isServerChannel()){
				sender.sendMessage(plugin.CHANNEL_INVITE_NOPERM);
				return;
			}
			try {
				if (!plugin.getUtilities().chatChannelExists(arg1[0])) {
					sender.sendMessage(plugin.CHANNEL_NOT_EXIST);
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (cc != null && (sender.getName().equalsIgnoreCase(cc.getOwner()) || CommandUtil.hasPermission(sender, PERMISSION_NODES_OVERRIDE))) {
				plugin.getUtilities().deleteChannel(cc.getName());
				sender.sendMessage(plugin.CHANNEL_DELETE_CONFIRM);
			} else {
				sender.sendMessage(plugin.CHANNEL_NO_PERMISSION);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "/" + plugin.deleteChannel + " [channel]");
		}
	}

}
