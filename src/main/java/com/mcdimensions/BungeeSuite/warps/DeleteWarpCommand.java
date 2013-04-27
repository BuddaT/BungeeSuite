package com.mcdimensions.BungeeSuite.warps;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;
import com.mcdimensions.BungeeSuite.warps.persistence.WarpPersistence;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DeleteWarpCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.warp.delete",
		"bungeesuite.admin", "bungeesuite.*" };
	private final WarpPersistence warpStorage;

	public DeleteWarpCommand(BungeeSuite bungeeSuite, WarpPersistence warpStorage) {
		super(bungeeSuite.delWarp);
		this.plugin = bungeeSuite;
		this.warpStorage = warpStorage;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.delWarp + " (name)");
			return;
		}
		
		try {
			if (warpStorage.warpExists(arg1[0])) {
				plugin.getWarpPersistence().deleteWarp(arg1[0]);
				sender.sendMessage(plugin.WARP_DELETE_CONFIRM);
			} else {
				sender.sendMessage(plugin.WARP_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
