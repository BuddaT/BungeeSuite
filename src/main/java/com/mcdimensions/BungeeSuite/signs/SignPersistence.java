package com.mcdimensions.BungeeSuite.signs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buddat.bungeesuite.database.Database;

import com.mcdimensions.BungeeSuite.BungeeSuite;

public class SignPersistence {
	private final BungeeSuite plugin;
	private final Database database;

	public SignPersistence(BungeeSuite plugin, Database database) {
		this.plugin = plugin;
		this.database = database;
	}
	
	public void createSignSQLTables() throws SQLException {
		try (Connection connection = database.getConnection()) {
			if(!database.doesTableExist(connection, "BungeeSignType")){
				System.out.println("Table 'BungeeSignType' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeSignType (T_ID int NOT NULL AUTO_INCREMENT, Type VARCHAR(50) NOT NULL UNIQUE, PRIMARY KEY (T_Id))ENGINE=INNODB;");
				database.query(connection, "INSERT INTO BungeeSignType (Type) VALUES('PlayerList');");
				database.query(connection, "INSERT INTO BungeeSignType (Type) VALUES('MOTD');");
				System.out.println("Table 'BungeeSignType' created!");
			}
			try (ResultSet rs = database.query(connection, "SELECT Type FROM BungeeSignType WHERE Type ='Portal'")) {
				if(!rs.next()){ 
					database.query(connection, "INSERT INTO BungeeSignType (Type) VALUES('Portal');");
				}
			}
			if(!database.doesTableExist(connection, "BungeeSignLocations")){
				System.out.println("Table 'BungeeSignLocations' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeSignLocations (L_ID int NOT NULL AUTO_INCREMENT, Type VARCHAR(50) NOT NULL, Server VARCHAR(50) NOT NULL, TargetServer VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X int NOT NULL,  Y int NOT NULL, Z int NOT NULL, FOREIGN KEY(Type) REFERENCES BungeeSignType(Type) ON DELETE CASCADE, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE,  FOREIGN KEY(TargetServer) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (L_ID))ENGINE=INNODB;");
				System.out.println("Table 'BungeeSignLocations' created!");
			}
			if(!database.doesTableExist(connection, "BungeeSignFormats")){
				System.out.println("Table 'BungeeSignFormats' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeSignFormats (F_ID Int NOT NULL AUTO_INCREMENT,ColoredMOTD Boolean NOT NULL, MOTDOnline VARCHAR(50) NOT NULL, MOTDOffline VARCHAR(50) NOT NULL, PlayerCountOnline VARCHAR(50) NOT NULL,  PlayerCountOnlineClick VARCHAR(50) NOT NULL, PlayerCountOffline VARCHAR(50) NOT NULL, PlayerCountOfflineClick VARCHAR(50) NOT NULL, PortalFormatOnline VARCHAR(50) NOT NULL, PortalFormatOffline VARCHAR(50) NOT NULL, PortalFormatOfflineClick VARCHAR(50) NOT NULL,PRIMARY KEY (F_ID))ENGINE=INNODB;");
				System.out.println("Table 'BungeeSignFormats' created!");
			}
		}
	}

	public void updateSignFormats() throws SQLException {
		try (Connection connection = database.getConnection();
				ResultSet signFormats = database.query(connection, "SELECT F_ID FROM BungeeSignFormats Where F_ID = 1")) {
			if(signFormats.next()){
				database.update(connection,
						"UPDATE BungeeSignFormats " +
								" SET ColoredMOTD = ?" +
								", MOTDOnline = ?" +
								", MOTDOffline = ?" +
								", PlayerCountOnline = ?" +
								", PlayerCountOnlineClick = ?" +
								", PlayerCountOffline = ?" +
								", PlayerCountOfflineClick = ?" +
								", PortalFormatOnline = ?" +
								", PortalFormatOffline = ?" +
								", PortalFormatOfflineClick = ?" +
								" WHERE F_ID = 1",
								plugin.motdColored,
								plugin.motdFormatOnline,
								plugin.motdFormatOffline,
								plugin.playerCountFormatOnline,
								plugin.playerCountFormatOnlineClick,
								plugin.playerCountFormatOffline,
								plugin.playerCountFormatOfflineClick,
								plugin.portalFormatOnline,
								plugin.portalFormatOffline,
								plugin.portalFormatOfflineClick);
			} else {
				database.update(connection,
						"INSERT INTO BungeeSignFormats(" +
								"F_ID" +
								", ColoredMOTD" +
								", MOTDOnline" +
								", MOTDOffline" +
								", PlayerCountOnline" +
								", PlayerCountOnlineClick" +
								", PlayerCountOffline" +
								", PlayerCountOfflineClick" +
								", PortalFormatOnline" +
								", PortalFormatOffline" +
								", PortalFormatOfflineClick" +
								") VALUES (1, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
								plugin.motdColored,
								plugin.motdFormatOnline,
								plugin.motdFormatOffline,
								plugin.playerCountFormatOnline,
								plugin.playerCountFormatOnlineClick,
								plugin.playerCountFormatOffline,
								plugin.playerCountFormatOfflineClick,
								plugin.portalFormatOnline,
								plugin.portalFormatOffline,
								plugin.portalFormatOfflineClick);
			}
		}
	}
}
