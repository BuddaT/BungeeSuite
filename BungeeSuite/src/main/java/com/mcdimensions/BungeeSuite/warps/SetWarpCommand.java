package com.mcdimensions.BungeeSuite.warps;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SetWarpCommand extends Command {

	private BungeeSuite plugin;

	public SetWarpCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.setWarp);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!sender.hasPermission("BungeeSuite.admin")) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.setWarp
					+ " (name) *(private)");
			return;
		}
		
		String name = arg1[0];
		// sends plugin message to BungeeSuiteBukkit to get player pos
		ProxiedPlayer player = (ProxiedPlayer) sender;
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		
		try {
			out.writeUTF("SetWarp");
			out.writeUTF(sender.getName());
			out.writeUTF(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (arg1.length > 1 && arg1[1].equalsIgnoreCase("private")) {
			try {
				out.writeBoolean(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				out.writeBoolean(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		player.getServer().sendData("BungeeSuiteMC", b.toByteArray());
	}

}
