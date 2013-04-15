package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DisplayServerCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.displayserver", "bungeesuite.chat.admin", 
		"bungeesuite.admin", "bungeesuite.*" };

	public DisplayServerCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.displayServer);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		cp.toggleSendingServer();
		sender.sendMessage(plugin.PLAYER_SENDING_SERVER);
	}

}
