package com.mcdimensions.BungeeSuite.chat;

import java.util.HashSet;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatChannel {

	private String channelName;
	private String channelFormat;
	private String owner;
	private boolean serverChannel;
	public HashSet<String> members;
	private HashSet<String> invites;
	BungeeSuite plugin;

	public ChatChannel(String channelName, String channelFormat, String owner,
			boolean serverChannel) {
		this.channelName = channelName;
		this.channelFormat = channelFormat;
		this.serverChannel = serverChannel;
		this.owner = owner;
		this.members = new HashSet<String>();
		plugin = (BungeeSuite) ProxyServer.getInstance().getPluginManager()
				.getPlugin("BungeeSuite");
		invites =new HashSet<String>();
	}

	public String getFormat() {
		return channelFormat;
	}

	public String getOwner() {
		return owner;
	}
	
	public String getName() {
		return channelName;
	}

	public HashSet<String> getMembers() {
		return members;
	}

	public void addMember(ChatPlayer player) {
		members.add(player.getName());
		plugin.getUtilities().addMemberChannel(player.getName(), this.getName());
	}

	public void removeMember(String player) {
		members.remove(player);
		plugin.getUtilities().removeMemberChannel(player, this.getName());
		if(plugin.channelMessages){
			String jmessage = plugin.CHANNEL_PLAYER_LEAVE;
			jmessage = jmessage.replace("%player", player);
			jmessage = jmessage.replace("%channel", this.getName());
			sendChannelMessage(jmessage);
		}
	}
	public void offlineMember(ChatPlayer player){
		members.remove(player.getName());
	}
	public void onlineMember(ChatPlayer player){
		members.add(player.getName());
	}
	public boolean containsMember(String player) {
		return members.contains(player);
	}
	public void invitePlayer(String string){
		invites.add(string);
	}
	public void uninvitePlayer(String player){
		invites.remove(player);
		plugin.getUtilities().removeInvite(player, channelName);
	}
	public void acceptInvite(String player){
		ChatPlayer cp = plugin.getChatPlayer(player);
		this.addMember(cp);
		cp.addChannel(this.channelName);
		plugin.getUtilities().removeInvite(player, channelName);
		this.uninvitePlayer(player);
		if(plugin.channelMessages){
			String jmessage = plugin.CHANNEL_PLAYER_JOINED;
			jmessage = jmessage.replace("%player", player);
			jmessage = jmessage.replace("%channel", this.getName());
			sendChannelMessage(jmessage);
			cp.setCurrent(this);
		}
		String wmsg = plugin.CHANNEL_WELCOME;
		wmsg = wmsg.replace("%channel", this.channelName);
		cp.sendMessage(wmsg);
	}
	private void sendChannelMessage(String message) {
		for(String data: members){
			ChatPlayer cp= plugin.getChatPlayer(data);
			cp.sendMessage(message);
		}
		
	}
	public void rename(String name){
		plugin.chatChannels.remove(channelName);
		plugin.chatChannels.put(name, this);
		for(String data:members){
			ChatPlayer cp = plugin.getChatPlayer(data);
			cp.removeChannel(channelName);
			cp.addChannel(name);
		}
		this.channelName = name;
		plugin.getUtilities().renameChannel(channelName,name);
	}
	public void reformat(String format){
		this.channelFormat = format;
		plugin.ChannelConfig.setString("BungeeSuite.Chat.Channels."+getName(), format);
		plugin.ChannelConfig.save();
		plugin.getUtilities().reformatChannel(channelName,format);
	}
	public void kickPlayer(String player){
		ProxiedPlayer pp =plugin.getProxy().getPlayer(player);
		if(pp!=null){
			String kmsg = plugin.CHANNEL_KICK_PLAYER;
			kmsg = kmsg.replace("%channel", channelName);
			kmsg = kmsg.replace("%player", player);
			pp.sendMessage(kmsg);
			members.remove(plugin.getChatPlayer(player));
			return;
		}else{
		plugin.getUtilities().removeMemberChannel(player,channelName);
		}
	}

	public boolean isInvited(String player){
		return this.invites.contains(player);
	}
	public void sendMessage(ChatPlayer player, String message) {
		message = formatMessage(player, message);
		for (String data : members) {
			ChatPlayer cp = plugin.getChatPlayer(data);
			if(!cp.ignoringPlayer(player.getName())|| cp.getPlayer().hasPermission("BungeeSuite.mod")){
			cp.getPlayer().sendMessage(message);
			}
		}
		for(String data:plugin.chatSpying){
			if(!members.contains(data)){
				ChatPlayer spy = plugin.getChatPlayer(data);
				spy.sendMessage(message);
			}
		}
		if(plugin.logChat){
			plugin.cl.Log(message);
		}
	}

	public void sendGlobalMessage(ChatPlayer player, String message) {
		message = formatMessage(player, message);
		for (ChatPlayer data : plugin.OnlinePlayers.values()) {
			if(!data.ignoringPlayer(player.getName())|| data.getPlayer().hasPermission("BungeeSuite.mod")){
			data.sendMessage(message);
			}
		}
		if(plugin.logChat){
			plugin.cl.Log(message);
		}
	}

	public String formatMessage(ChatPlayer player, String message) {
		String output = "";
		String format = getFormat();
		ProxiedPlayer p = player.getPlayer();
		if(p.hasPermission("BungeeSuite.nick") || p.hasPermission("BungeeSuite.nickcolored") || p.hasPermission("BungeeSuite.mod")){
		output = format.replace("%player", player.getDisplayName());
		}else{
			output = format.replace("%player", player.getName());
		}
		String server = player.getCurrentServer();
		if (plugin.serverNames.containsKey(server)) {
			output = output.replace("%server",
					 plugin.serverNames.get(server));
		} else {
			output = output.replace("%server", server);
		}
		output = output.replace("%cname", channelName);
		output = output.replace("%title", "");//old formatting removal
		output = output.replace("%rank", "");
		// prefix
		if(plugin.useVault){
			output = output.replace("%prefix",player.getPrefix());
			output = output.replace("%suffix",player.getSuffix());
		}else{//permissions prefixes
			if (p.hasPermission("BungeeSuite.owner")) {
				output = output.replace("%prefix", plugin.prefix.get("owner"));
			} else if (p.hasPermission("BungeeSuite.admin")) {
				output = output.replace("%prefix", plugin.prefix.get("admin"));
			} else if (p.hasPermission("BungeeSuite.mod")) {
				output = output.replace("%prefix", plugin.prefix.get("mod"));
			} else if (p.hasPermission("BungeeSuite.tmod")) {
				output = output.replace("%prefix", plugin.prefix.get("tmod"));
			} else {
				output = output.replace("%prefix", "");
			}	
			output = output.replace("%suffix", "");
		}
		output = output.replace("%title", "");
		if (p.hasPermission("BungeeSuite.mod")
				|| p.hasPermission("BungeeSuite.colorChat")) {
			output = output.replace("%message", ChatColor.WHITE + message);
			output = colorSub(output);
		} else {
			if (plugin.formatChat) {
				message = format(message);
				 output = colorSub(output);
				output = output.replace("%message", message);
			} else {
				output = colorSub(output);
				output = output.replace("%message", message);
			}
		}
		return output;
	}

	private String format(String output) {
		String array[] = output.split("^\\b");
		String string = "";
		for (int i = 0; i < array.length; i++) {
			String newWord = "";
			if (array[i].length() > 2) {
				if (i == 0) {
					newWord += array[i].substring(0, 1).toUpperCase()
							+ array[i].substring(1, array[i].length())
									.toLowerCase();
				} else {
					newWord += array[i].toLowerCase();
				}
			} else if (array[i].equalsIgnoreCase("i")) {
				string += "I";
			} else if (array[i].charAt(0) == ':' || array[i].charAt(0) == ';'
					|| array[i].charAt(0) == 'D' || array[i].charAt(0) == 'X') {
				newWord += array[i];
			} else {
				if (i == 0) {
					newWord += array[i].substring(0, 1).toUpperCase()
							+ array[i].substring(1, array[i].length())
									.toLowerCase();
				} else {
					newWord += array[i].toLowerCase();
				}
			}
			for (String data : plugin.replacementWords.keySet()) {
				newWord = newWord.replace(data,
						colorSub(plugin.replacementWords.get(data)));

			}
			if (i != array.length) {
				string += newWord + " ";
			}

		}
		return string;
	}
	public boolean isServerChannel(){
		return serverChannel;
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

	public void setOwner(ChatPlayer player) {
		this.owner = player.getName();
		String cmsg = plugin.CHANNEL_CREATE_CONFIRM;
		cmsg = cmsg.replace("%channel", channelName);
		player.sendMessage(cmsg);
		plugin.getUtilities().setChannelOwner(channelName, player.getName());
	}

}
