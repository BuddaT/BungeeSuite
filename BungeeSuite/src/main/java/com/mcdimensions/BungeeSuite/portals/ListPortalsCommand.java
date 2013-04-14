package com.mcdimensions.BungeeSuite.portals;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ListPortalsCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.portal.delete", "bungeesuite.portal.*",
		"bungeesuite.admin", "bungeesuite.*" };

	public ListPortalsCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.listPortals, null, bungeeSuite.portalsc);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		String message = ChatColor.DARK_AQUA + "Portals: " + ChatColor.WHITE;
		
		try {
			for (String data : plugin.getUtilities().getPortals()) {
				message += data + ", ";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sender.sendMessage(message);
	}

}
