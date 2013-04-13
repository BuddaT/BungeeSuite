package com.mcdimensions.BungeeSuite.banning;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

	BungeeSuite plugin;

	public KickCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.kick);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.mod")) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			arg0.sendMessage(ChatColor.RED + "/" + plugin.kick
					+ " (PlayerName)");
			return;
		}

		String name = arg1[0];

		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(name);
		String kmessage = plugin.DEFAULT_KICK_PLAYER;
		kmessage.replace("%sender", arg0.getName());
		String Message = kmessage;

		if (player != null) {
			if (arg1.length > 1) {
				int count = 0;
				for (String data : arg1) {
					if (count == 0) {
						Message = "";
						count++;
					} else {
						Message += data + " ";
					}
				}
			}
			player.disconnect(Message);
			String k2message = plugin.DEFAULT_KICK_BROADCAST;
			k2message.replace("%sender", arg0.getName());
			k2message.replace("%player", player.getDisplayName());
			k2message.replace("%message", Message);
			plugin.getUtilities().sendBroadcast(k2message);
		} else {
			arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
		}

	}

}
