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
	public void execute(CommandSender arg0, String[] arg1) {
		if (arg1.length == 0) {// current
			ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
			ChatChannel current = cp.getCurrent();

			if (arg0.getName().equalsIgnoreCase(current.getOwner())
					|| arg0.hasPermission("BungeeSuite.admin")) {
				plugin.getUtilities().deleteChannel(current.getName());
				arg0.sendMessage(plugin.CHANNEL_DELETE_CONFIRM);
				return;
			} else {
				arg0.sendMessage(plugin.CHANNEL_NO_PERMISSION);
				return;
			}
		} else if (arg1.length == 1) {// other
			ChatChannel cc = plugin.getChannel(arg1[0]);
			try {
				if (!plugin.getUtilities().chatChannelExists(arg1[0])) {
					arg0.sendMessage(plugin.CHANNEL_NOT_EXIST);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (cc != null
					&& (arg0.getName().equalsIgnoreCase(cc.getOwner()) || arg0
							.hasPermission("BungeeSuite.admin"))) {
				plugin.getUtilities().deleteChannel(cc.getName());
				arg0.sendMessage(plugin.CHANNEL_DELETE_CONFIRM);
				return;
			} else {
				arg0.sendMessage(plugin.CHANNEL_NO_PERMISSION);
				return;
			}
		} else {// wrong
			arg0.sendMessage(ChatColor.RED + "/" + plugin.deleteChannel
					+ " *(Channel Name)");
			return;
		}
	}

}
