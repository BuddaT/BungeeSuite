package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;

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
				plugin.getUtilities().getChatPlayer(player);
		}
	
	@Subscribe
	public void logout(PlayerDisconnectEvent event) throws SQLException {
		if(event.getPlayer().getName()==null)return;
		plugin.getUtilities().updateLastSeen(event.getPlayer().getName());
		if (plugin.OnlinePlayers.containsKey(event.getPlayer().getName())) {
			ChatPlayer cp = plugin.getChatPlayer(event.getPlayer().getName());
			if (cp == null)
				return;
			// remove from all channels
			for (String data : cp.getChannels()) {
				ChatChannel cc = plugin.getChannel(data);
				cc.offlineMember(cp);
			}
			plugin.getChannel(cp.getCurrentServer()).removeMember(cp.getName());
			if (plugin.chatSpying.contains(cp.getName())) {
				plugin.chatSpying.remove(cp.getName());
			}
			plugin.OnlinePlayers.remove(event.getPlayer().getName());
		}
	}
	
	
}
