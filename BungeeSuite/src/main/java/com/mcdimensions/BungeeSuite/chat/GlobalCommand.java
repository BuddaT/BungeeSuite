package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class GlobalCommand extends Command {

	BungeeSuite plugin;
	public static final String[] PERMISSION_NODES = { "bungeesuite.chat.global", "bungeesuite.chat.basic", "bungeesuite.chat.*", 
		"bungeesuite.global", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public GlobalCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.global, null, bungeeSuite.g);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length == 0) {
			ChatChannel cc = plugin.getChannel("Global");
			ChatPlayer cp = plugin.getChatPlayer(sender.getName());
			if (plugin.globalToggleable) {
				if (!cp.getCurrentChannel().equals(cc)) {
					cp.setCurrentChannel(cc);
				}
			}
			return;
		}

		String message = "";
		for (String data : arg1) {
			message += data + " ";
		}

		ChatChannel cc = plugin.getChannel("Global");
		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		cc.sendGlobalMessage(cp, message);
		if (plugin.globalToggleable) {
			if (!cp.getCurrentChannel().equals(cc)) {
				cp.setCurrentChannel(cc);
			}
		}
	}

}
