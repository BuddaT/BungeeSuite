package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ChatSpyCommand extends Command {

	BungeeSuite plugin;
	public static final String[] PERMISSION_NODES = { "bungeesuite.chat.spy", "bungeesuite.chat.admin", 
		"bungeesuite.chatspy", "bungeesuite.admin", "bungeesuite.*" };

	public ChatSpyCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.chatSpy);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		cp.toggleChatSpy();
		sender.sendMessage(plugin.CHATSPY_TOGGLED);
	}

}
