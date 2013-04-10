package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class s extends Command {

	BungeeSuite plugin;
	public s(BungeeSuite bungeeSuite) {
		super(bungeeSuite.s);
		plugin = bungeeSuite;
	}
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length==0){
			ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
			ChatChannel cc = plugin.getChannel(cp.getPlayer().getServer().getInfo().getName());
			if(plugin.globalToggleable){
				if(!cp.getCurrent().equals(cc)){
					cp.setCurrent(cc);
				}
			}
			return;
		}
		String message  = "";
		for(String data: arg1){
			message+= data+" ";
		}
		String server = ((ProxiedPlayer) arg0).getServer().getInfo().getName();
		ChatPlayer cp= plugin.getChatPlayer(arg0.getName());
		ChatChannel cc = plugin.getChannel(cp.getPlayer().getServer().getInfo().getName());
		if(plugin.ignoreServers.contains(server)){
			return;	
		}
		cc.sendMessage(cp, message);
		if(!cp.getCurrent().equals(cc)){
			cp.setCurrent(cc);
		}
	}

}
