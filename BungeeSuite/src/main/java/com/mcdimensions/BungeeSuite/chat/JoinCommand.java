package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class JoinCommand extends Command {

	BungeeSuite plugin;

	public JoinCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.join);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (arg1.length != 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.join + " (channel)");
			return;
		}

		String channelName = arg1[0];
		String playerName = sender.getName();
		try {
			if (plugin.getUtilities().chatChannelExists(channelName)) {
				ChatChannel cc = plugin.getChannel(channelName);
				if (cc.isInvited(playerName) || sender.hasPermission("BungeeSuite.admin")) {
					cc.acceptInvite(playerName);
					return;
				} else {
					String jmsg = plugin.CHANNEL_NOT_INVITED;
					jmsg = jmsg.replace("%channel", cc.getName());
					sender.sendMessage(jmsg);
				}
			} else {
				sender.sendMessage(plugin.CHANNEL_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
