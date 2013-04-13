package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ChatSpyCommand extends Command {

	BungeeSuite plugin;

	public ChatSpyCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.chatSpy);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!(sender.hasPermission("BungeeSuite.admin") || sender.hasPermission("BungeeSuite.chatspy"))) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		cp.ChatSpy();
		sender.sendMessage(plugin.CHATSPY_TOGGLED);
	}

}
