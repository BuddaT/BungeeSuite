package com.mcdimensions.BungeeSuite.warps;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class delWarp extends Command {

	BungeeSuite plugin;
	public delWarp(BungeeSuite bungeeSuite) {
		super(bungeeSuite.delwarp);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/delwarp (warp name)");
			return;
		}
		try {
			if(plugin.getUtilities().warpExists(arg1[0])){
				plugin.getUtilities().deleteWarp(arg1[0]);
				arg0.sendMessage(ChatColor.DARK_GREEN+"Warp deleted!");
				return;
			}else{
				arg0.sendMessage(ChatColor.RED+"Warp deos not exist!");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
