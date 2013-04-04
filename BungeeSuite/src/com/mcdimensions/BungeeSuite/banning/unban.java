package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class unban extends Command {

	
	BungeeSuite plugin;
	public unban(BungeeSuite bungeeSuite) {
		super(bungeeSuite.unban);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.mod")){
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/"+plugin.unban+" (PlayerName)");
			return;
		}
		String name  = arg1[0];
		try {
			if(plugin.getUtilities().isBanned(name)){
				try {
					plugin.getUtilities().unbanPlayer(name);
					String bmessage = plugin.PLAYER_UNBANNED;
					bmessage= bmessage.replace("%player", arg1[0]);
					arg0.sendMessage(bmessage);
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				arg0.sendMessage(plugin.PLAYER_NOT_BANNED);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
