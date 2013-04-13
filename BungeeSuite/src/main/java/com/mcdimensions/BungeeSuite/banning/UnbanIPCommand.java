package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UnbanIPCommand extends Command {

	BungeeSuite plugin;

	public UnbanIPCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.unbanip);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!sender.hasPermission("BungeeSuite.mod")) {
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
