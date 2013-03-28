package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

public class ChatListener implements Listener {
	BungeeSuite plugin;
	Timer timer;

	public ChatListener(BungeeSuite bungeeSuite) {
		this.plugin = bungeeSuite;
	}

	@Subscribe
	public void playerTalk(ChatEvent event) {
		if (event.isCommand() || event.isCancelled()) {
			return;
		}
		if (!(event.getSender() instanceof ProxiedPlayer))return;
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String serverName = player.getServer().getInfo().getName();
		ChatPlayer cp = plugin.getChatPlayer(player.getName());
		if(cp.getCurrent().getName().equalsIgnoreCase(serverName)&& plugin.ignoreServers.contains(serverName)){
			if(!cp.isMute() && !plugin.allMuted){
				event.setCancelled(true);
			}
			return;	
		}
		event.setCancelled(true);
		if (plugin.allMuted && !player.hasPermission("BungeeSuite.admin")){
			return;
		}
		if (!cp.isMute()) {
			ChatChannel cur = cp.getCurrent();
			if (cur.getName().equalsIgnoreCase("Global")) {
				cur.sendGlobalMessage(cp, event.getMessage());
				return;
			}
			cur.sendMessage(cp, event.getMessage());
			return;
		}
	}
	@Subscribe
	public void changeServer(ServerConnectedEvent event) {
		ChatPlayer cp = null;
		cp = plugin.getChatPlayer(event.getPlayer().getName());
		if (cp == null) {
			String player = event.getPlayer().getPendingConnection().getName();
			String connection = event.getPlayer().getPendingConnection()
					.getAddress().getAddress().toString();
			try {
				if (!plugin.getUtilities().playerExists(player)) {
					plugin.getUtilities().createPlayer(player, connection);
				} else {
					plugin.getUtilities().updateIP(player, connection);
					plugin.getUtilities().getChatPlayer(player);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			cp = plugin.getChatPlayer(event.getPlayer().getName());

		}
		ChatChannel cc = cp.getCurrent();

		ChatChannel oldServerchan = plugin.getChannel(cp.getCurrentServer());
		ChatChannel newServerchan = plugin.getChannel(event.getServer()
				.getInfo().getName());
		if (oldServerchan != null) {
			oldServerchan.removeMember(cp.getName());// remove player from old
		}
		newServerchan.addMember(cp);// add to new
		cp.addChannel(newServerchan.getName());
		cp.setCurrentServer(event.getServer().getInfo().getName());
		if (cc == null) {
			cp.setCurrentSilent(newServerchan);
		} else if (cc.equals(oldServerchan)) {// swap if current = old or null
			cp.setCurrentSilent(newServerchan);
		}

	}

}
