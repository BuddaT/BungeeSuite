package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;
import com.mcdimensions.BungeeSuite.config.Config;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;

public class ChatListener implements Listener {
	private static final String BASE_CONFIG = Config.BASE + ".Chat";
	private static final String CONFIG_MAX_MESSAGE_COUNT = BASE_CONFIG + ".flood.maxcount";
	private static final String CONFIG_MESSAGE_COUNT_INTERVAL = BASE_CONFIG + ".flood.countinterval";
	private static final String CONFIG_FLOOD_KICK_MESSAGE = BASE_CONFIG + ".flood.kickmessage";
	private static final int DEFAULT_MAX_MESSAGE_COUNT = 5;
	private static final int DEFAULT_MESSAGE_COUNT_INTERVAL = 1000;
	private static final String DEFAULT_FLOOD_KICK_MESSAGE = "Chat flooding";
	private final BungeeSuite plugin;
	private final HashMap<String, LinkedList<Long>> messageTimes = new HashMap<>();
	private final Config config;
	private final int maxMessageCount;
	private final int messageCountInterval;
	private final String kickMessage;

	public ChatListener(BungeeSuite bungeeSuite) {
		this.plugin = bungeeSuite;
		this.config = bungeeSuite.config;
		maxMessageCount = config.getInt(CONFIG_MAX_MESSAGE_COUNT, DEFAULT_MAX_MESSAGE_COUNT);
		messageCountInterval = config.getInt(CONFIG_MESSAGE_COUNT_INTERVAL, DEFAULT_MESSAGE_COUNT_INTERVAL);
		kickMessage = config.getString(CONFIG_FLOOD_KICK_MESSAGE, DEFAULT_FLOOD_KICK_MESSAGE);
	}
	
	@Subscribe
	public void playerTalk(ChatEvent event) {
		if(!plugin.chatEnabled){
			return;
		}
		if (event.isCommand() || event.isCancelled()) {
			return;
		}
		if (!(event.getSender() instanceof ProxiedPlayer)) {
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String serverName = player.getServer().getInfo().getName();
		if (handleFlooding(event, player, serverName)) {
			return;
		}
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
	
	/**
	 * Determines whether or not a player is flooding and, if so, kicks them.
	 * 
	 * @param event
	 *            Chat event that triggered the flood check
	 * @param player
	 *            Player that sent the chat event
	 * @param serverName
	 *            Name of the server to which the player is connected
	 * @return True if a flood event was encountered and handled, otherwise
	 *         false.
	 */
	private boolean handleFlooding(ChatEvent event, ProxiedPlayer player, String serverName) {
		long currentTime = System.currentTimeMillis();
		String playerName = player.getName();
		LinkedList<Long> playerMessageTimes;
		if (messageTimes.containsKey(playerName)) {

			playerMessageTimes = messageTimes.get(playerName);
			long floodCheckMin = currentTime - messageCountInterval;
			// Remove messages outside the flood check period
			for (Long earliestMessage = playerMessageTimes.peekFirst();
					earliestMessage != null && earliestMessage.longValue() < floodCheckMin;
					earliestMessage = playerMessageTimes.peekFirst()) {
				playerMessageTimes.removeFirst();
			}
			if (playerMessageTimes.size() >= maxMessageCount) {
				player.disconnect(kickMessage);

				String kickBroadcast = plugin.DEFAULT_KICK_BROADCAST;
				kickBroadcast = kickBroadcast.replace("%sender", serverName);
				kickBroadcast = kickBroadcast.replace("%player", player.getDisplayName());
				kickBroadcast = kickBroadcast.replace("%message", kickMessage);
				plugin.getUtilities().sendBroadcast(kickBroadcast);
				event.setCancelled(true);
				return true;
			}
			playerMessageTimes.addLast(currentTime);
		} else {
			playerMessageTimes = new LinkedList<>();
			playerMessageTimes.add(currentTime);
			messageTimes.put(playerName, playerMessageTimes);
		}
		return false;
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
				plugin.getChatPersistence()
						.loadChatPlayer(event.getPlayer().getName());
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
