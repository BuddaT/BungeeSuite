package com.mcdimensions.BungeeSuite.teleports;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class tpaccept extends Command {

	BungeeSuite plugin;
	public tpaccept(BungeeSuite bungeeSuite) {
		super(bungeeSuite.tpAccept);
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
			ProxiedPlayer targetPlayer = plugin.tpaList.get(player);
			plugin.tpaList.remove(player);
			plugin.getUtilities().teleportToPlayer(targetPlayer, player);
			return;
		}
		if(plugin.tpHereList.containsKey(player)){
			ProxiedPlayer targetPlayer = plugin.tpHereList.get(player);
			plugin.tpaList.remove(player);
			plugin.getUtilities().teleportToPlayer(player, targetPlayer);
			
			return;
		}
		return;
	}

}
