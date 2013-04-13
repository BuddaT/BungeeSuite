package com.mcdimensions.BungeeSuite.warps;

import java.io.IOException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WarpSpawnCommand extends Command {

	BungeeSuite plugin;

	public WarpSpawnCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.spawn);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (plugin.warpList.containsKey("Spawn")) {
			try {
				plugin.warpList.get("Spawn").warpPlayer((ProxiedPlayer) sender);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (plugin.getUtilities().warpExists("Spawn")) {
					Warp warp = plugin.getUtilities().loadWarp("Spawn");
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
