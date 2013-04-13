package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class reply extends Command {
	BungeeSuite plugin;
	public reply(BungeeSuite bungeeSuite) {
		super(bungeeSuite.reply);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length<1){
			sender.sendMessage(ChatColor.RED+"/"+ plugin.reply+ " (message)");
			return;
		}
		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		String player = cp.getReplyPlayer();
		if (player == null) {
			sender.sendMessage(plugin.PLAYER_REPLY_NONE);
		} else {
			ChatPlayer rp = plugin.getChatPlayer(player);
			if (rp == null) {
				sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
			} else if (rp.ignoringPlayer(sender.getName())) {
				sender.sendMessage(plugin.PLAYER_IGNORING);
			} else {
				String message="";
				for(String data:args){
					message+=data+" ";
				}
				rp.sendPrivate(message, sender.getName());
				sender.sendMessage(ChatColor.GOLD+"[me->"+rp.getName()+"]"+ChatColor.WHITE+message);
			}
		}
	}

}
