package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ToggleCommand extends Command {
	
	BungeeSuite plugin;

	public ToggleCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.toggle);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (arg1.length == 0) {
			ArrayList<ChatChannel> channels = null;
			try {
				channels = plugin.getUtilities().getPlayersChannels(sender.getName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			ChatPlayer cp = plugin.getChatPlayer(sender.getName());
			ChatChannel cur = cp.getCurrent();
			int check = 0;
			ChatChannel next = null;
			ChatChannel data = null;
			
			Iterator<ChatChannel> it = channels.iterator();
			while (it.hasNext()) {
				data = it.next();
				if (check == 1) {
					next = data;
					break;
				}
				if (data.equals(cur)) {
					if (it.hasNext()) {
						check = 1;
					} else {
						next = channels.iterator().next();
					}

				}
			}
			cp.setCurrent(next);
			return;
		}
		
		String channel = arg1[0];
		try {
			if (plugin.getUtilities().chatChannelExists(channel)) {
				ChatChannel cc = plugin.getChannel(channel);
				ChatPlayer cp = plugin.getChatPlayer(sender.getName());
				
				if (cc.containsMember(cp.getName())) {
					cp.setCurrent(cc);
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