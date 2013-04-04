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
		if(!arg0.hasPermission("BungeeSuite.mod")){
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/"+plugin.unbanip+" (IP)");
			return;
		}
		String name  = arg1[0];
		if(plugin.IPbans.contains(name)){
			try {
				plugin.getUtilities().unbanIP(name);
				arg0.sendMessage(plugin.IP_UNBANNED);
				return;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			arg0.sendMessage(plugin.IP_NOT_EXIST);
		}
	}

}
