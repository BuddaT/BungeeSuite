package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class tempban extends Command {

	BungeeSuite plugin;

	public tempban(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tempban);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.mod"))
			return;
		if (arg1.length < 1) {
			arg0.sendMessage(ChatColor.RED
					+ "/TempBan (PlayerName) (d:days h:hours m:minutes)");
			return;
		}
		try {
			if (plugin.getUtilities().playerExists(arg1[0])) {
				String player = arg1[0];
				if (arg1.length == 1) {
					arg0.sendMessage("Must define a ban time /tempban (playername) (d:days h:hours m:minutes)");
					return;
				}
				int minuteIncrease = 0;
				int hourIncrease = 0;
				int dateIncrease = 0;
				for (int i = 1; i < arg1.length; i++) {
					try {
						//days
						if (arg1[i].substring(0, 1).equalsIgnoreCase("d:")) {
							dateIncrease += Integer.parseInt(arg1[i].substring(
									2, arg1[i].length()));
						}
						//hours
						if (arg1[i].substring(0, 1).equalsIgnoreCase("h:")) {
							hourIncrease += Integer.parseInt(arg1[i].substring(
									2, arg1[i].length()));
						}
						//minutes
						if (arg1[i].substring(0, 1).equalsIgnoreCase("m:")) {
							minuteIncrease += Integer.parseInt(arg1[i]
									.substring(2, arg1[i].length()));
						}
					} catch (NumberFormatException e) {
						arg0.sendMessage(ChatColor.RED
								+ "An incorrect value was used to the time /tempban (playername) (d:days h:hours, m: minutes)");
					}
				}
				plugin.getUtilities().tempBanPlayer(player, minuteIncrease,
						hourIncrease, dateIncrease);
				plugin.getUtilities().sendBroadcast(ChatColor.DARK_GREEN+"["+arg1[0]+"]"+ChatColor.GOLD+" has been temporarilly banned from the server for "+dateIncrease+":days "+hourIncrease+":hours "+ minuteIncrease+":minutes");

			} else {
				arg0.sendMessage(ChatColor.RED + "That player does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
