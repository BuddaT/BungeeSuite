package com.mcdimensions.BungeeSuite.chat;

import java.util.HashSet;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ignoring extends Command {

	private BungeeSuite plugin;

	public ignoring(BungeeSuite bungeeSuite) {
		super(bungeeSuite.ignoring);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		HashSet<String> list = plugin.getChatPlayer(arg0.getName()).getIgnores();
		String message = ChatColor.DARK_AQUA+"Ignoring players: "+ ChatColor.RESET;
		for(String data: list){
			message+=data+", ";
		}
		arg0.sendMessage(message);
	}

}
