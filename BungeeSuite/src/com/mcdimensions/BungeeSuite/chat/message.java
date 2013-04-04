package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class message extends Command {
	BungeeSuite plugin;
	public message(BungeeSuite bungeeSuite) {
		super(bungeeSuite.message);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length<2){
			arg0.sendMessage(ChatColor.RED+"/"+plugin.message+" (player) (message)");
			return;
		}
		ProxiedPlayer player  = plugin.getUtilities().getClosestPlayer(arg1[0]);
		if(player!=null){
			ChatPlayer cp = plugin.getChatPlayer(player.getName());
			String message = "";
			for(String data: arg1){
				if(!data.equalsIgnoreCase(arg1[0])){
					message+=data+" ";
				}
			}
			cp.sendPrivate(message,arg0.getName());
			arg0.sendMessage(ChatColor.GOLD+"[me->"+cp.getName()+"]"+ChatColor.WHITE+message);
		}else{
			arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
		}
	}

}
