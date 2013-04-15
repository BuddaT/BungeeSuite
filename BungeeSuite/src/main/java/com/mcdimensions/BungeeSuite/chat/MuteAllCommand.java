package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteAllCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.muteall", 
		"bungeesuite.chat.admin", "bungeesuite.admin", "bungeesuite.*" };

	public MuteAllCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.muteAll);
		plugin = bungeeSuite;

	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (plugin.allMuted) {
			plugin.allMuted = false;
			for (ProxiedPlayer data : plugin.getProxy().getPlayers()) {
				data.sendMessage(plugin.PLAYER_ALL_UNMUTED);
			}
		} else {
			plugin.allMuted = true;
			for (ProxiedPlayer data : plugin.getProxy().getPlayers()) {
				data.sendMessage(plugin.PLAYER_ALL_MUTED);
			}
		}

	}

}
