package com.mcdimensions.BungeeSuite.listeners;

import java.io.IOException;
import java.sql.SQLException;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

public class WarpListener implements Listener {

	private BungeeSuite plugin;
	public WarpListener(BungeeSuite bungeeSuite) {
		this.plugin = bungeeSuite;
	}
//	@Subscribe
//	public void playerConnect(ServerConnectedEvent event) throws IOException{
//		String playername = event.getPlayer().getName();
//		if(plugin.warped.containsKey(playername)){
//			plugin.warped.get(playername).warpPlayer(event.getPlayer());
//			plugin.warped.remove(playername);
//			return;
//		}
//		
//	}

}
