package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

	BungeeSuite plugin;

	public MuteCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.mute);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!sender.hasPermission("BungeeSuite.mod") || !sender.hasPermission("BungeeSuite.admin")) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.mute + " (player)");
			return;
		}
		
		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(arg1[0]);
		if (player != null) {
			ChatPlayer cp = plugin.getChatPlayer(player.getName());
			cp.toggleMute();
			if (cp.isMute()) {
				player.sendMessage(plugin.PLAYER_MUTE);
				sender.sendMessage(plugin.PLAYER_MUTED);
			} else {
				player.sendMessage(plugin.PLAYER_UNMUTE);
				sender.sendMessage(plugin.PLAYER_UNMUTED);
			}
			return;
		} else {
			sender.sendMessage(plugin.PLAYER_NOT_EXIST);
			return;
		}
	}

}
