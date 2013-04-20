package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.ban.unban", "bungeesuite.ban.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public UnbanCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.unban, null, bungeeSuite.pardon);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.unban
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
					sender.sendMessage(bmessage);
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				String bmessage = plugin.PLAYER_NOT_BANNED;
				bmessage = bmessage.replace("%player", arg1[0]);
				sender.sendMessage(bmessage);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
