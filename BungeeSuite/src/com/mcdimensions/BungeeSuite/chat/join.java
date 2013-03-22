package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class join extends Command {
	BungeeSuite plugin;
	public join(BungeeSuite bungeeSuite) {
		super(bungeeSuite.join);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length!=1){
			arg0.sendMessage(ChatColor.RED+"/Join (Channel name)");
			return;
		}
		String channelName = arg1[0];
		String playerName = arg0.getName();
		try {
			if(plugin.getUtilities().chatChannelExists(channelName)){//channel exists
				ChatChannel cc = plugin.getChannel(channelName);
				if(cc.isInvited(playerName) || arg0.hasPermission("BungeeSuite.admin")){
					cc.acceptInvite(playerName);
					return;
				}else{
					arg0.sendMessage(ChatColor.RED+"You do not have permission to join this channel");
				}
			}else{//channel doesnt exist
				arg0.sendMessage(ChatColor.RED+"That channel does not exist");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
