package com.mcdimensions.BungeeSuite.warps;

import java.io.IOException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;


import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class spawn extends Command {

	BungeeSuite plugin;

	public spawn(BungeeSuite bungeeSuite) {
			super(bungeeSuite.spawn);
			this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(plugin.warpList.containsKey("Spawn")){
			try {
				plugin.warpList.get("Spawn").warpPlayer((ProxiedPlayer) arg0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			try {
				if(plugin.getUtilities().warpExists("Spawn")){
					Warp warp  = plugin.getUtilities().loadWarp("Spawn");
					warp.warpPlayer((ProxiedPlayer) arg0);
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
	}

}
