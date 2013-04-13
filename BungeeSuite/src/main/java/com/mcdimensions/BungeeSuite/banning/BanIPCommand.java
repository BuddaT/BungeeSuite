package com.mcdimensions.BungeeSuite.banning;

import java.net.UnknownHostException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BanIPCommand extends Command {

	BungeeSuite plugin;

	public BanIPCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.ipban);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!sender.hasPermission("BungeeSuite.mod")) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.ipban + " (PlayerName/IP)");
			return;
		}

		try {
			// if playername was inserted
			if (plugin.getUtilities().playerExists(arg1[0])) {
				String ip = null;
				try {
					// get players ip
					ip = plugin.getUtilities().getIP(arg1[0]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					plugin.getUtilities().IPBanPlayer(ip);
					String imessage = plugin.DEFAULT_IPBAN_PLAYER;
					imessage = imessage.replace("%player", ip);
					plugin.getUtilities().sendBroadcast(imessage);
				} catch (SQLException | UnknownHostException e) {
					e.printStackTrace();
				}
			} else {
				// if IP was typed in
				// TODO may need to add a '/' to the start
				plugin.getUtilities().IPBanPlayer(arg1[0]);
				
				String imessage = plugin.DEFAULT_IPBAN_PLAYER;
				imessage = imessage.replace("%player", arg1[0]);
				plugin.getUtilities().sendBroadcast(imessage);
			}
		} catch (SQLException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
