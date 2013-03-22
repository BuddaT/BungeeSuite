package com.mcdimensions.BungeeSuite.banning;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ban extends Command {

	BungeeSuite plugin;
	public ban(BungeeSuite bungeeSuite) {
		//command
		super(bungeeSuite.ban);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.mod"))return;
		//invalid arguments
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/Ban (PlayerName) (message)");
			return;
		}
		try {
			if(plugin.getUtilities().playerExists(arg1[0])){
				String message = "You have been banned!";
				//puts together a ban message.
				if(arg1.length>1){
					message= "";
					for(String data: arg1){
						if(!data.equalsIgnoreCase(arg1[0]));
						message+=data+" ";
					}
				}
				plugin.getUtilities().banPlayer(arg1[0], message);
				plugin.getUtilities().sendBroadcast(ChatColor.DARK_GREEN+"["+arg1[0]+"]"+ChatColor.GOLD+" has been banned from the server");
			}else{
				arg0.sendMessage(ChatColor.RED+"That player does not exist");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
