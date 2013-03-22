package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class r extends Command {
	BungeeSuite plugin;
	public r(BungeeSuite bungeeSuite) {
		super(bungeeSuite.r);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/r (message)");
			return;
		}
		ChatPlayer cp =plugin.getChatPlayer(arg0.getName());
		String player =cp.getReplyPlayer();
		if(player!=null){
		ChatPlayer rp =plugin.getChatPlayer(player);
		if(rp!=null){
			String message="";
			for(String data:arg1){
				message+=data+" ";
			}
			rp.sendPrivate(message, arg0.getName());
			arg0.sendMessage(ChatColor.GOLD+"[me->"+rp.getName()+"]"+ChatColor.WHITE+message);
			return;
		}else{
			arg0.sendMessage(ChatColor.RED+"That player is no longer online");
		}
		}else{
			arg0.sendMessage(ChatColor.RED+"You have no one to reply to");
		}
	}

}
