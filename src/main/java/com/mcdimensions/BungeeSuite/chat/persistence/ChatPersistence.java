package com.mcdimensions.BungeeSuite.chat.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import net.buddat.bungeesuite.database.Database;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;

/**
 * Manages CRUD operations chat- and channel-related entities.
 */
public class ChatPersistence {
	private final BungeeSuite plugin;
	private final Database database;

	public ChatPersistence(BungeeSuite plugin, Database database) {
		this.plugin = plugin;
		this.database = database;
	}
	
	public void createChatSQLTables() throws SQLException {
		try (Connection connection = database.getConnection()) {
			if(!database.doesTableExist(connection, "BungeeChannels")){
				System.out.println("Table 'BungeeChannels' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeChannels (C_ID int NOT NULL AUTO_INCREMENT, ChannelName VARCHAR(50) NOT NULL UNIQUE, ChannelFormat VARCHAR(250) NOT NULL, isServerChannel BOOLEAN DEFAULT FALSE, Owner VARCHAR(50), CreatedDate DATE, PRIMARY KEY (C_ID))ENGINE=INNODB;");
				System.out.println("Table 'BungeeChannels' created!");
			}
			if(!database.doesTableExist(connection, "BungeePlayers")){
				System.out.println("Table 'BungeePlayers' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeePlayers (P_ID int NOT NULL AUTO_INCREMENT, PlayerName VARCHAR(50) NOT NULL UNIQUE, DisplayName VARCHAR(50), Current VARCHAR(50), ChannelsOwned int NOT NULL DEFAULT 0, LastOnline DATE NOT NULL, ChatSpy BOOLEAN DEFAULT FALSE, SendServer BOOLEAN DEFAULT true, SendPrefix BOOLEAN DEFAULT true,SendSuffix BOOLEAN DEFAULT true, Mute BOOLEAN DEFAULT FALSE,IPAddress VARCHAR(50), FOREIGN KEY(Current) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE, PRIMARY KEY (P_Id))ENGINE=INNODB;");
				System.out.println("Table 'ChatPlayers' created!");
			}
			if(!database.doesTableExist(connection, "BungeeInvites")){
				System.out.println("Table 'BungeeInvites' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeInvites (I_ID int NOT NULL AUTO_INCREMENT, PlayerName VARCHAR(50) NOT NULL, ChannelName VARCHAR(50) NOT NULL, FOREIGN KEY(PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE, FOREIGN KEY (ChannelName) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY (I_ID))ENGINE=INNODB;");
				System.out.println("Table 'BungeeInvites' created!");
			}
			if(!database.doesTableExist(connection, "BungeeMembers")){
				System.out.println("Table 'BungeeMembers' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeMembers (ChannelName VARCHAR(50) NOT NULL, PlayerName VARCHAR(50) NOT NULL, FOREIGN KEY (ChannelName) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE ON UPDATE CASCADE,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB");
				System.out.println("Table 'BungeeMembers' created!");
			} 
			if(!database.doesTableExist(connection, "BungeeIgnores")){
				System.out.println("Table 'BungeeIgnores' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeIgnores (PlayerName VARCHAR(50) NOT NULL, Ignoring VARCHAR(50) NOT NULL, FOREIGN KEY (Ignoring) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB");
				System.out.println("Table 'BungeeIgnores' created!");
			} 
		}
	}
	
	public void createStandardChannels() throws SQLException{
		try (Connection connection = database.getConnection()) {
			try (ResultSet rs = database.query(connection, "SELECT ChannelName FROM BungeeChannels WHERE ChannelName = 'Global';")) {
				if(!rs.next()){
					database.update(connection, "INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel) VALUES('Global', ?, TRUE);", plugin.defaultServerChannelFormat);
				}
			}
			for(String data: plugin.getProxy().getServers().keySet()){
				ResultSet rs = database.query(connection, "SELECT ChannelName FROM BungeeChannels WHERE ChannelName = '"+data+"';");
				if(!rs.next()){
					database.update(connection, "INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel) VALUES(?, ?, TRUE);", data, plugin.defaultServerChannelFormat);
				}
			}
		}
	}

	public boolean chatChannelExists(String name) throws SQLException{
		boolean check = plugin.chatChannels.containsKey(name);
		return check;
	}

	public void setCurrentChannel(String player, String channel){
		try {
			database.update("UPDATE BungeePlayers SET Current = ? WHERE PlayerName = ?", channel, player);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ChatPlayer loadChatPlayer(String player) throws SQLException {
		plugin.cl.cLog("Loading "+ player);
		try (Connection connection = database.getConnection();
				ResultSet players = database.query(connection, "SELECT * FROM BungeePlayers WHERE PlayerName = '"+player+"'");
				ResultSet members = database.query(connection, "SELECT * FROM BungeeMembers WHERE PlayerName = '"+player+"'");
				ResultSet ignored = database.query(connection, "SELECT Ignoring FROM BungeeIgnores WHERE PlayerName = '"+player+"'")) {
			ChatPlayer cp = null;
			while(players.next()){
				ChatChannel channel = plugin.chatChannels.get(players.getString("Current"));
				cp = new ChatPlayer(player, players.getString("DisplayName"), channel, players.getBoolean("ChatSpy"), players.getBoolean("SendServer"), players.getBoolean("Mute"), players.getInt("ChannelsOwned"), players.getBoolean("SendPrefix"), players.getBoolean("SendSuffix"));
			}
			while(members.next()){
				ChatChannel cc = plugin.getChannel(members.getString("ChannelName"));
				cc.onlineMember(cp);
				cp.addChannel(cc.getName());
			}
			while(ignored.next()){
				cp.addIgnore(ignored.getString("Ignoring"));
			}
			plugin.onlinePlayers.put(player, cp);
			if(cp.isChatSpying()){
				plugin.chatSpying.add(cp.getName());
			}
			return cp;
		}
	}

	public void loadChannels() throws SQLException {
		try (Connection connection = database.getConnection();
				ResultSet channels = database.query(connection, "SELECT * FROM BungeeChannels");
				ResultSet invites = database.query(connection, "SELECT * FROM BungeeInvites")) {
			while(channels.next()){
				ChatChannel newchan = new ChatChannel(channels.getString("ChannelName"), channels.getString("ChannelFormat"),channels.getString("Owner"), channels.getBoolean("isServerChannel"));
				plugin.chatChannels.put(newchan.getName(), newchan);
			}
			while(invites.next()){
				ChatChannel c = plugin.getChannel(invites.getString("ChannelName"));
				c.invitePlayer(invites.getString("PlayerName"));
			}
		}
	}
	
	public void createChannel(String channelName, String channelFormat, boolean server, String owner){
		try {
			database.update("INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel, Owner, CreatedDate) VALUES(?, ?, ?, ?, CURDATE());", channelName, channelFormat, server, owner);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ChatChannel newchan = new ChatChannel(channelName, channelFormat,owner, server);
		plugin.chatChannels.put(newchan.getName(), newchan);
		ChatPlayer cp = plugin.getChatPlayer(owner);
		cp.addChannelsOwned();
		cp.addChannel(newchan.getName());
		newchan.addMember(cp);
		cp.setCurrentChannel(newchan);
	}
	
	public void deleteChannel(String channel){
		for(String data:plugin.getChannel(channel).members){//set online
			ChatPlayer cp = plugin.getChatPlayer(data);
			ChatChannel cc = plugin.getChannel(cp.getCurrentServer());
			cp.setCurrentChannel(cc);
		}
		try (Connection connection = database.getConnection()) {
			//set offline to stop them being deleted
			database.update(connection, "UPDATE BungeePlayers SET Current=NULL WHERE Current = ?", channel);
			database.query(connection, "DELETE FROM BungeeChannels WHERE ChannelName = '"+channel+"'");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//may need to go through and change any players with it as their current.
		plugin.getChatPlayer(plugin.chatChannels.get(channel).getOwner()).subtractChannelsOwned();
		plugin.chatChannels.remove(channel);
	}
	
	public void renameChannel(String channelName, String name) {
		try {
			database.update("UPDATE BungeeChannels SET ChannelName= ? WHERE ChannelName = ?", name, channelName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reformatChannel(String channelName, String format) {
		try {
			database.update("UPDATE BungeeChannels SET ChannelFormat= ? WHERE ChannelName = ?", format, channelName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeMemberChannel(String player, String channel) {
		try {
			database.update("DELETE FROM BungeeMembers WHERE ChannelName = ? AND PlayerName = ?", channel, player);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addChannel(String name) {
		try {
			database.update("UPDATE BungeePlayers SET ChannelsOwned = ChannelsOwned + 1 WHERE PlayerName = ?", name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void subtractChannel(String name) {
		try {
			database.update("UPDATE BungeePlayers SET ChannelsOwned = ChannelsOwned - 1 WHERE PlayerName = ?", name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setChannelOwner(String channelName, String name) {
		try {
			database.update("UPDATE BungeeChannels SET Owner = ? WHERE ChannelName = ?", name, channelName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ChatChannel> getPlayersChannels(String name) throws SQLException {
		ArrayList<ChatChannel> channels = new ArrayList<ChatChannel>();
		try (Connection connection = database.getConnection();
				ResultSet res = database.query(connection, "SELECT ChannelName FROM BungeeMembers WHERE PlayerName = '"+name+"'")) {
			while(res.next()){
				channels.add(plugin.getChannel(res.getString("ChannelName")));
			}
		}
		if(plugin.globalToggleable && plugin.getProxy().getPlayer(name).hasPermission("BungeeSuite.global")){
			channels.add(plugin.getChannel("Global"));
		}
		return channels;
	}

	public void addMemberChannel(String player, String channel) {
		try {
			database.update("INSERT INTO BungeeMembers(ChannelName, PlayerName) VALUES (?, ?)", channel, player);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addChatSpy(ChatPlayer chatPlayer) {
		try {
			database.update("UPDATE BungeePlayers SET ChatSpy = TRUE WHERE PlayerName = ?", chatPlayer.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plugin.chatSpying.add(chatPlayer.getName());
	}

	public void removeChatSpy(ChatPlayer chatPlayer) {
		try {
			database.update("UPDATE BungeePlayers SET ChatSpy = FALSE WHERE PlayerName = ?", chatPlayer.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plugin.chatSpying.remove(chatPlayer.getName());
	}
	
	public HashSet<String> getIgnores(String player){
		HashSet<String> ignorelist =  new HashSet<String>();
		try (Connection connection = database.getConnection();
				ResultSet res = database.query(connection, "SELECT Ignoring FROM BungeeIgnores WHERE PlayerName = '"+player+"'")) {
			while(res.next()){
				ignorelist.add(res.getString("Ignoring"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ignorelist;
	}

	public void unignorePlayer(String name, String name2) {
		try {
			database.update("DELETE FROM BungeeIgnores WHERE PlayerName= ? AND Ignoring = ?", name, name2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		plugin.getChatPlayer(name).removeIgnore(name2);
	}

	public void ignorePlayer(String name, String name2) {
		try {
			database.update("INSERT INTO BungeeIgnores (PlayerName, Ignoring) VALUES (?, ?)", name, name2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		plugin.getChatPlayer(name).addIgnore(name2);	
	}

	public void mutePlayer(String name) {
		try {
			database.update("UPDATE BungeePlayers SET Mute =TRUE WHERE PlayerName = ?", name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void unMutePlayer(String name){
		try {
			database.update("UPDATE BungeePlayers SET Mute =FALSE WHERE PlayerName = ?", name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
