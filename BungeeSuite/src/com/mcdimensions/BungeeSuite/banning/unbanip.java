package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class unbanip extends Command {

	BungeeSuite plugin;
	public unbanip(BungeeSuite bungeeSuite) {
		super(bungeeSuite.unbanip);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.mod"))return;
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/UnbanIP (IP)");
			return;
		}
		String name  = arg1[0];
		if(plugin.IPbans.contains(name)){
			try {
				plugin.getUtilities().unbanIP(name);
				arg0.sendMessage(ChatColor.DARK_GREEN+"IP unbanned!");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			arg0.sendMessage(ChatColor.RED+"IP was not banned!");
		}
	}

}
