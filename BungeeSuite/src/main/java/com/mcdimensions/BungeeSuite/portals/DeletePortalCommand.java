package com.mcdimensions.BungeeSuite.portals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;

public class DeletePortalCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.portal.delete", "bungeesuite.portal.*",
		"bungeesuite.admin", "bungeesuite.*" };

	public DeletePortalCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.delPortal);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.delPortal + " (Name)");
			return;
		}
		
		try {
			if (plugin.getUtilities().portalExists(arg1[0])) {
				ServerInfo server = plugin.getProxy().getServerInfo(plugin.getUtilities().getPortalsServer(arg1[0]));
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				
				out.writeUTF("DeletePortal");
				out.writeUTF(arg1[0]);
				server.sendData("BungeeSuiteMC", b.toByteArray());
				
				sender.sendMessage(plugin.PORTAL_DELETE_CONFIRM);
			} else {
				sender.sendMessage(plugin.PORTAL_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
