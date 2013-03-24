package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;
import java.util.HashSet;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatPlayer {
	private String name;
	private ChatChannel current;
	private int channelsOwned;
	private String currentServer;
	private String displayName;
	private boolean chatspying;
	private boolean sendingServer;
	private boolean sendingPrefix;
	private boolean sendingSuffix;
	private String prefix;
	private String suffix;
	private boolean mute;
	BungeeSuite plugin;
	private String replyPlayer;
	private HashSet<String> channels;

	public ChatPlayer(String name, String displayName, ChatChannel current, boolean chatspying, boolean sendingServer, boolean mute, int channelsOwned, boolean sendingPrefix, boolean sendingSuffix) {
		this.name = name;
		this.displayName = displayName;
		this.current = current;
		this.chatspying = chatspying;
		this.sendingServer = sendingServer;
		this.sendingPrefix = sendingPrefix;
		this.sendingSuffix = sendingSuffix;
		this.mute = mute;
		this.channelsOwned = channelsOwned;
		plugin = (BungeeSuite) ProxyServer.getInstance().getPluginManager().getPlugin("BungeeSuite");
		channels = new HashSet<String>();
	}
	public void addChannel(String channel){
		channels.add(channel);
	}
	public void removeChannel(String channel){
		channels.remove(channel);
	}
	public HashSet<String> getChannels(){
		return this.channels;
	}
	public String getName() {
		return name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void updateDisplayName(){
		this.getPlayer().setDisplayName(displayName);
	}
	public void setDisplayName(String nick) throws SQLException{
		this.displayName = nick;
		plugin.getUtilities().setNickName(name, nick);
		this.getPlayer().setDisplayName(nick);
	}
	public ChatChannel getCurrent() {
		return current;
	}

	public void setCurrent(ChatChannel channel) {
		this.current = channel;
		plugin.getUtilities().setCurrentChannel(name,channel.getName());
		sendMessage(ChatColor.DARK_GREEN+"Sending messages in the channel "+ channel.getName());
	}

	public String getCurrentServer(){
		return currentServer;
	}
	public void setCurrentServer(String server){
		currentServer = server;
	}
	public int getChannelsOwned() {
		return channelsOwned;
	}

	public boolean isChatSpying() {
		return chatspying;
	}

	public void ChatSpy() {
		if (isChatSpying()) {
			chatspying = false;
			plugin.getUtilities().removeChatSpy(this);
			return;
		} else {
			chatspying = true;
			plugin.getUtilities().ChatSpy(this);
			return;
		}
	}

	public boolean sendingPrefix() {
		return sendingPrefix;
	}

	public boolean sendPrefix() {
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

	public boolean sendSuffix() {
		if (sendingSuffix()) {
			sendingSuffix = false;
			return false;
		} else {
			sendingSuffix = true;
			return true;
		}
	}
	public void setSuffix(String suffix){
		this.suffix=suffix;
	}
	public String getSuffix(){
		if(suffix!=null){
		return suffix;
		}else{
			return "";
		}
	}
	public void setPrefix(String prefix){
		this.prefix=prefix;
	}
	public String getPrefix(){
		if(prefix!=null){
		return prefix;
		}else{
			return "";
		}
	}
	public boolean sendingServer(){
		return sendingServer;
	}
	public void sendServer(){
		if(sendingServer()){
			sendingServer = false;
			plugin.getUtilities().playerRemoveSendServer(name);
			return;
		}else{
			sendingServer = true;
			plugin.getUtilities().playerSendServer(name);
			return;
		}
	}
	public boolean isMute() {
		return mute;
	}

	public boolean mute() {
		if (isMute()) {
			mute = false;
			plugin.getUtilities().unMutePlayer(name);
			return false;
		} else {
			mute = true;
			plugin.getUtilities().mutePlayer(name);
			return true;
		}
	}
	public ProxiedPlayer getPlayer(){
		return ProxyServer.getInstance().getPlayer(name);
	}
	
	public String getReplyPlayer(){
		return replyPlayer;
	}
	public void sendMessage(String message){
		this.getPlayer().sendMessage(message);
	}
	
	public void addChannelsOwned(){
		this.channelsOwned++;
		plugin.getUtilities().addChannel(name);
	}
	public void subtractChannelsOwned(){
		this.channelsOwned --;
		plugin.getUtilities().subtractChannel(name);
	}
	public void sendPrivate(String message, String name2) {
		this.replyPlayer = name2;
				this.getPlayer().sendMessage(ChatColor.GOLD+"["+name2+"->me]"+ChatColor.WHITE+message);
				for(String data:plugin.chatSpying){
					ChatPlayer cp = plugin.getChatPlayer(data);
					if(!cp.equals(this)){
						cp.sendMessage(ChatColor.YELLOW+"["+name2+"->"+this.name+"] "+ChatColor.WHITE+message);
					}
		}
				if(plugin.logChat){
					plugin.cl.Color("&e["+name2+"->"+this.name+"] &f"+message);
				}
		
	}
	public void setCurrentSilent(ChatChannel channel) {
		this.current = channel;
		plugin.getUtilities().setCurrentChannel(name,channel.getName());
		
	}

}