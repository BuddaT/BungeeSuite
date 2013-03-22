package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class delete extends Command {
	BungeeSuite plugin;
	public delete(BungeeSuite bungeeSuite) {
		super(bungeeSuite.delete);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length==0){//current
			ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
			ChatChannel current = cp.getCurrent();
			if(arg0.getName().equalsIgnoreCase(current.getOwner())|| arg0.hasPermission("BungeeSuite.admin")){
				plugin.getUtilities().deleteChannel(current.getName());
				arg0.sendMessage(ChatColor.DARK_GREEN+"Channel deleted");
				return;
			}else{
				arg0.sendMessage(ChatColor.RED+"You do not have permission to delete this channel");
				return;
			}
		}else if(arg1.length==1){//other
			ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
			ChatChannel cc = plugin.getChannel(arg1[0]);
			try {
				if(!plugin.getUtilities().chatChannelExists(arg1[0])){
					arg0.sendMessage(ChatColor.RED+"This channel does not  exist");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(cc!=null && (arg0.getName().equalsIgnoreCase(cc.getOwner())|| arg0.hasPermission("BungeeSuite.admin"))){
				plugin.getUtilities().deleteChannel(cc.getName());
				arg0.sendMessage(ChatColor.DARK_GREEN+"Channel deleted");
				return;
			}else{
				arg0.sendMessage(ChatColor.RED+"You do not have permission to delete this channel");
				return;
			}
		}else{//wrong
			arg0.sendMessage(ChatColor.RED+"/Delete *(Channel Name)");
			return;
		}
		

	}

}
