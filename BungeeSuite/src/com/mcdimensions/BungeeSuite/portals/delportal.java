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

public class delportal extends Command {

	private BungeeSuite plugin;

	public delportal(BungeeSuite bungeeSuite) {
		super(bungeeSuite.delportal);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.admin"))
			return;
		if (arg1.length < 1) {
			arg0.sendMessage(ChatColor.RED + "/delportal (Name)");
			return;
		}
		try {
			if(plugin.getUtilities().portalExists(arg1[0])){
				ServerInfo server = plugin.getProxy().getServerInfo(plugin.getUtilities().getPortalsServer(arg1[0]));
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				out.writeUTF("DeletePortal");//sent to bukkit so it can remove from memory
				out.writeUTF(arg1[0]);
				server.sendData("BungeeSuiteMC", b.toByteArray());
				arg0.sendMessage(ChatColor.DARK_GREEN+"Portal deleted");
			}else{
				arg0.sendMessage(ChatColor.RED+"That portal does not exist");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
