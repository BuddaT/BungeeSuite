package com.mcdimensions.BungeeSuite.utilities;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import net.buddat.bungeesuite.database.Database;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;



import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;
import com.mcdimensions.BungeeSuite.teleports.TPCommand;
import com.mcdimensions.BungeeSuite.warps.Warp;
import com.mcdimensions.BungeeSuite.warps.WarpLocation;


public class Utilities {
	Database sql;
	BungeeSuite plugin;
	
	public Utilities(BungeeSuite bpk){
		plugin = bpk;
		sql = plugin.getDatabase();
	}
	
	public void createServer(String name) throws SQLException{
		sql.updateQuery("INSERT INTO BungeeServers(ServerName, Online) VALUES('"+name+"', FALSE)");
	}
	public void CreateWarpSQLTables() throws SQLException {
		try (Connection connection = sql.getConnection()) {
			if(!sql.doesTableExist("BungeeWarps", connection)){
				System.out.println("Table 'BungeeWarps' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeWarps (W_ID int NOT NULL AUTO_INCREMENT,Name VARCHAR(50) NOT NULL UNIQUE, Server VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X double NOT NULL,  Y double NOT NULL, Z double NOT NULL, Yaw float NOT NULL, Pitch float NOT NULL, Visible boolean NOT NULL, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (W_ID))ENGINE=INNODB;", connection);
				System.out.println("Table 'BungeeWarps' created!");
			}
		}
	}
	public boolean serverExists(String name) throws SQLException{
		return sql.existenceQuery("SELECT ServerName FROM BungeeServers WHERE ServerName = '"+name+"'");
	}
	
	public HashMap<String,Warp> loadWarps() throws SQLException {
		HashMap<String,Warp> warps = new HashMap<String,Warp>();
		try (Connection connection = sql.getConnection()) {
			if(!sql.doesTableExist("BungeeWarps", connection)){
			    System.out.println("Table 'BungeeWarps' does not exist! Creating table...");
			    sql.sqlQuery("CREATE TABLE BungeeWarps (W_ID int NOT NULL AUTO_INCREMENT,Name VARCHAR(50) NOT NULL UNIQUE, Server VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X double NOT NULL,  Y double NOT NULL, Z double NOT NULL, Yaw float NOT NULL, Pitch float NOT NULL, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (W_ID))ENGINE=INNODB;", connection);
			    System.out.println("Table 'BungeeWarps' created!");
			}
			try (ResultSet res = sql.sqlQuery("SELECT * FROM BungeeWarps", connection)) {
				while(res.next()){
					String name = res.getString("Name");
					warps.put(name, new Warp(name, new WarpLocation(res.getString("Server"), res.getString("World"), res.getDouble("X"), res.getDouble("Y"), res.getDouble("Z"), res.getFloat("Yaw"), res.getFloat("Pitch")),res.getBoolean("Visable")));
				}
			}
			return warps;
		}
	}

	public ProxiedPlayer getClosestPlayer(String player) {
		String name = player.toLowerCase();
		for (ProxiedPlayer data : plugin.getProxy().getPlayers())
			if (data.getName().toLowerCase().startsWith(name)) {
				return data;
			}
		return null;
	}
	
	public void createSQLServerTable() throws SQLException {
		try (Connection connection = sql.getConnection()) {
			if (!sql.doesTableExist("BungeeServers", connection)) {
				System.out
						.println("Table 'BungeeServers' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeServers (S_ID int NOT NULL AUTO_INCREMENT, ServerName VARCHAR(50) NOT NULL UNIQUE, PlayersOnline int DEFAULT 0, MaxPlayers int,MOTD VARCHAR(60), Online BOOLEAN DEFAULT FALSE, PRIMARY KEY (S_Id))ENGINE=INNODB;", connection);
				System.out.println("Table 'BungeeServers' created!");
			}
		}
	}
	public void CreateSignSQLTables() throws SQLException {
		try (Connection connection = sql.getConnection()) {
			if(!sql.doesTableExist("BungeeSignType", connection)){
				System.out.println("Table 'BungeeSignType' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeSignType (T_ID int NOT NULL AUTO_INCREMENT, Type VARCHAR(50) NOT NULL UNIQUE, PRIMARY KEY (T_Id))ENGINE=INNODB;", connection);
				sql.sqlQuery("INSERT INTO BungeeSignType (Type) VALUES('PlayerList');", connection);
				sql.sqlQuery("INSERT INTO BungeeSignType (Type) VALUES('MOTD');", connection);
				System.out.println("Table 'BungeeSignType' created!");
			}
			try (ResultSet rs = sql.sqlQuery("SELECT Type FROM BungeeSignType WHERE Type ='Portal'", connection)) {
				if(!rs.next()){ 
					sql.sqlQuery("INSERT INTO BungeeSignType (Type) VALUES('Portal');", connection);
				}
			}
			if(!sql.doesTableExist("BungeeSignLocations", connection)){
				System.out.println("Table 'BungeeSignLocations' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeSignLocations (L_ID int NOT NULL AUTO_INCREMENT, Type VARCHAR(50) NOT NULL, Server VARCHAR(50) NOT NULL, TargetServer VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X int NOT NULL,  Y int NOT NULL, Z int NOT NULL, FOREIGN KEY(Type) REFERENCES BungeeSignType(Type) ON DELETE CASCADE, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE,  FOREIGN KEY(TargetServer) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (L_ID))ENGINE=INNODB;", connection);
				System.out.println("Table 'BungeeSignLocations' created!");
			}
			if(!sql.doesTableExist("BungeeSignFormats", connection)){
				System.out.println("Table 'BungeeSignFormats' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeSignFormats (F_ID Int NOT NULL AUTO_INCREMENT,ColoredMOTD Boolean NOT NULL, MOTDOnline VARCHAR(50) NOT NULL, MOTDOffline VARCHAR(50) NOT NULL, PlayerCountOnline VARCHAR(50) NOT NULL,  PlayerCountOnlineClick VARCHAR(50) NOT NULL, PlayerCountOffline VARCHAR(50) NOT NULL, PlayerCountOfflineClick VARCHAR(50) NOT NULL, PortalFormatOnline VARCHAR(50) NOT NULL, PortalFormatOffline VARCHAR(50) NOT NULL, PortalFormatOfflineClick VARCHAR(50) NOT NULL,PRIMARY KEY (F_ID))ENGINE=INNODB;", connection);
				System.out.println("Table 'BungeeSignFormats' created!");
			}
		}
	}
	public void CreatePortalSQLTables() throws SQLException {
		try (Connection connection = sql.getConnection()) {
			if(!sql.doesTableExist("BungeePortals", connection)){
				System.out.println("Table 'BungeePortals' does not exist! Creating table...");
			    sql.sqlQuery("CREATE TABLE BungeePortals (P_ID int NOT NULL AUTO_INCREMENT,Name VARCHAR(50) NOT NULL, Server VARCHAR(50) NOT NULL, ToServer VARCHAR(50) NOT NULL, Warp VARCHAR(50), World VARCHAR(50) NOT NULL, XMax int NOT NULL, XMin int NOT NULL, YMax int NOT NULL, Ymin int NOT NULL, ZMax int NOT NULL, Zmin int NOT NULL, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, FOREIGN KEY(ToServer) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE,  FOREIGN KEY(Warp) REFERENCES BungeeWarps(Name) ON DELETE CASCADE, PRIMARY KEY (P_ID))ENGINE=INNODB;", connection);
			    System.out.println("Table 'BungeePortals' created!");
			}
		}
	}
	public boolean warpExists(String name) throws SQLException{
		return sql.existenceQuery("SELECT Name FROM BungeeWarps WHERE Name = '"+name+"'");
	}

	public Warp loadWarp(String string) throws SQLException {
		try (Connection connection = sql.getConnection();
				ResultSet res = sql.sqlQuery("SELECT * FROM BungeeWarps WHERE Name='"+string+"'", connection)) {
			Warp warp = null;
			while(res.next()){
				String name = res.getString("Name");
				WarpLocation wl = new WarpLocation(res.getString("Server"), res.getString("World"), res.getDouble("X"), res.getDouble("Y"), res.getDouble("Z"), res.getFloat("Yaw"), res.getFloat("Pitch"));
				warp = new Warp(name, wl, res.getBoolean("Visible"));
				plugin.warpList.put(name, warp);
			}
			return warp;
		}
	}

	public String[] getWarpList(CommandSender arg0) throws SQLException {
		
		String[] list= new String[2];
		list[0]=ChatColor.AQUA+"Warp List: ";
		list[1]=ChatColor.BLUE+"Private warps: ";
		try (Connection connection = sql.getConnection();
				ResultSet res = sql.sqlQuery("SELECT Name,Visible FROM BungeeWarps", connection)) {
			while(res.next()){
				String name = res.getString("Name");
				boolean visible = res.getBoolean("Visible");
				if(visible){
					list[0]+=name+ ", ";
				}else{
					list[1]+=name+ ", ";
				}
			}
			return list;
		}
	}

	public void teleportToPlayer(ProxiedPlayer originalPlayer,
			ProxiedPlayer targetPlayer) {
		if (!originalPlayer.getServer().getInfo().equals(targetPlayer.getServer().getInfo())) {
			plugin.teleportsPending.put(originalPlayer, targetPlayer);
			originalPlayer.connect(targetPlayer.getServer().getInfo());
		} else {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream o = new DataOutputStream(b);

			try {
				o.writeUTF("Teleport");
				o.writeUTF(originalPlayer.getName());
				o.writeUTF(targetPlayer.getName());// target player
			} catch (IOException e) {
				// Can never happen
			}
			
			originalPlayer.getServer().sendData("BungeeSuiteMC",
					b.toByteArray());
			
			String tmsg = plugin.TELEPORTED_PLAYER_TO_TARGET;
			tmsg = tmsg.replace("%player", targetPlayer.getName());
			tmsg = tmsg.replace("%sender", originalPlayer.getName());
			originalPlayer.sendMessage(tmsg);
			
			if (CommandUtil.hasPermission(targetPlayer, TPCommand.PERMISSION_NODES)) {
				String pmsg = plugin.PLAYER_TELEPORTED_TO;
				pmsg = pmsg.replace("%player", targetPlayer.getName());
				pmsg = pmsg.replace("%sender", originalPlayer.getName());
				targetPlayer.sendMessage(pmsg);
			}
		}
	}

	public void sendTpRequest(ProxiedPlayer player, ProxiedPlayer targetPlayer) {
		plugin.tpaList.put(targetPlayer, player);
		String tmsg = plugin.TELEPORT_REQUEST_TO;
		tmsg = tmsg.replace("%player", player.getDisplayName());
		targetPlayer.sendMessage(tmsg);	
		return;
	}

	public void sendTpHereRequest(ProxiedPlayer player, ProxiedPlayer targetPlayer) {
		plugin.tpHereList.put(player, targetPlayer);
		String tmsg = plugin.TELEPORT_REQUEST_HERE;
		tmsg = tmsg.replace("%player", targetPlayer.getDisplayName());
		player.sendMessage(tmsg);	
		return;
	}

	public void CreateChatSQLTables() throws SQLException {
		try (Connection connection = sql.getConnection()) {
			if(!sql.doesTableExist("BungeeChannels", connection)){
				System.out.println("Table 'BungeeChannels' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeChannels (C_ID int NOT NULL AUTO_INCREMENT, ChannelName VARCHAR(50) NOT NULL UNIQUE, ChannelFormat VARCHAR(250) NOT NULL, isServerChannel BOOLEAN DEFAULT FALSE, Owner VARCHAR(50), CreatedDate DATE, PRIMARY KEY (C_ID))ENGINE=INNODB;", connection);
				System.out.println("Table 'BungeeChannels' created!");
			}
			if(!sql.doesTableExist("BungeePlayers", connection)){
				System.out.println("Table 'BungeePlayers' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeePlayers (P_ID int NOT NULL AUTO_INCREMENT, PlayerName VARCHAR(50) NOT NULL UNIQUE, DisplayName VARCHAR(50), Current VARCHAR(50), ChannelsOwned int NOT NULL DEFAULT 0, LastOnline DATE NOT NULL, ChatSpy BOOLEAN DEFAULT FALSE, SendServer BOOLEAN DEFAULT true, SendPrefix BOOLEAN DEFAULT true,SendSuffix BOOLEAN DEFAULT true, Mute BOOLEAN DEFAULT FALSE,IPAddress VARCHAR(50), FOREIGN KEY(Current) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE, PRIMARY KEY (P_Id))ENGINE=INNODB;", connection);
				System.out.println("Table 'ChatPlayers' created!");
			}
			if(!sql.doesTableExist("BungeeInvites", connection)){
				System.out.println("Table 'BungeeInvites' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeInvites (I_ID int NOT NULL AUTO_INCREMENT, PlayerName VARCHAR(50) NOT NULL, ChannelName VARCHAR(50) NOT NULL, FOREIGN KEY(PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE, FOREIGN KEY (ChannelName) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY (I_ID))ENGINE=INNODB;", connection);
				System.out.println("Table 'BungeeInvites' created!");
			}
			if(!sql.doesTableExist("BungeeMembers", connection)){
				System.out.println("Table 'BungeeMembers' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeMembers (ChannelName VARCHAR(50) NOT NULL, PlayerName VARCHAR(50) NOT NULL, FOREIGN KEY (ChannelName) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE ON UPDATE CASCADE,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB", connection);
				System.out.println("Table 'BungeeMembers' created!");
			} 
			if(!sql.doesTableExist("BungeeIgnores", connection)){
				System.out.println("Table 'BungeeIgnores' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeIgnores (PlayerName VARCHAR(50) NOT NULL, Ignoring VARCHAR(50) NOT NULL, FOREIGN KEY (Ignoring) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB", connection);
				System.out.println("Table 'BungeeIgnores' created!");
			} 
		}
	}
	
	public void createStandardChannels() throws SQLException{
		try (Connection connection = sql.getConnection()) {
			try (ResultSet rs = sql.sqlQuery("SELECT ChannelName FROM BungeeChannels WHERE ChannelName = 'Global';", connection)) {
				if(!rs.next()){
					sql.updateQuery("INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel) VALUES('Global', '"+plugin.defaultServerChannelFormat+"', TRUE);", connection);
				}
			}
			for(String data: plugin.getProxy().getServers().keySet()){
				ResultSet rs = sql.sqlQuery("SELECT ChannelName FROM BungeeChannels WHERE ChannelName = '"+data+"';", connection);
				if(!rs.next()){
					sql.updateQuery("INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel) VALUES('"+data+"', '"+plugin.defaultServerChannelFormat+"', TRUE);", connection);
				}
			}
		}
	}
	public void createPlayer(String player, String connection) throws SQLException {
		if(plugin.globalDefault){
			sql.updateQuery("INSERT INTO BungeePlayers (PlayerName, DisplayName, Current, LastOnline, IPAddress) VALUES ('"+player+"','"+player+"','Global', CURDATE(), '"+connection+"')");
		}else{
			sql.updateQuery("INSERT INTO BungeePlayers (PlayerName, DisplayName, Current, LastOnline, IPAddress) VALUES ('"+player+"','"+player+"',NULL, CURDATE(), '"+connection+"')");
		}
		if(plugin.chatEnabled){
			getChatPlayer(player);
		}
	}
	public void setCurrentChannel(String player, String channel){
		try {
			sql.updateQuery("UPDATE BungeePlayers SET Current = '"+channel+"' WHERE PlayerName = '"+player+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateIP(String name, String connection) throws SQLException{
		sql.updateQuery("UPDATE BungeePlayers SET IPAddress = '"+connection+"' WHERE PlayerName = '"+name+"'");
	}
	
	public void updateLastSeen(String player) throws SQLException{
		sql.updateQuery("UPDATE BungeePlayers SET LastOnline=CURDATE() WHERE PlayerName = '"+player+"'");
	}
	public boolean chatChannelExists(String name) throws SQLException{
		boolean check = plugin.chatChannels.containsKey(name);
		return check;
	}
	public ChatChannel getChatChannel(String name){
		return plugin.chatChannels.get(name);
	}
	public boolean playerExists(String player) throws SQLException {
		return sql.existenceQuery("SELECT PlayerName FROM BungeePlayers Where PlayerName = '"+player+"'");
	}
	public String getIP(String player) throws SQLException{
		try (Connection connection = sql.getConnection();
				ResultSet res = sql.sqlQuery("SELECT IPAddress FROM BungeePlayers WHERE PlayerName = '"+player+"'", connection)) {
			String ip = null;
			while(res.next()){
				ip = res.getString("IPAddress");
			}
			return ip;
		}
	}
	
	public ChatPlayer getChatPlayer(String player) throws SQLException {
		plugin.cl.cLog("Loading "+ player);
		try (Connection connection = sql.getConnection();
				ResultSet players = sql.sqlQuery("SELECT * FROM BungeePlayers WHERE PlayerName = '"+player+"'", connection);
				ResultSet members = sql.sqlQuery("SELECT * FROM BungeeMembers WHERE PlayerName = '"+player+"'", connection);
				ResultSet ignored = sql.sqlQuery("SELECT Ignoring FROM BungeeIgnores WHERE PlayerName = '"+player+"'", connection)) {
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
			return null;
		}
	}

	public void loadChannels() throws SQLException {
		try (Connection connection = sql.getConnection();
				ResultSet channels = sql.sqlQuery("SELECT * FROM BungeeChannels", connection);
				ResultSet invites = sql.sqlQuery("SELECT * FROM BungeeInvites", connection)) {
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

	public void setNickName(String name, String string) throws SQLException {
		sql.updateQuery("UPDATE BungeePlayers SET DisplayName='"+string+"' WHERE PlayerName = '"+name+"'");
	}

	public void createBanningSQLTables() throws SQLException {
		try (Connection connection = sql.getConnection()) {
			if(!sql.doesTableExist("BungeeBans", connection)){
				System.out.println("Table 'BungeeBans' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeBans (PlayerName VARCHAR(50) NOT NULL, TempBan BOOLEAN NOT NULL, TempBanEndDate DATETIME,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB", connection);
				System.out.println("Table 'BungeeBans' created!");
			} 
			if(!sql.doesTableExist("BungeeBannedIPs", connection)){
				System.out.println("Table 'BungeeBannedIPs' does not exist! Creating table...");
				sql.sqlQuery("CREATE TABLE BungeeBannedIPs (IPAddress VARCHAR(50))ENGINE=INNODB", connection);
				System.out.println("Table 'BungeeBannedIPs' created!");
			}
		}
	}

	public void loadBans() throws SQLException {
		try (Connection connection = sql.getConnection();
				ResultSet tempBans = sql.sqlQuery("SELECT * FROM BungeeBans", connection);
				ResultSet ips = sql.sqlQuery("SELECT * FROM BungeeBannedIPs", connection)) {
			sql.updateQuery("DELETE FROM BungeeBans WHERE (NOW()- TempBanEndDate)>=0", connection);
			while (tempBans.next()){
				Timestamp date = tempBans.getTimestamp("TempBanEndDate");
				Calendar cal = null;
				if(date!=null){
				cal =Calendar.getInstance(); 
						 cal.setTime(date);
				}
				plugin.playerBans.put(tempBans.getString("PlayerName"), cal);
			}
			while (ips.next()){
				String IP= ips.getString("IPAddress");
				plugin.IPbans.add(IP);
			}
		}
	}
	
	public void tempBanPlayer(String name, int minuteIncrease,int hourIncrease, int dateIncrease) throws SQLException{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, +minuteIncrease);
		cal.add(Calendar.HOUR, +hourIncrease);
		cal.add(Calendar.DATE, +dateIncrease);
		sql.updateQuery("INSERT INTO BungeeBans (PlayerName, TempBan, TempBanEndDate)VALUES ('"+name+"',TRUE, DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL "+minuteIncrease+" MINUTE), INTERVAL "+hourIncrease+" HOUR),INTERVAL "+dateIncrease+" DAY))");
		plugin.playerBans.put(name, cal);
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
		if (player!=null){
			player.disconnect("You have been temporarily banned for "+dateIncrease+":days "+hourIncrease+":hours "+minuteIncrease+":minutes.");
		}
	}
	public void banPlayer(String name, String message) throws SQLException{
		sql.updateQuery("INSERT INTO BungeeBans (PlayerName, TempBan) VALUES ('"+name+"',FALSE)");
		plugin.playerBans.put(name, null);
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
		if(player!=null){
			player.disconnect(message);
		}
	}
	public void IPBanPlayer(String IP) throws SQLException, UnknownHostException{
		sql.updateQuery("INSERT INTO BungeeBannedIPs (IPAddress) VALUES ('"+IP+"')");
		plugin.IPbans.add(IP);
		for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
			if (player.getAddress().getAddress().toString().equals(IP)) {
				player.disconnect("You have been banned");
			}
		}
	}
	public void unbanPlayer(String name) throws SQLException{
		sql.updateQuery("DELETE FROM BungeeBans WHERE PlayerName = '"+name+"'");
		plugin.playerBans.remove(name);
	}
	public void unbanIP(String ip) throws SQLException{
		sql.updateQuery("DELETE FROM BungeeBannedIPs WHERE IPAddress ='"+ip+"'");
		if(plugin.IPbans.contains(ip)){
			plugin.IPbans.remove(ip);
		}
	}

	public boolean isBanned(String name) throws SQLException {
		return plugin.playerBans.containsKey(name);
	}

	public void sendBroadcast(String string) {
		for(ProxiedPlayer data:plugin.getProxy().getPlayers()){
			data.sendMessage(string);
		}
		
	}
	
	public void createChannel(String ChannelName, String ChannelFormat, boolean server, String owner){
		try {
			sql.updateQuery("INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel, Owner, CreatedDate) VALUES('"+ChannelName+"', '"+ChannelFormat+"',"+server+", '"+owner+"', CURDATE());");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ChatChannel newchan = new ChatChannel(ChannelName, ChannelFormat,owner, server);
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
		try (Connection connection = sql.getConnection()) {
			//set offline to stop them being deleted
			sql.updateQuery("UPDATE BungeePlayers SET Current=NULL WHERE Current = '"+channel+"'", connection);
			sql.sqlQuery("DELETE FROM BungeeChannels WHERE ChannelName = '"+channel+"'", connection);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//may need to go through and change any players with it as their current.
		plugin.getChatPlayer(plugin.chatChannels.get(channel).getOwner()).subtractChannelsOwned();
		plugin.chatChannels.remove(channel);
	}

	public void updateTables() {
		// TODO: get clarification on what this is meant to do, rename, move
		// into migration class if that is its purpose
		try (Connection connection = sql.getConnection()) {
			try {
				sql.sqlQuery("ALTER TABLE BungeePlayers DROP COLUMN Broadcast", connection);
			} catch (SQLException e) {
			}
			try {
				sql.sqlQuery("ALTER TABLE BungeePlayers DROP COLUMN SendRank", connection);
			} catch (SQLException e) {
			}
			try {
				sql.sqlQuery("ALTER TABLE BungeePlayers ADD SendSuffix BOOLEAN DEFAULT TRUE", connection);
			} catch (SQLException e) {
			}
			try {
				sql.sqlQuery("ALTER TABLE BungeePlayers ADD SendPrefix BOOLEAN DEFAULT TRUE", connection);
			} catch (SQLException e) {
			}
			try {
				sql.sqlQuery("ALTER TABLE BungeeChannels MODIFY ChannelFormat VARCHAR(250)", connection);
			} catch (SQLException e) {
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void UpdateSignFormats() throws SQLException {
		try (Connection connection = sql.getConnection();
				ResultSet signFormats = sql.sqlQuery("SELECT F_ID FROM BungeeSignFormats Where F_ID = 1", connection)) {
			if(signFormats.next()){
				sql.updateQuery("UPDATE BungeeSignFormats SET ColoredMOTD="+plugin.motdColored+",MOTDOnline ='"+plugin.motdFormatOnline+"',MOTDOffline= '"+plugin.motdFormatOffline+"',PlayerCountOnline= '"+plugin.playerCountFormatOnline+"', PlayerCountOnlineClick='"+plugin.playerCountFormatOnlineClick+"',PlayerCountOffline= '"+plugin.playerCountFormatOffline+"', PlayerCountOfflineClick='"+plugin.playerCountFormatOfflineClick+"', PortalFormatOnline='"+plugin.portalFormatOnline+"',PortalFormatOffline= '"+plugin.portalFormatOffline+"',PortalFormatOfflineClick= '"+plugin.portalFormatOfflineClick+"' WHERE F_ID = 1", connection);
			} else {
				sql.updateQuery("INSERT INTO BungeeSignFormats(F_ID,ColoredMOTD, MOTDOnline, MOTDOffline, PlayerCountOnline,  PlayerCountOnlineClick, PlayerCountOffline, PlayerCountOfflineClick, PortalFormatOnline, PortalFormatOffline, PortalFormatOfflineClick) VALUES(1,"+plugin.motdColored+", '"+plugin.motdFormatOnline+"', '"+plugin.motdFormatOffline+"', '"+plugin.playerCountFormatOnline+"', '"+plugin.playerCountFormatOnlineClick+"', '"+plugin.playerCountFormatOffline+"', '"+plugin.playerCountFormatOfflineClick+"', '"+plugin.portalFormatOnline+"', '"+plugin.portalFormatOffline+"', '"+plugin.portalFormatOfflineClick+"')", connection);
			}
		}
	}

	public void selectDatabase() throws SQLException {
		try (Connection connection = sql.getConnection()) {
			sql.sqlQuery("USE "+plugin.database, connection);
		};
	}

	public void deleteWarp(String warp) throws SQLException {
		sql.updateQuery("DELETE FROM BungeeWarps WHERE Name = '"+warp+"'");
		plugin.warpList.remove(warp);
	}

	public void mutePlayer(String name) {
		try {
			sql.updateQuery("UPDATE BungeePlayers SET Mute =TRUE WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void unMutePlayer(String name){
		try {
			sql.updateQuery("UPDATE BungeePlayers SET Mute =FALSE WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean portalExists(String string) throws SQLException {
		return sql.existenceQuery("SELECT Name FROM BungeePortals Where Name = '"+string+"' ");
	}

	public String getPortalsServer(String name) throws SQLException {
		return sql.singleResultStringQuery("SELECT Server FROM BungeePortals WHERE Name = '"+name+"'");
	}

	public ArrayList<String> getPortals() throws SQLException {
		ArrayList<String>portals = new ArrayList<String>();
		try (Connection connection = sql.getConnection();
				ResultSet res = sql.sqlQuery("SELECT Name FROM BungeePortals", connection)) {
			while(res.next()){
				portals.add(res.getString("Name"));
			}
			return portals;
		}
	}

	public void removeInvite(String name, String channel) {
		try {
			sql.updateQuery("DELETE FROM BungeeInvites WHERE PlayerName ='"+name+"' AND ChannelName='"+channel+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void renameChannel(String channelName, String name) {
		try {
			sql.updateQuery("UPDATE BungeeChannels SET ChannelName= '"+name+"' WHERE ChannelName ='"+channelName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reformatChannel(String channelName, String format) {
		try {
			sql.updateQuery("UPDATE BungeeChannels SET ChannelFormat= '"+format+"' WHERE ChannelName ='"+channelName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removeMemberChannel(String player, String channel) {
		try {
			sql.updateQuery("DELETE FROM BungeeMembers WHERE ChannelName ='"+channel+"' AND PlayerName='"+player+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addChannel(String name) {
		try {
			sql.updateQuery("UPDATE BungeePlayers SET ChannelsOwned = ChannelsOwned + 1 WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void subtractChannel(String name) {
		try {
			sql.updateQuery("UPDATE BungeePlayers SET ChannelsOwned = ChannelsOwned - 1 WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setChannelOwner(String channelName, String name) {
		try {
			sql.updateQuery("UPDATE BungeeChannels SET Owner = '"+name+"' WHERE ChannelName = '"+channelName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ChatChannel> getPlayersChannels(String name) throws SQLException {
		ArrayList<ChatChannel> channels = new ArrayList<ChatChannel>();
		try (Connection connection = sql.getConnection();
				ResultSet res = sql.sqlQuery("SELECT ChannelName FROM BungeeMembers WHERE PlayerName = '"+name+"'", connection)) {
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
			sql.updateQuery("INSERT INTO BungeeMembers(ChannelName, PlayerName) VALUES ('"+channel+"','"+player+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addChatSpy(ChatPlayer chatPlayer) {
		try {
			sql.updateQuery("UPDATE BungeePlayers SET ChatSpy = TRUE WHERE PlayerName = '"+chatPlayer.getName()+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plugin.chatSpying.add(chatPlayer.getName());
	}

	public void removeChatSpy(ChatPlayer chatPlayer) {
		try {
			sql.updateQuery("UPDATE BungeePlayers SET ChatSpy = FALSE WHERE PlayerName = '"+chatPlayer.getName()+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plugin.chatSpying.remove(chatPlayer.getName());
	}

	public void playerSendServer(String name) {
		try {
			sql.updateQuery("UPDATE BungeePlayers SET SendServer = TRUE WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void playerRemoveSendServer(String name) {
		try {
			sql.updateQuery("UPDATE BungeePlayers SET SendServer = FALSE WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashSet<String> getIgnores(String player){
		HashSet<String> ignorelist =  new HashSet<String>();
		try (Connection connection = sql.getConnection();
				ResultSet res = sql.sqlQuery("SELECT Ignoring FROM BungeeIgnores WHERE PlayerName = '"+player+"'", connection)) {
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
			sql.updateQuery("DELETE FROM BungeeIgnores WHERE PlayerName= '"+name+"' AND Ignoring = '"+name2+"' ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		plugin.getChatPlayer(name).removeIgnore(name2);
	}

	public void ignorePlayer(String name, String name2) {
		try {
			sql.updateQuery("INSERT INTO BungeeIgnores (PlayerName, Ignoring) VALUES ('"+name+"', '"+name2+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		plugin.getChatPlayer(name).addIgnore(name2);	
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
