package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MessageCommand extends Command {

	BungeeSuite plugin;

	public MessageCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.message, null, bungeeSuite.msg, bungeeSuite.whisper,
				bungeeSuite.tell, bungeeSuite.t);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
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
				
				cp.sendPrivate(message, sender.getName());
				sender.sendMessage(ChatColor.GOLD + "[me->" + cp.getName() + "]" + ChatColor.WHITE + message);
			}
		} else {
			sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
		}
	}

}
