package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class tpdeny extends Command {

	BungeeSuite plugin;
	public tpdeny(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpdeny);
		plugin = bungeeSuite;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		ProxiedPlayer player = (ProxiedPlayer) arg0;
		if(plugin.blockedTeleports.contains(player.getServer().getInfo().getName())){
			arg0.sendMessage(ChatColor.RED+"This server does not have teleports enabled");
			return;
		}
		if(plugin.tpaList.containsKey(player)){
			ProxiedPlayer targetPlayer =plugin.tpaList.get(player);
			plugin.tpaList.remove(player);
			targetPlayer.sendMessage(ChatColor.RED+player.getDisplayName()+" has denied your request");
		}
		if(plugin.tpHereList.containsKey(player)){
			ProxiedPlayer targetPlayer =plugin.tpHereList.get(player);
			plugin.tpHereList.remove(player);
			targetPlayer.sendMessage(ChatColor.RED+player.getDisplayName()+" has denied your request");
		}

	}

}
