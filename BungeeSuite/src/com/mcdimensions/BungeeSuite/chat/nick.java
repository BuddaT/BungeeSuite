package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class nick extends Command {
	BungeeSuite plugin;
	public nick(BungeeSuite bungeeSuite) {
		super(bungeeSuite.nick);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!(arg0.hasPermission("BungeeSuite.nick") || arg0.hasPermission("BungeeSuite.admin")))return;
		if(arg1.length==1){
			String nick = arg1[0];
			if(nick.length()>16){
				nick = nick.substring(0,16);
			}
			try {
				plugin.getChatPlayer(arg0.getName()).setDisplayName(nick);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}
		if(arg1.length>1){
			try {
				if(plugin.getUtilities().playerExists(arg1[0])){
					String nick = arg1[1];
					if(nick.length()>16){
						nick = nick.substring(0,16);
					}
					if(plugin.OnlinePlayers.containsKey(arg1[0])){
						plugin.OnlinePlayers.get(arg1[0]).setDisplayName(nick);
						arg0.sendMessage(ChatColor.DARK_GREEN+"Nickname changed to " + nick);
					}else{
						plugin.getUtilities().setNickName(arg0.getName(), nick);
						arg0.sendMessage(ChatColor.DARK_GREEN+"Nickname set for " + nick);
					}
				}else{
					arg0.sendMessage(ChatColor.RED+"That player does not exist");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
