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
	public void execute(CommandSender arg0, String[] arg1) {
		if (!(arg0.hasPermission("BungeeSuite.mod") || arg0
				.hasPermission("BungeeSuite.admin"))) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
		cp.sendServer();
		arg0.sendMessage(plugin.PLAYER_SENDING_SERVER);
	}

}
