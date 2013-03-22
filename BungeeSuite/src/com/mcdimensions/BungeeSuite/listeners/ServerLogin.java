package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

public class ServerLogin implements Listener {
	BungeeSuite plugin;
	
	public ServerLogin(BungeeSuite bungeeSuite){
		this.plugin= bungeeSuite;
	}
	

	
	@Subscribe
	public void login(LoginEvent event) throws SQLException {
			String player = event.getConnection().getName();
			String connection = event.getConnection().getAddress().getAddress().toString();
			if(!plugin.getUtilities().playerExists(player)){
				plugin.getUtilities().createPlayer(player, connection);
			}else{
				plugin.getUtilities().updateIP(player, connection);
			}
		}
	
	@Subscribe
	public void logout(PlayerDisconnectEvent event) throws SQLException {
		plugin.getUtilities().updateLastSeen(event.getPlayer().getName());
	}
	
}
