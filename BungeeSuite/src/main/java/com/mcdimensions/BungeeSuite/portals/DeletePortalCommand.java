package com.mcdimensions.BungeeSuite.portals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;

public class DeletePortalCommand extends Command {

	private BungeeSuite plugin;

	public DeletePortalCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.delPortal);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.admin")) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			arg0.sendMessage(ChatColor.RED + "/" + plugin.delPortal + " (Name)");
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
				
				arg0.sendMessage(plugin.PORTAL_DELETE_CONFIRM);
			} else {
				arg0.sendMessage(plugin.PORTAL_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
