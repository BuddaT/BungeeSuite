package com.mcdimensions.BungeeSuite.portals;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Portals extends Command {

	private BungeeSuite plugin;

	public Portals(BungeeSuite bungeeSuite) {
		super(bungeeSuite.portalsc);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin"))return;
		String message = ChatColor.DARK_AQUA+"Portals: "+ChatColor.WHITE;
		ArrayList<String> portals = null;
		try {
			portals = plugin.getUtilities().getPortals();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(String data:portals){
			message +=data+", ";
		}
	
	arg0.sendMessage(message);
	}

}
