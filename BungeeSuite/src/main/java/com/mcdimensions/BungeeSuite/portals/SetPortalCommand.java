package com.mcdimensions.BungeeSuite.portals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SetPortalCommand extends Command {
	
	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.portal.create", "bungeesuite.portal.*",
		"bungeesuite.admin", "bungeesuite.*" };

	public SetPortalCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.setPortal);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 3) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.setPortal + " (Name) (type) (Dest)");
			return;
		}
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		String p = player.getName();
		String Name = arg1[0];
		String Type = arg1[1]; // server/warp
		String Dest = arg1[2]; // desination
		
		try {
			out.writeUTF("CreatePortal");
			out.writeUTF(p);
			out.writeUTF(Name);
			out.writeUTF(Type);
			out.writeUTF(Dest);
		} catch (IOException e) {
			e.printStackTrace();
		}

		player.getServer().sendData("BungeeSuiteMC", b.toByteArray());

	}

}
