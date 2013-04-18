package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.ban.ban", "bungeesuite.ban.*",
			"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public BanCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.ban);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.ban + " (PlayerName) (message)");
			return;
		}

		try {
			if (plugin.getUtilities().playerExists(arg1[0])) {
				String message = plugin.DEFAULT_BAN_PLAYER;

				if (arg1.length > 1) {
					message = "";
					for (String data : arg1) {
						if (!data.equalsIgnoreCase(arg1[0]))
							;
						message += data + " ";
					}
				}
				plugin.getUtilities().banPlayer(arg1[0], message);
				
				String bmessage = plugin.BAN_MESSAGE_BROADCAST.replace("%player", arg1[0]);
				bmessage = bmessage.replace("%sender", sender.getName());
				bmessage = bmessage.replace("%message", message);
				plugin.getUtilities().sendBroadcast(bmessage);
			} else {
				sender.sendMessage(plugin.PLAYER_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
