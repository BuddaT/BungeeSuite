package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class TempBanCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.ban.tempban", "bungeesuite.ban.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public TempBanCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tempban, null, bungeeSuite.tban);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.tempban
					+ " (PlayerName) (d:days h:hours m:minutes)");
			return;
		}
		
		try {
			if (plugin.getUtilities().playerExists(arg1[0])) {
				String player = arg1[0];
				if (arg1.length == 1) {
					sender.sendMessage(ChatColor.RED
							+ "Must define a ban time /tempban (playername) (d:days h:hours m:minutes)");
					return;
				}
				
				int minuteIncrease = 0;
				int hourIncrease = 0;
				int dateIncrease = 0;
				for (int i = 1; i < arg1.length; i++) {
					try {
						if (arg1[i].substring(0, 2).equalsIgnoreCase("d:")) {
							dateIncrease += Integer.parseInt(arg1[i].substring(
									2, arg1[i].length()));
						}
						if (arg1[i].substring(0, 2).equalsIgnoreCase("h:")) {
							hourIncrease += Integer.parseInt(arg1[i].substring(
									2, arg1[i].length()));
						}
						if (arg1[i].substring(0, 2).equalsIgnoreCase("m:")) {
							minuteIncrease += Integer.parseInt(arg1[i]
									.substring(2, arg1[i].length()));
						}
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "An incorrect value was used for the time. /tempban (playername) (d:days h:hours, m: minutes)");
						return;
					}
				}
				plugin.getUtilities().tempBanPlayer(player, minuteIncrease,
						hourIncrease, dateIncrease);
				
				String tmessage = plugin.TEMP_BAN_BROADCAST;
				tmessage = tmessage.replace("%player", arg1[0]);
				tmessage = tmessage.replace("%sender", sender.getName());
				tmessage = tmessage.replace("%days", dateIncrease + "");
				tmessage = tmessage.replace("%hours", hourIncrease + "");
				tmessage = tmessage.replace("%minutes", minuteIncrease + "");
				plugin.getUtilities().sendBroadcast(tmessage);
			} else {
				sender.sendMessage(plugin.PLAYER_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
