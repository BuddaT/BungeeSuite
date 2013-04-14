package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.channels", "bungeesuite.chat.*",
		"bungeesuite.chat.basic", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };
	
	public ServerCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.server, null, bungeeSuite.s);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length == 0) {
			ChatPlayer cp = plugin.getChatPlayer(sender.getName());
			ChatChannel cc = plugin.getChannel(cp.getPlayer().getServer() .getInfo().getName());
			
			if (plugin.globalToggleable)
				if (!cp.getCurrentChannel().equals(cc))
					cp.setCurrentChannel(cc);

			return;
		}
		
		String message = "";
		for (String data : arg1) {
			message += data + " ";
		}
		
		String server = ((ProxiedPlayer) sender).getServer().getInfo().getName();
		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		ChatChannel cc = plugin.getChannel(cp.getPlayer().getServer().getInfo().getName());
		
		if (plugin.ignoreServers.contains(server)) {
			return;
		}
		
		cc.sendMessage(cp, message);
		
		if (!cp.getCurrentChannel().equals(cc)) {
			cp.setCurrentChannel(cc);
		}
	}

}
