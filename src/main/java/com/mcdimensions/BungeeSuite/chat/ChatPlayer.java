package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatPlayer {
	
	BungeeSuite plugin;
	
	private String name;
	private String displayName;
	private String currentServer;
	
	private ChatChannel currentChannel;
	private int channelsOwned;
	
	private boolean chatspying;
	
	private boolean sendingServer;
	private boolean sendingPrefix;
	private boolean sendingSuffix;
	
	private String prefix;
	private String suffix;
	
	private boolean mute;
	
	private String replyPlayer;
	private ArrayList<String> channels;
	private HashSet<String> ignores;

	public ChatPlayer(String name, String displayName, ChatChannel current, boolean chatspying, 
			boolean sendingServer, boolean mute, int channelsOwned, boolean sendingPrefix, boolean sendingSuffix) {
		this.name = name;
		this.displayName = displayName;
		this.currentChannel = current;
		this.chatspying = chatspying;
		this.sendingServer = sendingServer;
		this.sendingPrefix = sendingPrefix;
		this.sendingSuffix = sendingSuffix;
		this.mute = mute;
		this.channelsOwned = channelsOwned;
		
		plugin = (BungeeSuite) ProxyServer.getInstance().getPluginManager().getPlugin("BungeeSuite");
		
		channels = new ArrayList<String>();
		channels.add("Global");
		ignores = new HashSet<String>();
	}

	public void addChannel(String channel) {
		channels.add(channel);
	}

	public void removeChannel(String channel) {
		channels.remove(channel);
	}

	public ArrayList<String> getChannels() {
		return this.channels;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void updateDisplayName() {
		if (CommandUtil.hasPermission(this.getPlayer(), NicknameCommand.PERMISSION_NODES_COLORED))
			this.getPlayer().setDisplayName(colorSub(displayName));
		else
			this.getPlayer().setDisplayName(ChatColor.stripColor(colorSub(name)));
	}

	public void setDisplayName(String nick) throws SQLException {
		this.displayName = nick;
		
		plugin.getUtilities().setNickName(name, nick);
		updateDisplayName();
	}

	public ChatChannel getCurrentChannel() {
		return currentChannel;
	}

	public void setCurrentChannel(ChatChannel channel) {
		this.setCurrentChannel(channel, true);
	}
	
	public void setCurrentChannel(ChatChannel channel, boolean alert) {
		this.currentChannel = channel;
		plugin.getUtilities().setCurrentChannel(name, channel.getName());
		
		if (alert){
			String msg = plugin.PLAYER_SENDING_CHAT;
			msg = msg.replace("%channel", channel.getName());
			sendMessage(msg);
		}
	}

	public String getCurrentServer() {
		return currentServer;
	}

	public void setCurrentServer(String server) {
		currentServer = server;
	}

	public int getChannelsOwned() {
		return channelsOwned;
	}

	public boolean isChatSpying() {
		return chatspying;
	}

	public void toggleChatSpy() {
		if (isChatSpying()) {
			chatspying = false;
			plugin.getUtilities().removeChatSpy(this);
			return;
		} else {
			chatspying = true;
			plugin.getUtilities().addChatSpy(this);
			return;
		}
	}

	public boolean sendingPrefix() {
		return sendingPrefix;
	}

	public boolean toggleSendingPrefix() {
		if (sendingPrefix()) {
			sendingPrefix = false;
			return false;
		} else {
			sendingPrefix = true;
			return true;
		}
	}

	public boolean sendingSuffix() {
		return sendingSuffix;
	}

	public boolean toggleSendingSuffix() {
		if (sendingSuffix()) {
			sendingSuffix = false;
			return false;
		} else {
			sendingSuffix = true;
			return true;
		}
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		if (suffix != null) {
			return suffix;
		} else {
			return "";
		}
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		if (prefix != null) {
			return prefix;
		} else {
			return "";
		}
	}

	public boolean sendingServer() {
		return sendingServer;
	}

	public void toggleSendingServer() {
		if (sendingServer()) {
			sendingServer = false;
			plugin.getUtilities().playerRemoveSendServer(name);
			return;
		} else {
			sendingServer = true;
			plugin.getUtilities().playerSendServer(name);
			return;
		}
	}

	public boolean isMuted() {
		return mute;
	}

	public boolean toggleMute() {
		if (isMuted()) {
			mute = false;
			plugin.getUtilities().unMutePlayer(name);
			return false;
		} else {
			mute = true;
			plugin.getUtilities().mutePlayer(name);
			return true;
		}
	}

	public ProxiedPlayer getPlayer() {
		return ProxyServer.getInstance().getPlayer(name);
	}

	public String getReplyPlayer() {
		return replyPlayer;
	}

	public void sendMessage(String message) {
		if (this.getPlayer() == null) {
			System.out.println("PLAYER NULL: " + getName());
			return;
		}

		this.getPlayer().sendMessage(message);
	}

	public void sendPrivateMessage(String message, String replyPlayer) {
		this.replyPlayer = replyPlayer;
		
		this.getPlayer().sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + replyPlayer + ChatColor.GRAY + 
				"->" + ChatColor.AQUA + "me" + ChatColor.GRAY + "] " + message);
		
		for (String data : plugin.chatSpying) {
			ChatPlayer cp = plugin.getChatPlayer(data);
			
			if (!cp.equals(this)) {
				cp.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + replyPlayer + ChatColor.GRAY + 
						"->" + ChatColor.GOLD + this.name + ChatColor.GRAY + "] " + message);
			}
		}
		
		if (plugin.logChat) {
			plugin.cl.cLog("&e[" + replyPlayer + "->" + this.name + "] &f" + message);
		}
	}

	public boolean ignoringPlayer(String player) {
		return ignores.contains(player);
	}

	public void addIgnore(String string) {
		this.ignores.add(string);
	}

	public void removeIgnore(String string) {
		this.ignores.remove(string);
	}

	public HashSet<String> getIgnores() {
		return ignores;
	}
	
	public void addChannelsOwned() {
		this.channelsOwned++;
		plugin.getUtilities().addChannel(name);
	}

	public void subtractChannelsOwned() {
		this.channelsOwned--;
		plugin.getUtilities().subtractChannel(name);
	}

	public String colorSub(String str) {
		String output = "";
		output = str.replace("&0", ChatColor.BLACK.toString());
		output = output.replace("&1", ChatColor.DARK_BLUE.toString());
		output = output.replace("&2", ChatColor.DARK_GREEN.toString());
		output = output.replace("&3", ChatColor.DARK_AQUA.toString());
		output = output.replace("&4", ChatColor.DARK_RED.toString());
		output = output.replace("&5", ChatColor.DARK_PURPLE.toString());
		output = output.replace("&6", ChatColor.GOLD.toString());
		output = output.replace("&7", ChatColor.GRAY.toString());
		output = output.replace("&8", ChatColor.DARK_GRAY.toString());
		output = output.replace("&9", ChatColor.BLUE.toString());
		output = output.replace("&a", ChatColor.GREEN.toString());
		output = output.replace("&b", ChatColor.AQUA.toString());
		output = output.replace("&c", ChatColor.RED.toString());
		output = output.replace("&d", ChatColor.LIGHT_PURPLE.toString());
		output = output.replace("&e", ChatColor.YELLOW.toString());
		output = output.replace("&f", ChatColor.WHITE.toString());
		output = output.replace("&k", ChatColor.MAGIC.toString());
		output = output.replace("&l", ChatColor.BOLD.toString());
		output = output.replace("&n", ChatColor.UNDERLINE.toString());
		output = output.replace("&o", ChatColor.ITALIC.toString());
		output = output.replace("&m", ChatColor.STRIKETHROUGH.toString());
		return output;
	}
}
