package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DeleteChannelCommand extends Command {

	BungeeSuite plugin;

	public DeleteChannelCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.deleteChannel, null, bungeeSuite.delete);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (arg1.length == 0) {
			ChatPlayer cp = plugin.getChatPlayer(sender.getName());
			ChatChannel current = cp.getCurrent();

			if (sender.getName().equalsIgnoreCase(current.getOwner()) || sender.hasPermission("BungeeSuite.admin")) {
				plugin.getUtilities().deleteChannel(current.getName());
				sender.sendMessage(plugin.CHANNEL_DELETE_CONFIRM);
			} else {
				sender.sendMessage(plugin.CHANNEL_NO_PERMISSION);
			}
		} else if (arg1.length == 1) {
			ChatChannel cc = plugin.getChannel(arg1[0]);
			try {
				if (!plugin.getUtilities().chatChannelExists(arg1[0])) {
					sender.sendMessage(plugin.CHANNEL_NOT_EXIST);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (cc != null && (sender.getName().equalsIgnoreCase(cc.getOwner()) || sender.hasPermission("BungeeSuite.admin"))) {
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
