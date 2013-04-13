package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReplyCommand extends Command {

	BungeeSuite plugin;

	public ReplyCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.reply, null, bungeeSuite.r);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.reply + " (message)");
			return;
		}

		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		String target = cp.getReplyPlayer();
		
		if (target != null) {
			ChatPlayer rp = plugin.getChatPlayer(target);
			if (rp == null)
				sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
			else if (rp.ignoringPlayer(sender.getName()))
				sender.sendMessage(plugin.PLAYER_IGNORING);
			else {
				String message = "";
				for (String data : arg1)
					message += data + " ";

				rp.sendPrivate(message, sender.getName());
				sender.sendMessage(ChatColor.GOLD + "[me->" + rp.getName() + "]" + ChatColor.WHITE + message);
				return;
			}
		} else {
			sender.sendMessage(plugin.PLAYER_REPLY_NONE);
		}
	}

}
