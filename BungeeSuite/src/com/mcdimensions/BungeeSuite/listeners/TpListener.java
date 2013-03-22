package com.mcdimensions.BungeeSuite.listeners;

import java.io.IOException;
import java.sql.SQLException;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

public class TpListener implements Listener {

	private BungeeSuite plugin;
	public TpListener(BungeeSuite bungeeSuite) {
		this.plugin = bungeeSuite;
	}
//	@Subscribe
//	public void playerConnect(ServerConnectedEvent event) throws IOException{
//		ProxiedPlayer player =event.getPlayer();
//		if(plugin.teleportsPending.containsKey(player)){
//			plugin.getUtilities().teleportToPlayer(player, plugin.teleportsPending.get(player));
//			plugin.teleportsPending.remove(player);
//			return;
//		}
//		
//	}

}
