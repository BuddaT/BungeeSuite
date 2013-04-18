package com.mcdimensions.BungeeSuite.banning;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.ban.kick", "bungeesuite.ban.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public KickCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.kick);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.kick + " (PlayerName)");
			return;
		}

		String name = arg1[0];

		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(name);
		String kmessage = plugin.DEFAULT_KICK_PLAYER;
		kmessage = kmessage.replace("%sender", sender.getName());

		if (player != null) {
			if (arg1.length > 1) {
				int count = 0;
				for (String data : arg1) {
					if (count == 0) {
						kmessage = "";
						count++;
					} else {
						kmessage += data + " ";
					}
				}
			}
			player.disconnect(kmessage);
			
			String k2message = plugin.DEFAULT_KICK_BROADCAST;
			k2message = k2message.replace("%sender", sender.getName());
			k2message = k2message.replace("%player", player.getDisplayName());
			k2message = k2message.replace("%message", kmessage);
			plugin.getUtilities().sendBroadcast(k2message);
		} else {
			sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
		}
	}

}
