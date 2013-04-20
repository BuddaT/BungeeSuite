package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;
import java.util.Timer;
import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
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
		if(!plugin.chatEnabled){
			return;
		}
		if (event.isCommand() || event.isCancelled()) {
			return;
		}
		if (!(event.getSender() instanceof ProxiedPlayer))
			return;
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String serverName = player.getServer().getInfo().getName();
		ChatPlayer cp = plugin.getChatPlayer(player.getName());
		if (cp.getCurrentChannel().getName().equalsIgnoreCase(serverName)
				&& plugin.ignoreServers.contains(serverName)) {
			if (cp.isMuted() || plugin.allMuted) {
				event.setCancelled(true);
			}
			return;
		}
		event.setCancelled(true);
		if (plugin.allMuted && !player.hasPermission("BungeeSuite.admin")) {
			return;
		}
		if (!cp.isMuted()) {
			ChatChannel cur = cp.getCurrentChannel();
			if (cur.getName().equalsIgnoreCase("Global")) {
				cur.sendGlobalMessage(cp, event.getMessage());
				return;
			}
			cur.sendMessage(cp, event.getMessage());
			return;
		}
	}

	@Subscribe
	public void changeServer(ServerConnectedEvent event) throws SQLException {
		if(!plugin.chatEnabled)return;
		if (!plugin.onlinePlayers.containsKey(event.getPlayer().getName())) {
			String player = event.getPlayer().getName();
			String connection = event.getPlayer().getAddress().getAddress()
					.toString();
			if (!plugin.getUtilities().playerExists(player)) {
				plugin.getUtilities().createPlayer(player, connection);
			}
			if (!plugin.onlinePlayers.containsKey(event.getPlayer().getName())) {
				plugin.getUtilities()
						.getChatPlayer(event.getPlayer().getName());
			}
		}
		ChatPlayer cp = plugin.getChatPlayer(event.getPlayer().getName());
		ChatChannel cc = cp.getCurrentChannel();

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
			cp.setCurrentChannel(newServerchan, false);
		} else if (cc.equals(oldServerchan)) {// swap if current = old or null
			cp.setCurrentChannel(newServerchan, false);
		}
	}

}
