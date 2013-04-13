package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DisplayServerCommand extends Command {

	BungeeSuite plugin;

	public DisplayServerCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.displayServer);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!(sender.hasPermission("BungeeSuite.mod") || sender.hasPermission("BungeeSuite.admin"))) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		cp.sendServer();
		sender.sendMessage(plugin.PLAYER_SENDING_SERVER);
	}

}
