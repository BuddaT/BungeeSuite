package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;

public class ServerLoginLogout implements Listener {
	BungeeSuite plugin;
	
	public ServerLoginLogout(BungeeSuite bungeeSuite){
		this.plugin= bungeeSuite;
	}
	

	
	@Subscribe
	public void login(LoginEvent event) throws SQLException {
			String player = event.getConnection().getName();
			if(player==null){
				return;
			}
			String connection = event.getConnection().getAddress().getAddress().toString();
			if(!plugin.getUtilities().playerExists(player)){
				plugin.getUtilities().createPlayer(player, connection);
			}else{
				plugin.getUtilities().updateIP(player, connection);
			}
			if(plugin.chatEnabled && !plugin.onlinePlayers.containsKey(player)){
				plugin.getUtilities().getChatPlayer(player);
			}
		}
	
	@Subscribe
	public void logout(PlayerDisconnectEvent event) throws SQLException {
		if(!plugin.chatEnabled)return;
		String name = event.getPlayer().getName();
		if(name==null){
			for(String data:plugin.onlinePlayers.keySet()){
				if(plugin.getProxy().getPlayer(data)==null){
					plugin.onlinePlayers.remove(data);
					return;
				}
			}
		}
		plugin.getUtilities().updateLastSeen(name);
		if (plugin.onlinePlayers.containsKey(name)) {
			ChatPlayer cp = plugin.getChatPlayer(name);
			if (cp == null){
				return;
			}
			// remove from all channels
			for (String data : cp.getChannels()) {
				ChatChannel cc = plugin.getChannel(data);
				cc.offlineMember(cp);
			}
			plugin.getChannel(cp.getCurrentServer()).removeMember(cp.getName());
			if (plugin.chatSpying.contains(cp.getName())) {
				plugin.chatSpying.remove(cp.getName());
			}
			plugin.onlinePlayers.remove(event.getPlayer().getName());
		}
	}	
	@Subscribe
	public void postlog(final PostLoginEvent event) {
		if(!plugin.chatEnabled)return;
		Runnable UpdateDisplay = new Runnable(){
			public void run() {
			ChatPlayer cp = plugin.getChatPlayer(event.getPlayer().getName());
			cp.updateDisplayName();
			}
		};
		plugin.getProxy().getScheduler().schedule(plugin, UpdateDisplay, 2L, TimeUnit.SECONDS);
	}
}
