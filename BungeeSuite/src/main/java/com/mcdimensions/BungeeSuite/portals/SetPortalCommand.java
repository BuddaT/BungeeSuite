package com.mcdimensions.BungeeSuite.portals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SetPortalCommand extends Command {
	
	BungeeSuite plugin;

	public SetPortalCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.setPortal);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.admin")) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		if (arg1.length < 3) {
			arg0.sendMessage(ChatColor.RED + "/" + plugin.setPortal
					+ " (Name) (type) (Dest)");
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) arg0;
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		String p = player.getName();
		String Name = arg1[0];
		String Type = arg1[1]; // server/warp
		String Dest = arg1[2]; // desination
		try {
			out.writeUTF("CreatePortal");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			out.writeUTF(p);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			out.writeUTF(Name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.writeUTF(Type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.writeUTF(Dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		player.getServer().sendData("BungeeSuiteMC", b.toByteArray());

	}

}
