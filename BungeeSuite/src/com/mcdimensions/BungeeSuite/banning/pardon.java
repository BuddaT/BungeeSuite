package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class pardon extends Command {

	
	BungeeSuite plugin;
	public pardon(BungeeSuite bungeeSuite) {
		super(bungeeSuite.pardon);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.mod"))return;
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/Pardon (PlayerName)");
			return;
		}
		String name  = arg1[0];
		try {
			if(plugin.getUtilities().isBanned(name)){
				try {
					plugin.getUtilities().unbanPlayer(name);
					arg0.sendMessage(ChatColor.DARK_GREEN+"Player unbanned!");
					return;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				arg0.sendMessage(ChatColor.RED+"Player was not banned!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
