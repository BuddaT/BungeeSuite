package com.mcdimensions.BungeeSuite.warps;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class WarpC extends Command {
	
	BungeeSuite plugin;


	

	public WarpC(BungeeSuite bungeeSuite) {
		super(bungeeSuite.warp);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/warp (name)");
			return;
		}
		else if(plugin.warpList.containsKey(arg1[0])){
			try {
				plugin.warpList.get(arg1[0]).warpPlayer((ProxiedPlayer) arg0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			try {
				if(plugin.getUtilities().warpExists(arg1[0])){
					Warp warp  = plugin.getUtilities().loadWarp(arg1[0]);
					warp.warpPlayer((ProxiedPlayer) arg0);
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
	}

}
