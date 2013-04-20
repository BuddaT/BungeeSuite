package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;

public class LoginMessages implements Listener {
	
	private static final String SUBCHANNEL_LOGINOUT = "login_logout";
	
	BungeeSuite plugin;

	public LoginMessages(BungeeSuite bungeeSuite) {
		this.plugin = bungeeSuite;
	}

	@Subscribe
	public void login(ChatEvent event) throws SQLException {
		if (event.getSender() instanceof ProxiedPlayer)
			return;
		
		String message = event.getMessage();
		String connected = message;
		
		if (connected.endsWith("joined the game.")) {
			event.setCancelled(true);
		} else {
			return;
		}
	}

	@Subscribe
	public void logout(ChatEvent event) throws SQLException {
		if (event.getSender() instanceof ProxiedPlayer)
			return;
		
		String message = event.getMessage();
		String connected = message;
		
		if (connected.endsWith("left the game.")) {
			event.setCancelled(true);
		} else {
			return;
		}
	}

	@Subscribe
	public void login(PostLoginEvent event) throws SQLException {
		ChatChannel.dispatchMessage("[" + ChatColor.DARK_GREEN + event.getPlayer().getName()
				+ ChatColor.WHITE + " connected]", SUBCHANNEL_LOGINOUT);
		
		for (ProxiedPlayer data : ProxyServer.getInstance().getPlayers()) {
			data.sendMessage(ChatColor.YELLOW + event.getPlayer().getName()
					+ " joined the server.");
		}
	}

	@Subscribe
	public void logout(PlayerDisconnectEvent event) throws SQLException {
		ChatChannel.dispatchMessage("[" + ChatColor.DARK_GREEN + event.getPlayer().getName()
				+ ChatColor.WHITE + " disconnected]", SUBCHANNEL_LOGINOUT);
		
		for (ProxiedPlayer data : ProxyServer.getInstance().getPlayers()) {
			data.sendMessage(ChatColor.YELLOW + event.getPlayer().getName()
					+ " left the server.");
		}
	}
	
}
