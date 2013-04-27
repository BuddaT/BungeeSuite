package com.mcdimensions.BungeeSuite.warps.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.warps.Warp;
import com.mcdimensions.BungeeSuite.warps.WarpLocation;

import net.buddat.bungeesuite.database.Database;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class WarpPersistence {
	private final BungeeSuite plugin;
	private final Database database;
	
	public WarpPersistence(BungeeSuite plugin, Database database) {
		this.plugin = plugin;
		this.database = database;
	}
	
	/**
	 * Creates the tables required for storing warps. This is done regardless of
	 * enabled, as other functionality references these tables even if warps are
	 * not active.
	 * 
	 * @throws SQLException
	 *             if a database error occurs.
	 */
	public void createTables() throws SQLException {
		try (Connection connection = database.getConnection()) {
			if(!database.doesTableExist(connection, "BungeeWarps")){
				System.out.println("Table 'BungeeWarps' does not exist! Creating table...");
				database.query(connection, "CREATE TABLE BungeeWarps (W_ID int NOT NULL AUTO_INCREMENT,Name VARCHAR(50) NOT NULL UNIQUE, Server VARCHAR(50) NOT NULL, World VARCHAR(50) NOT NULL, X double NOT NULL,  Y double NOT NULL, Z double NOT NULL, Yaw float NOT NULL, Pitch float NOT NULL, Visible boolean NOT NULL, FOREIGN KEY(Server) REFERENCES BungeeServers(ServerName) ON DELETE CASCADE, PRIMARY KEY (W_ID))ENGINE=INNODB;");
				System.out.println("Table 'BungeeWarps' created!");
			}
		}
	}
	
	public boolean warpExists(String name) throws SQLException{
		return database.existenceQuery("SELECT Name FROM BungeeWarps WHERE Name = '"+name+"'");
	}

	public Warp loadWarp(String string) throws SQLException {
		try (Connection connection = database.getConnection();
				ResultSet res = database.query(connection, "SELECT * FROM BungeeWarps WHERE Name='"+string+"'")) {
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
		try (Connection connection = database.getConnection();
				ResultSet res = database.query(connection, "SELECT Name,Visible FROM BungeeWarps")) {
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

	public void deleteWarp(String warp) throws SQLException {
		database.update("DELETE FROM BungeeWarps WHERE Name = ?", warp);
		plugin.warpList.remove(warp);
	}
}
