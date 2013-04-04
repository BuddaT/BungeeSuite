package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class chatspy extends Command {
	BungeeSuite plugin;
	public chatspy(BungeeSuite bungeeSuite) {
		super(bungeeSuite.chatspy);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!(arg0.hasPermission("BungeeSuite.admin")|| arg0.hasPermission("BungeeSuite.chatspy"))){
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
		cp.ChatSpy();
		arg0.sendMessage(plugin.CHATSPY_TOGGLED);

	}

}
