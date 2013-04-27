package com.mcdimensions.BungeeSuite.warps;

import java.io.IOException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;
import com.mcdimensions.BungeeSuite.warps.persistence.WarpPersistence;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WarpSpawnCommand extends Command {
	private static final String WARP_SPAWN = "Spawn";

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.warp.spawn", "bungeesuite.warp.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public WarpSpawnCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.spawn);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (plugin.warpList.containsKey(WARP_SPAWN)) {
			try {
				plugin.warpList.get(WARP_SPAWN).warpPlayer((ProxiedPlayer) sender);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				WarpPersistence warpPersistence = plugin.getWarpPersistence();
				if (warpPersistence.warpExists(WARP_SPAWN)) {
					Warp warp = warpPersistence.loadWarp(WARP_SPAWN);
					warp.warpPlayer((ProxiedPlayer) sender);
				} else {
					sender.sendMessage(plugin.WARP_SPAWN_NOT_EXIST);
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
	}

}
