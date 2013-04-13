package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class PardonCommand extends Command {

	BungeeSuite plugin;

	public PardonCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.pardon);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.mod")) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			arg0.sendMessage(ChatColor.RED + "/" + plugin.pardon
					+ " (PlayerName)");
			return;
		}

		String name = arg1[0];
		try {
			if (plugin.getUtilities().isBanned(name)) {
				try {
					plugin.getUtilities().unbanPlayer(name);
					String bmessage = plugin.PLAYER_UNBANNED;
					bmessage = bmessage.replace("%player", arg1[0]);
					arg0.sendMessage(bmessage);
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				String bmessage = plugin.PLAYER_NOT_BANNED;
				bmessage = bmessage.replace("%player", arg1[0]);
				arg0.sendMessage(bmessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
