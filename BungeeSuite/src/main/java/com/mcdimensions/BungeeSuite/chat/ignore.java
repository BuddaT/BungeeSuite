package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ignore extends Command {

	private BungeeSuite plugin;

	public ignore(BungeeSuite bungeeSuite) {
		super(bungeeSuite.cignore);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length<1){
			arg0.sendMessage(ChatColor.RED+"/"+plugin.cignore+ " (player)");
			return;
		}
		ProxiedPlayer player  = plugin.getUtilities().getClosestPlayer(arg1[0]);
		if(player!=null){
			ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
			if(cp.ignoringPlayer(player.getName())){
				plugin.getUtilities().unignorePlayer(arg0.getName(), player.getName());
				arg0.sendMessage(plugin.PLAYER_UNIGNORED);
				return;
			}else{
				plugin.getUtilities().ignorePlayer(arg0.getName(), player.getName());
				arg0.sendMessage(plugin.PLAYER_IGNORED);
				return;
			}
		}else{
			arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
		}
	}

}
