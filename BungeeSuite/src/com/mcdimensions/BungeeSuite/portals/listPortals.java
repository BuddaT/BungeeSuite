package com.mcdimensions.BungeeSuite.portals;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class listPortals extends Command {

	private BungeeSuite plugin;

	public listPortals(BungeeSuite bungeeSuite) {
		super(bungeeSuite.listportals);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		String message = ChatColor.DARK_AQUA+"Portals: "+ChatColor.WHITE;
	try {
		for(String data:plugin.getUtilities().getPortals()){
			message +=data+", ";
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	arg0.sendMessage(message);
	}

}
