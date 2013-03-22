package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;
import java.util.HashSet;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class toggle extends Command {
	BungeeSuite plugin;
	public toggle(BungeeSuite bungeeSuite) {
		super(bungeeSuite.toggle);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length==0){
			HashSet<ChatChannel> channels = null;
			try {
				channels = plugin.getUtilities().getPlayersChannels(arg0.getName());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
			ChatChannel cur = cp.getCurrent();
			int check = 0;
			int count = 0;
			ChatChannel next = null;
			for(ChatChannel data: channels){
				if(check==1){
					next = data;
					break;
				}
				if(data.equals(cur)){
					check =1;
				}else{
					count++;
					if(count==channels.size()){
						next=channels.iterator().next();
					}
				}
			}
			cp.setCurrent(next);
			return;
		}
		String channel = arg1[0];
		try {
			if(plugin.getUtilities().chatChannelExists(channel)){//channel exists
				ChatChannel cc = plugin.getChannel(channel);
				ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
				if(cc.containsMember(cp.getName())){//player is member
					cp.setCurrent(cc);
				}else{//unable to toggle to this channel
					arg0.sendMessage(ChatColor.RED+"Unable to toggle to this channel");
				}
			}else{//channel does not exist
				arg0.sendMessage(ChatColor.RED+"Channel does not exist");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
