package com.mcdimensions.BungeeSuite.warps;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class warps extends Command {
	BungeeSuite plugin;
	public warps(BungeeSuite bungeeSuite) {
		super("warps");
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		String[] list = null;
		try {
			list = plugin.getUtilities().getWarpList(arg0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		arg0.sendMessage(list[0]);
		if(arg0.hasPermission("BungeeSuite.admin")){
			arg0.sendMessage(list[1]);
		}
		return;
		
	}
	

}