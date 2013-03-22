package com.mcdimensions.BungeeSuite.banning;

import java.net.UnknownHostException;
import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ipban extends Command {

	
	BungeeSuite plugin;
	public ipban(BungeeSuite bungeeSuite) {
		//command
		super(bungeeSuite.ipban);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.mod"))return;
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/Ipban (PlayerName/IP)");
			return;
		}
		try {
			//if playername was inserted
			if(plugin.getUtilities().playerExists(arg1[0])){
				String ip = null;
				try {
					//get players ip
					ip = plugin.getUtilities().getIP(arg1[0]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					plugin.getUtilities().IPBanPlayer(ip);
					plugin.getUtilities().sendBroadcast(ChatColor.DARK_GREEN+"["+ip+"]"+ChatColor.GOLD+" has been IPBanned from the server");
				} catch (SQLException | UnknownHostException e) {
					e.printStackTrace();
				}
			}else{
				//if IP was typed in
				//TODO may need to add a '/' to the start
				plugin.getUtilities().IPBanPlayer(arg1[0]);
				plugin.getUtilities().sendBroadcast(ChatColor.DARK_GREEN+"["+arg1[0]+"]"+ChatColor.GOLD+" has been IPBanned from the server");
			}
		} catch (SQLException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
