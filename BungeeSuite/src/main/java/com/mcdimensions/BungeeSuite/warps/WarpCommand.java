package com.mcdimensions.BungeeSuite.warps;

import java.io.IOException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WarpCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.warp.warp", "bungeesuite.warp.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public WarpCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.warp);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.warp + " (name)");
			return;
		} else if (plugin.warpList.containsKey(arg1[0])) {
			try {
				plugin.warpList.get(arg1[0]).warpPlayer((ProxiedPlayer) sender);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (plugin.getUtilities().warpExists(arg1[0])) {
					Warp warp = plugin.getUtilities().loadWarp(arg1[0]);
					warp.warpPlayer((ProxiedPlayer) sender);
				} else {
					sender.sendMessage(plugin.WARP_NOT_EXIST);
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
