package com.mcdimensions.BungeeSuite.utilities;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;



import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;
import com.mcdimensions.BungeeSuite.chat.GlobalCommand;
import com.mcdimensions.BungeeSuite.teleports.TPCommand;
import com.mcdimensions.BungeeSuite.warps.Warp;
import com.mcdimensions.BungeeSuite.warps.WarpLocation;


public class Utilities {
	SQL sql;
	BungeeSuite plugin;
	
	public Utilities(BungeeSuite bpk){
		plugin = bpk;
		sql = plugin.getSQL();
	}
	
	public void createServer(String name) throws SQLException{
		sql.initialise();
		sql.standardQuery("INSERT INTO BungeeServers(ServerName, Online) VALUES('"+name+"', FALSE)");
		sql.closeConnection();
	}
	public void CreateWarpSQLTables() throws SQLException {
		sql.initialise();
		if(!sql.doesTableExist("BungeeWarps")){
			System.out.println("Table 'BungeeWarps' does not exist! Creating table...");
			sql.standardQuery("CREATE TABLE BungeeWarps (W_ID int NOT NULL AUTO_INCREMENT,Name VARCHAR(50) NOT NULL UNIQUE, Server VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X double NOT NULL,  Y double NOT NULL, Z double NOT NULL, Yaw float NOT NULL, Pitch float NOT NULL, Visible boolean NOT NULL, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (W_ID))ENGINE=INNODB;");
			System.out.println("Table 'BungeeWarps' created!");
		}
		sql.closeConnection();
	}
	public boolean serverExists(String name) throws SQLException{
		sql.initialise();
		boolean check = sql.existenceQuery("SELECT ServerName FROM BungeeServers WHERE ServerName = '"+name+"'");
		sql.closeConnection();
		return check;
	}
	
	public HashMap<String,Warp> loadWarps() throws SQLException {
		HashMap<String,Warp> warps = new HashMap<String,Warp>();
		sql.initialise();
		if(!sql.doesTableExist("BungeeWarps")){
		    System.out.println("Table 'BungeeWarps' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeWarps (W_ID int NOT NULL AUTO_INCREMENT,Name VARCHAR(50) NOT NULL UNIQUE, Server VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X double NOT NULL,  Y double NOT NULL, Z double NOT NULL, Yaw float NOT NULL, Pitch float NOT NULL, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (W_ID))ENGINE=INNODB;");
		    System.out.println("Table 'BungeeWarps' created!");
		}
		ResultSet res = sql.sqlQuery("SELECT * FROM BungeeWarps");
		while(res.next()){
			String name = res.getString("Name");
			warps.put(name, new Warp(name, new WarpLocation(res.getString("Server"), res.getString("World"), res.getDouble("X"), res.getDouble("Y"), res.getDouble("Z"), res.getFloat("Yaw"), res.getFloat("Pitch")),res.getBoolean("Visable")));
		}
		res.close();
		sql.closeConnection();
		return warps;
	}

	public ProxiedPlayer getClosestPlayer(String player) {
		String name = player.toLowerCase();
		for (ProxiedPlayer data : plugin.getProxy().getPlayers())
			if (data.getName().toLowerCase().startsWith(name)) {
				return data;
			}
		return null;
	}
	
	public void createSQLPlayerTable() throws SQLException{
		sql.initialise();
		if(!sql.doesTableExist("BungeePlayers")){
		    System.out.println("Table 'BungeePlayers' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeePlayers (P_ID int NOT NULL AUTO_INCREMENT, PlayerName VARCHAR(50) NOT NULL UNIQUE, DisplayName VARCHAR(50), Current VARCHAR(50), ChannelsOwned int NOT NULL DEFAULT 0, LastOnline DATE NOT NULL, ChatSpy BOOLEAN DEFAULT FALSE, SendServer BOOLEAN DEFAULT true, SendPrefix BOOLEAN DEFAULT true,SendSuffix BOOLEAN DEFAULT true, Mute BOOLEAN DEFAULT FALSE,IPAddress VARCHAR(50), FOREIGN KEY(Current) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE, PRIMARY KEY (P_Id))ENGINE=INNODB;");
		    System.out.println("Table 'ChatPlayers' created!");
		} 
		sql.closeConnection();
	}
	public void createSQLServerTable() throws SQLException {
		sql.initialise();
		if (!sql.doesTableExist("BungeeServers")) {
			System.out
					.println("Table 'BungeeServers' does not exist! Creating table...");
			sql.standardQuery("CREATE TABLE BungeeServers (S_ID int NOT NULL AUTO_INCREMENT, ServerName VARCHAR(50) NOT NULL UNIQUE, PlayersOnline int DEFAULT 0, MaxPlayers int,MOTD VARCHAR(60), Online BOOLEAN DEFAULT FALSE, PRIMARY KEY (S_Id))ENGINE=INNODB;");
			System.out.println("Table 'BungeeServers' created!");
		}
		sql.closeConnection();
	}
	public void CreateSignSQLTables() throws SQLException {
		sql.initialise();
		if(!sql.doesTableExist("BungeeSignType")){
	    System.out.println("Table 'BungeeSignType' does not exist! Creating table...");
	    sql.standardQuery("CREATE TABLE BungeeSignType (T_ID int NOT NULL AUTO_INCREMENT, Type VARCHAR(50) NOT NULL UNIQUE, PRIMARY KEY (T_Id))ENGINE=INNODB;");
		sql.standardQuery("INSERT INTO BungeeSignType (Type) VALUES('PlayerList');");
		sql.standardQuery("INSERT INTO BungeeSignType (Type) VALUES('MOTD');");
	    System.out.println("Table 'BungeeSignType' created!");
		}
		boolean check = sql.existenceQuery("SELECT Type FROM BungeeSignType WHERE Type ='Portal'");
		if(!check){ 
			sql.standardQuery("INSERT INTO BungeeSignType (Type) VALUES('Portal');");
		}
		if(!sql.doesTableExist("BungeeSignLocations")){
		    System.out.println("Table 'BungeeSignLocations' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeSignLocations (L_ID int NOT NULL AUTO_INCREMENT, Type VARCHAR(50) NOT NULL, Server VARCHAR(50) NOT NULL, TargetServer VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X int NOT NULL,  Y int NOT NULL, Z int NOT NULL, FOREIGN KEY(Type) REFERENCES BungeeSignType(Type) ON DELETE CASCADE, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE,  FOREIGN KEY(TargetServer) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (L_ID))ENGINE=INNODB;");
		    System.out.println("Table 'BungeeSignLocations' created!");
		}
		if(!sql.doesTableExist("BungeeSignFormats")){
		    System.out.println("Table 'BungeeSignFormats' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeSignFormats (F_ID Int NOT NULL AUTO_INCREMENT,ColoredMOTD Boolean NOT NULL, MOTDOnline VARCHAR(50) NOT NULL, MOTDOffline VARCHAR(50) NOT NULL, PlayerCountOnline VARCHAR(50) NOT NULL,  PlayerCountOnlineClick VARCHAR(50) NOT NULL, PlayerCountOffline VARCHAR(50) NOT NULL, PlayerCountOfflineClick VARCHAR(50) NOT NULL, PortalFormatOnline VARCHAR(50) NOT NULL, PortalFormatOffline VARCHAR(50) NOT NULL, PortalFormatOfflineClick VARCHAR(50) NOT NULL,PRIMARY KEY (F_ID))ENGINE=INNODB;");
		    System.out.println("Table 'BungeeSignFormats' created!");
		}
		sql.closeConnection();
	}
	public void CreatePortalSQLTables() throws SQLException {
		sql.initialise();
		if(!sql.doesTableExist("BungeePortals")){
			System.out.println("Table 'BungeePortals' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeePortals (P_ID int NOT NULL AUTO_INCREMENT,Name VARCHAR(50) NOT NULL, Server VARCHAR(50) NOT NULL, ToServer VARCHAR(50) NOT NULL, Warp VARCHAR(50), World VARCHAR(50) NOT NULL, XMax int NOT NULL, XMin int NOT NULL, YMax int NOT NULL, Ymin int NOT NULL, ZMax int NOT NULL, Zmin int NOT NULL, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, FOREIGN KEY(ToServer) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE,  FOREIGN KEY(Warp) REFERENCES BungeeWarps(Name) ON DELETE CASCADE, PRIMARY KEY (P_ID))ENGINE=INNODB;");
		    System.out.println("Table 'BungeePortals' created!");
		}
		sql.closeConnection();
	}
	public boolean warpExists(String name) throws SQLException{
		sql.initialise();
		boolean check = sql.existenceQuery("SELECT Name FROM BungeeWarps WHERE Name = '"+name+"'");
		sql.closeConnection();
		return check;
	}

	public Warp loadWarp(String string) throws SQLException {
		sql.initialise();
		ResultSet res = sql.sqlQuery("SELECT * FROM BungeeWarps WHERE Name='"+string+"'");
		Warp warp = null;
		while(res.next()){
			String name = res.getString("Name");
			WarpLocation wl = new WarpLocation(res.getString("Server"), res.getString("World"), res.getDouble("X"), res.getDouble("Y"), res.getDouble("Z"), res.getFloat("Yaw"), res.getFloat("Pitch"));
			warp = new Warp(name, wl, res.getBoolean("Visible"));
			plugin.warpList.put(name, warp);
		}
		sql.closeConnection();
		res.close();
		return warp;
	}

	public String[] getWarpList(CommandSender arg0) throws SQLException {
		sql.initialise();
		String[] list= new String[2];
		list[0]=ChatColor.AQUA+"Warp List: ";
		list[1]=ChatColor.BLUE+"Private warps: ";
		ResultSet res = sql.sqlQuery("SELECT Name,Visible FROM BungeeWarps");
		while(res.next()){
			String name = res.getString("Name");
			boolean visible = res.getBoolean("Visible");
			if(visible){
				list[0]+=name+ ", ";
			}else{
				list[1]+=name+ ", ";
			}
		}
		sql.closeConnection();
		res.close();
		return list;
		
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
		sql.initialise();
		if(!sql.doesTableExist("BungeeChannels")){
		    System.out.println("Table 'BungeeChannels' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeChannels (C_ID int NOT NULL AUTO_INCREMENT, ChannelName VARCHAR(50) NOT NULL UNIQUE, ChannelFormat VARCHAR(250) NOT NULL, isServerChannel BOOLEAN DEFAULT FALSE, Owner VARCHAR(50), CreatedDate DATE, PRIMARY KEY (C_ID))ENGINE=INNODB;");
		    System.out.println("Table 'BungeeChannels' created!");
		}
		if(!sql.doesTableExist("BungeePlayers")){
			sql.initialise();
			createSQLPlayerTable();
			sql.closeConnection();
		}
		sql.initialise();
		if(!sql.doesTableExist("BungeeInvites")){
		    System.out.println("Table 'BungeeInvites' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeInvites (I_ID int NOT NULL AUTO_INCREMENT, PlayerName VARCHAR(50) NOT NULL, ChannelName VARCHAR(50) NOT NULL, FOREIGN KEY(PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE, FOREIGN KEY (ChannelName) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY (I_ID))ENGINE=INNODB;");
		    System.out.println("Table 'BungeeInvites' created!");
		}
		if(!sql.doesTableExist("BungeeMembers")){
		    System.out.println("Table 'BungeeMembers' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeMembers (ChannelName VARCHAR(50) NOT NULL, PlayerName VARCHAR(50) NOT NULL, FOREIGN KEY (ChannelName) REFERENCES BungeeChannels(ChannelName) ON DELETE CASCADE ON UPDATE CASCADE,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB");
		    System.out.println("Table 'BungeeMembers' created!");
		} 
		if(!sql.doesTableExist("BungeeIgnores")){
		    System.out.println("Table 'BungeeIgnores' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeIgnores (PlayerName VARCHAR(50) NOT NULL, Ignoring VARCHAR(50) NOT NULL, FOREIGN KEY (Ignoring) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB");
		    System.out.println("Table 'BungeeIgnores' created!");
		} 
		sql.closeConnection();
	}
	
	public void createStandardChannels() throws SQLException{
		sql.initialise();
		boolean check = sql.existenceQuery("SELECT ChannelName FROM BungeeChannels WHERE ChannelName = 'Global';");
		sql.closeConnection();
		if(!check){
			sql.initialise();
			sql.standardQuery("INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel) VALUES('Global', '"+plugin.defaultServerChannelFormat+"', TRUE);");
			sql.closeConnection();
		}
		for(String data: plugin.getProxy().getServers().keySet()){
			sql.initialise();
			boolean check2 = sql.existenceQuery("SELECT ChannelName FROM BungeeChannels WHERE ChannelName = '"+data+"';");
			sql.closeConnection();
			if(!check2){
				sql.initialise();
				sql.standardQuery("INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel) VALUES('"+data+"', '"+plugin.defaultServerChannelFormat+"', TRUE);");
				sql.closeConnection();
			}
		}
	}
	public void createPlayer(String player, String connection) throws SQLException {
		sql.initialise();
		if(plugin.globalDefault){
		sql.standardQuery("INSERT INTO BungeePlayers (PlayerName, DisplayName, Current, LastOnline, IPAddress) VALUES ('"+player+"','"+player+"','Global', CURDATE(), '"+connection+"')");
		}else{
			sql.standardQuery("INSERT INTO BungeePlayers (PlayerName, DisplayName, Current, LastOnline, IPAddress) VALUES ('"+player+"','"+player+"',NULL, CURDATE(), '"+connection+"')");
		}
		if(plugin.chatEnabled){
			getChatPlayer(player);
		}
		sql.closeConnection();
	}
	public void setCurrentChannel(String player, String channel){
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET Current = '"+channel+"' WHERE PlayerName = '"+player+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
	}
	public void updateIP(String name, String connection) throws SQLException{
		sql.initialise();
		sql.standardQuery("UPDATE BungeePlayers SET IPAddress = '"+connection+"' WHERE PlayerName = '"+name+"'");
		sql.closeConnection();
	}
	
	public void updateLastSeen(String player) throws SQLException{
		sql.initialise();
		sql.standardQuery("UPDATE BungeePlayers SET LastOnline=CURDATE() WHERE PlayerName = '"+player+"'");
		sql.closeConnection();
	}
	public boolean chatChannelExists(String name) throws SQLException{ //gotta sql query as the db is not case sensitive anymore
		sql.initialise();
		boolean check = sql.existenceQuery("SELECT ChannelName FROM BungeeChannels Where ChannelName = '"+name+"'");
		sql.closeConnection();
		return check;
	}
	public ChatChannel getChatChannel(String name){
		return plugin.chatChannels.get(name);
	}
	public boolean playerExists(String player) throws SQLException {
		sql.initialise();
		boolean check = sql.existenceQuery("SELECT PlayerName FROM BungeePlayers Where PlayerName = '"+player+"'");
		sql.closeConnection();
		return check;
	}
	public String getIP(String player) throws SQLException{
		sql.initialise();
		ResultSet res = sql.sqlQuery("SELECT IPAddress FROM BungeePlayers WHERE PlayerName = '"+player+"'");
		String ip = null;
		while(res.next()){
			ip = res.getString("IPAddress");
		}
		sql.closeConnection();
		return ip;
	}
	
	public ChatPlayer getChatPlayer(String player) throws SQLException {
		sql.initialise();
		plugin.cl.cLog("Loading "+ player);
		ResultSet res = sql.sqlQuery("SELECT * FROM BungeePlayers WHERE PlayerName = '"+player+"'");
		ChatPlayer cp = null;
		while(res.next()){
			ChatChannel channel = plugin.chatChannels.get(res.getString("Current"));
			cp = new ChatPlayer(player, res.getString("DisplayName"), channel, res.getBoolean("ChatSpy"), res.getBoolean("SendServer"), res.getBoolean("Mute"), res.getInt("ChannelsOwned"), res.getBoolean("SendPrefix"), res.getBoolean("SendSuffix"));
		}
		//add channels to player
		ResultSet res2 = sql.sqlQuery("SELECT * FROM BungeeMembers WHERE PlayerName = '"+player+"'");
		while(res2.next()){
			ChatChannel cc = plugin.getChannel(res2.getString("ChannelName"));
			cc.onlineMember(cp);
			if(!cp.getChannels().contains(cc.getName())){
				cp.addChannel(cc.getName());
			}
		}
		//add players ignored
		ResultSet res3 = sql.sqlQuery("SELECT Ignoring FROM BungeeIgnores WHERE PlayerName = '"+player+"'");
		while(res3.next()){
			cp.addIgnore(res3.getString("Ignoring"));
		}
		plugin.onlinePlayers.put(player, cp);
		if(cp.isChatSpying()){
			plugin.chatSpying.add(cp.getName());
		}
		res.close();
		res2.close();
		sql.closeConnection();
		return null;
	}

	public void loadChannels() throws SQLException {
		sql.initialise();
		ResultSet res = sql.sqlQuery("SELECT * FROM BungeeChannels");
		while(res.next()){
			ChatChannel newchan = new ChatChannel(res.getString("ChannelName"), res.getString("ChannelFormat"),res.getString("Owner"), res.getBoolean("isServerChannel"));
			plugin.chatChannels.put(newchan.getName(), newchan);
		}
		res.close();
		ResultSet res2 = sql.sqlQuery("SELECT * FROM BungeeInvites");
		while(res2.next()){
			ChatChannel c = plugin.getChannel(res2.getString("ChannelName"));
			c.invitePlayer(res.getString("PlayerName"));
		}
		sql.closeConnection();
		
	}

	public void setNickName(String name, String string) throws SQLException {
		sql.initialise();
		sql.standardQuery("UPDATE BungeePlayers SET DisplayName='"+string+"' WHERE PlayerName = '"+name+"'");
		sql.closeConnection();
		
	}

	public void createBanningSQLTables() throws SQLException {
		sql.initialise();
		if(!sql.doesTableExist("BungeeBans")){
		    System.out.println("Table 'BungeeBans' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeBans (PlayerName VARCHAR(50) NOT NULL, TempBan BOOLEAN NOT NULL, TempBanEndDate DATETIME,FOREIGN KEY (PlayerName) REFERENCES BungeePlayers(PlayerName) ON DELETE CASCADE ON UPDATE CASCADE)ENGINE=INNODB");
		    System.out.println("Table 'BungeeBans' created!");
		} 
		if(!sql.doesTableExist("BungeeBannedIPs")){
		    System.out.println("Table 'BungeeBannedIPs' does not exist! Creating table...");
		    sql.standardQuery("CREATE TABLE BungeeBannedIPs (IPAddress VARCHAR(50))ENGINE=INNODB");
		    System.out.println("Table 'BungeeBannedIPs' created!");
		} 
		sql.closeConnection();
		
	}

	public void loadBans() throws SQLException {
		sql.initialise();
		sql.standardQuery("DELETE FROM BungeeBans WHERE (NOW()- TempBanEndDate)>=0");
		ResultSet res = sql.sqlQuery("SELECT * FROM BungeeBans");
		while(res.next()){
			Timestamp date = res.getTimestamp("TempBanEndDate");
			Calendar cal = null;
			if(date!=null){
			cal =Calendar.getInstance(); 
					 cal.setTime(date);
			}
			plugin.playerBans.put(res.getString("PlayerName"),cal);
		}
		ResultSet ips = sql.sqlQuery("SELECT * FROM BungeeBannedIPs");
		while(ips.next()){
			String IP= ips.getString("IPAddress");
			plugin.IPbans.add(IP);
		}
		sql.closeConnection();
		
	}
	
	public void tempBanPlayer(String name, int minuteIncrease,int hourIncrease, int dateIncrease) throws SQLException{
		sql.initialise();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, +minuteIncrease);
		cal.add(Calendar.HOUR, +hourIncrease);
		cal.add(Calendar.DATE, +dateIncrease);
		sql.standardQuery("INSERT INTO BungeeBans (PlayerName, TempBan, TempBanEndDate)VALUES ('"+name+"',TRUE, DATE_ADD(DATE_ADD(DATE_ADD(NOW(), INTERVAL "+minuteIncrease+" MINUTE), INTERVAL "+hourIncrease+" HOUR),INTERVAL "+dateIncrease+" DAY))");
		plugin.playerBans.put(name, cal);
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
		if(player!=null){
			player.disconnect("You have been temporarily banned for "+dateIncrease+":days "+hourIncrease+":hours "+minuteIncrease+":minutes.");
		}
		sql.closeConnection();	
	}
	public void banPlayer(String name, String message) throws SQLException{
		sql.initialise();
		sql.standardQuery("INSERT INTO BungeeBans (PlayerName, TempBan) VALUES ('"+name+"',FALSE)");
		plugin.playerBans.put(name, null);
		ProxiedPlayer player = plugin.getProxy().getPlayer(name);
		if(player!=null){
			player.disconnect(message);
		}
		sql.closeConnection();
	}
	public void IPBanPlayer(String IP) throws SQLException, UnknownHostException{
		sql.initialise();
		sql.standardQuery("INSERT INTO BungeeBannedIPs (IPAddress) VALUES ('"+IP+"')");
		plugin.IPbans.add(IP);
		for(ProxiedPlayer player : plugin.getProxy().getPlayers()) {
			if (player.getAddress().getAddress().toString().equals(IP)) {
				player.disconnect("You have been banned");
			}
		}
		sql.closeConnection();
	}
	public void unbanPlayer(String name) throws SQLException{
		sql.initialise();
		sql.standardQuery("DELETE FROM BungeeBans WHERE PlayerName = '"+name+"'");
		plugin.playerBans.remove(name);
		sql.closeConnection();
	}
	public void unbanIP(String ip) throws SQLException{
		sql.initialise();
		sql.standardQuery("DELETE FROM BungeeBannedIPs WHERE IPAddress ='"+ip+"'");
		if(plugin.IPbans.contains(ip)){
			plugin.IPbans.remove(ip);
		}
		sql.closeConnection();
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
		sql.initialise();
		try {
			sql.standardQuery("INSERT INTO BungeeChannels(ChannelName, ChannelFormat, isServerChannel, Owner, CreatedDate) VALUES('"+ChannelName+"', '"+ChannelFormat+"',"+server+", '"+owner+"', CURDATE());");
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
		sql.closeConnection();
	}
	
	
	public void deleteChannel(String channel){
		for(String data:plugin.getChannel(channel).members){//set online players to new channel
			ChatPlayer cp = plugin.getChatPlayer(data);
			cp.removeChannel(channel);
			if (plugin.globalDefault) {
				cp.setCurrentChannel(plugin.getChannel("Global"));
			} else {
				ChatChannel newc = plugin.getChannel(cp.getCurrentServer());
				cp.setCurrentChannel(newc);
			}
		}
		sql.initialise();//set offline to stop them being deleted
		try {
			sql.standardQuery("UPDATE BungeePlayers SET Current=NULL WHERE Current = '"+channel+"'");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sql.standardQuery("DELETE FROM BungeeChannels WHERE ChannelName = '"+channel+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		plugin.getUtilities().subtractChannel(plugin.chatChannels.get(channel).getOwner());
		plugin.chatChannels.remove(channel);
	}

	public void updateTables() {
		sql.initialise();
			try {
				sql.standardQuery("ALTER TABLE BungeePlayers DROP COLUMN Broadcast");
			} catch (SQLException e) {
			}
			try {
				sql.standardQuery("ALTER TABLE BungeePlayers DROP COLUMN SendRank");
			} catch (SQLException e) {
			}
			try {
				sql.standardQuery("ALTER TABLE BungeePlayers ADD SendSuffix BOOLEAN DEFAULT TRUE");
			} catch (SQLException e) {
			}
			try {
				sql.standardQuery("ALTER TABLE BungeePlayers ADD SendPrefix BOOLEAN DEFAULT TRUE");
			} catch (SQLException e) {
			}
			try {
				sql.standardQuery("ALTER TABLE BungeeChannels MODIFY ChannelFormat VARCHAR(250)");
			} catch (SQLException e) {
			}
		sql.closeConnection();
	}

	public void UpdateSignFormats() throws SQLException {
		sql.initialise();
		if(sql.existenceQuery("SELECT F_ID FROM BungeeSignFormats Where F_ID = 1")){
			sql.standardQuery("UPDATE BungeeSignFormats SET ColoredMOTD="+plugin.motdColored+",MOTDOnline ='"+plugin.motdFormatOnline+"',MOTDOffline= '"+plugin.motdFormatOffline+"',PlayerCountOnline= '"+plugin.playerCountFormatOnline+"', PlayerCountOnlineClick='"+plugin.playerCountFormatOnlineClick+"',PlayerCountOffline= '"+plugin.playerCountFormatOffline+"', PlayerCountOfflineClick='"+plugin.playerCountFormatOfflineClick+"', PortalFormatOnline='"+plugin.portalFormatOnline+"',PortalFormatOffline= '"+plugin.portalFormatOffline+"',PortalFormatOfflineClick= '"+plugin.portalFormatOfflineClick+"' WHERE F_ID = 1");
		}else{
		sql.standardQuery("INSERT INTO BungeeSignFormats(F_ID,ColoredMOTD, MOTDOnline, MOTDOffline, PlayerCountOnline,  PlayerCountOnlineClick, PlayerCountOffline, PlayerCountOfflineClick, PortalFormatOnline, PortalFormatOffline, PortalFormatOfflineClick) VALUES(1,"+plugin.motdColored+", '"+plugin.motdFormatOnline+"', '"+plugin.motdFormatOffline+"', '"+plugin.playerCountFormatOnline+"', '"+plugin.playerCountFormatOnlineClick+"', '"+plugin.playerCountFormatOffline+"', '"+plugin.playerCountFormatOfflineClick+"', '"+plugin.portalFormatOnline+"', '"+plugin.portalFormatOffline+"', '"+plugin.portalFormatOfflineClick+"')");
		}
		sql.closeConnection();
	}

	public void selectDatabase() throws SQLException {
		sql.initialise();
		sql.standardQuery("USE "+plugin.database);
		sql.closeConnection();
	}

	public void deleteWarp(String warp) throws SQLException {
		sql.initialise();
		sql.standardQuery("DELETE FROM BungeeWarps WHERE Name = '"+warp+"'");
		plugin.warpList.remove(warp);
		sql.closeConnection();
		
	}

	public void mutePlayer(String name) {
	sql.initialise();
	try {
		sql.standardQuery("UPDATE BungeePlayers SET Mute =TRUE WHERE PlayerName = '"+name+"'");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	sql.closeConnection();
	}
	public void unMutePlayer(String name){
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET Mute =FALSE WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
	}

	public boolean portalExists(String string) throws SQLException {
		sql.initialise();
		boolean check = sql.existenceQuery("SELECT Name FROM BungeePortals Where Name = '"+string+"' ");
		sql.closeConnection();
		return check;
	}

	public String getPortalsServer(String name) throws SQLException {
		sql.initialise();
		String server = null;
		ResultSet res = sql.sqlQuery("SELECT Server FROM BungeePortals WHERE Name = '"+name+"'");
		while(res.next()){
			server = res.getString("Server");
		}
		sql.closeConnection();
		return server;
	}

	public ArrayList<String> getPortals() throws SQLException {
		ArrayList<String>portals = new ArrayList<String>();
		sql.initialise();
		ResultSet res = sql.sqlQuery("SELECT Name FROM BungeePortals");
		while(res.next()){
			portals.add(res.getString("Name"));
		}
		res.close();
		sql.closeConnection();
		return portals;
	}

	public void removeInvite(String name, String channel) {
		sql.initialise();
		try {
			sql.standardQuery("DELETE FROM BungeeInvites WHERE PlayerName ='"+name+"' AND ChannelName='"+channel+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}

	public void renameChannel(String channelName, String name) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeeChannels SET ChannelName= '"+name+"' WHERE ChannelName ='"+channelName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}

	public void reformatChannel(String channelName, String format) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeeChannels SET ChannelFormat= '"+format+"' WHERE ChannelName ='"+channelName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
	}

	public void removeMemberChannel(String player, String channel) {
		sql.initialise();
		try {
			sql.standardQuery("DELETE FROM BungeeMembers WHERE ChannelName ='"+channel+"' AND PlayerName='"+player+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}

	public void addChannel(String name) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET ChannelsOwned = ChannelsOwned + 1 WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}

	public void subtractChannel(String name) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET ChannelsOwned = ChannelsOwned - 1 WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}

	public void setChannelOwner(String channelName, String name) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeeChannels SET Owner = '"+name+"' WHERE ChannelName = '"+channelName+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}

	public ArrayList<ChatChannel> getPlayersChannels(String name) throws SQLException {
		ArrayList<ChatChannel> channels = new ArrayList<ChatChannel>();
		sql.initialise();
		ResultSet res = sql.sqlQuery("SELECT ChannelName FROM BungeeMembers WHERE PlayerName = '"+name+"'");
		while(res.next()){
			channels.add(plugin.getChannel(res.getString("ChannelName")));
		}
		if(plugin.globalToggleable && CommandUtil.hasPermission(name, GlobalCommand.PERMISSION_NODES)){
			channels.add(plugin.getChannel("Global"));
		}
		sql.closeConnection();
		return channels;
	}

	public void addMemberChannel(String player, String channel) {
		sql.initialise();
		try {
			sql.standardQuery("INSERT INTO BungeeMembers(ChannelName, PlayerName) VALUES ('"+channel+"','"+player+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}

	public void addChatSpy(ChatPlayer chatPlayer) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET ChatSpy = TRUE WHERE PlayerName = '"+chatPlayer.getName()+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plugin.chatSpying.add(chatPlayer.getName());
		sql.closeConnection();
	}

	public void removeChatSpy(ChatPlayer chatPlayer) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET ChatSpy = FALSE WHERE PlayerName = '"+chatPlayer.getName()+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plugin.chatSpying.remove(chatPlayer.getName());
		sql.closeConnection();
	}

	public void playerSendServer(String name) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET SendServer = TRUE WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}
	public void playerRemoveSendServer(String name) {
		sql.initialise();
		try {
			sql.standardQuery("UPDATE BungeePlayers SET SendServer = FALSE WHERE PlayerName = '"+name+"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.closeConnection();
		
	}
	
	public HashSet<String> getIgnores(String player){
		HashSet<String> ignorelist =  new HashSet<String>();
		sql.initialise();
		ResultSet res = null;
		try {
			res = sql.sqlQuery("SELECT Ignoring FROM BungeeIgnores WHERE PlayerName = '"+player+"'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			while(res.next()){
				ignorelist.add(res.getString("Ignoring"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.closeConnection();
		return ignorelist;
	}

	public void unignorePlayer(String name, String name2) {
		sql.initialise();
		try {
			sql.standardQuery("DELETE FROM BungeeIgnores WHERE PlayerName= '"+name+"' AND Ignoring = '"+name2+"' ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.closeConnection();
		plugin.getChatPlayer(name).removeIgnore(name2);
		
	}

	public void ignorePlayer(String name, String name2) {
		sql.initialise();
		try {
			sql.standardQuery("INSERT INTO BungeeIgnores (PlayerName, Ignoring) VALUES ('"+name+"', '"+name2+"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql.closeConnection();
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
