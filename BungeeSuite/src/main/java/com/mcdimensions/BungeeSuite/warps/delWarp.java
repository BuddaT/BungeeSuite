package com.mcdimensions.BungeeSuite.warps;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class delWarp extends Command {

	BungeeSuite plugin;
	public delWarp(BungeeSuite bungeeSuite) {
		super(bungeeSuite.delWarp);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin")){
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/"+plugin.delWarp+" (warp name)");
			return;
		}
		try {
			if(plugin.getUtilities().warpExists(arg1[0])){
				plugin.getUtilities().deleteWarp(arg1[0]);
				arg0.sendMessage(plugin.WARP_DELETE_CONFIRM);
				return;
			}else{
				arg0.sendMessage(plugin.WARP_NOT_EXIST);
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
