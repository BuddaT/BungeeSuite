package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {

	BungeeSuite plugin;

	public BanCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.ban);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.mod")) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			arg0.sendMessage(ChatColor.RED + "/" + plugin.ban
					+ " (PlayerName) (message)");
			return;
		}

		try {
			if (plugin.getUtilities().playerExists(arg1[0])) {
				String message = plugin.DEFAULT_BAN_PLAYER;
				message = message.replace("%sender", arg0.getName());

				// puts together a ban message.
				if (arg1.length > 1) {
					message = "";
					for (String data : arg1) {
						if (!data.equalsIgnoreCase(arg1[0]))
							;
						message += data + " ";
					}
				}

				plugin.getUtilities().banPlayer(arg1[0], message);
				String bmessage = plugin.BAN_MESSAGE_BROADCAST.replace(
						"%player", arg1[0]);
				bmessage = bmessage.replace("%sender", arg0.getName());
				bmessage = bmessage.replace("%message", message);
				plugin.getUtilities().sendBroadcast(bmessage);
			} else {
				arg0.sendMessage(plugin.PLAYER_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
