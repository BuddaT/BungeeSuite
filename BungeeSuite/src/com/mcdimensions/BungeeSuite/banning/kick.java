package com.mcdimensions.BungeeSuite.banning;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class kick extends Command {
	BungeeSuite plugin;
	public kick(BungeeSuite bungeeSuite) {
		super(bungeeSuite.kick);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.mod"))return;
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/Kick (PlayerName)");
			return;
		}
		String name = arg1[0];
		
		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(name);
		String Message = "You have been kicked by "+arg0.getName();
		if(player!=null){
			if(arg1.length>1){
				int count = 0;
				for(String data: arg1){
					if(count==0){
						Message = "";
						count++;
					}else{
						Message+=data+" ";
					}
				}
			}
			player.disconnect(Message);
			plugin.getUtilities().sendBroadcast(ChatColor.DARK_GREEN+"["+player.getDisplayName()+"]"+ChatColor.GOLD+" has been kicked from the server");
		}else{
			arg0.sendMessage(ChatColor.RED+"That player is not online");
		}

	}

}
