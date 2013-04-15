package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnbanIPCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.ban.unbanip", "bungeesuite.ban.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public UnbanIPCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.unbanip);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.unbanip + " (IP)");
			return;
		}

		String name = arg1[0];
		if (plugin.IPbans.contains(name)) {
			try {
				plugin.getUtilities().unbanIP(name);
				sender.sendMessage(plugin.IP_UNBANNED);
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			sender.sendMessage(plugin.IP_NOT_EXIST);
		}
	}

}
