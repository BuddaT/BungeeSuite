package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MessageCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.message", "bungeesuite.chat.*",
		"bungeesuite.chat.basic", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public MessageCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.message, null, bungeeSuite.msg, bungeeSuite.whisper,
				bungeeSuite.tell, bungeeSuite.t);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 2) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.message + " (player) (message)");
			return;
		}

		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(arg1[0]);
		if (player != null) {
			ChatPlayer cp = plugin.getChatPlayer(player.getName());
			
			if (cp.ignoringPlayer(sender.getName()))
				sender.sendMessage(plugin.PLAYER_IGNORING);
			else {
				String message = "";
				for (String data : arg1) {
					if (!data.equalsIgnoreCase(arg1[0])) {
						message += data + " ";
					}
				}
				
				cp.sendPrivateMessage(message, sender.getName());
				sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + "me" + ChatColor.GRAY + 
						"->" + ChatColor.GOLD + cp.getName() + ChatColor.GRAY + "] " + message);
			}
		} else {
			sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
		}
	}

}
