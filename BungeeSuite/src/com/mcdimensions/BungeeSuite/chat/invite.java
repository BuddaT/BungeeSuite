package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class invite extends Command {
	BungeeSuite plugin;
	public invite(BungeeSuite bungeeSuite) {
	super(bungeeSuite.invite);
		this.plugin=bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length==1){//invite to current channel if op or owner
			String name = arg0.getName();
			String player = arg1[0];
			ChatPlayer cp = plugin.getChatPlayer(name);
			ChatChannel cur = cp.getCurrent();
			if(cur.getOwner().equalsIgnoreCase(name) || arg0.hasPermission("BungeeSuite.admin")){//is owner or admin
				ProxiedPlayer pp =plugin.getUtilities().getClosestPlayer(player);
				if(pp!=null){//if player is online
				cur.invitePlayer(pp.getName());
				if(cur.containsMember(cp.getName())){
					arg0.sendMessage(ChatColor.RED+"That player is already in this channel");
					return;
				}
				pp.sendMessage(ChatColor.DARK_AQUA+"You have been invited to the "+ cur.getName()+ " channel. Type /accept "+ cur.getName()+ " to join.");
				arg0.sendMessage(ChatColor.DARK_GREEN+pp.getDisplayName()+" Invited to current channel");
				return;
				}else{//player offline or does not exist
					arg0.sendMessage(ChatColor.RED+"That player is not online");
					return;
				}
			}else{//no permission to invite to current channel
				arg0.sendMessage(ChatColor.RED+"You do not have permission to invite players to your current channel");
				return;
			}
		}else if(arg1.length==2){//invite to a specific channel
			try {
				if(plugin.getUtilities().chatChannelExists(arg1[1])){//channel exists
					ChatChannel cc = plugin.getChannel(arg1[1]);
					if(cc.getOwner().equalsIgnoreCase(arg0.getName())|| arg0.hasPermission("BungeeSuite.admin")){//has permission
						ProxiedPlayer pp = plugin.getUtilities().getClosestPlayer(arg1[0]);
						if(pp!=null){//player is online
							cc.invitePlayer(pp.getName());
							pp.sendMessage(ChatColor.DARK_AQUA+"You have been invited to the "+ cc.getName()+ " channel. Type /join "+ cc.getName()+ " to join.");
							arg0.sendMessage(ChatColor.DARK_GREEN+pp.getDisplayName()+" Invited to channel "+ cc.getName());
							return;
						}else{//player not online
							arg0.sendMessage(ChatColor.RED+"That player is not online");
						}
					}else{//no permission
						arg0.sendMessage(ChatColor.RED+"You do not have permission to invite players to "+ cc.getName());
						return;
					}
				}else{//channel doesnt exist
					arg0.sendMessage(ChatColor.RED+"Channel does not exist");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			arg0.sendMessage(ChatColor.RED+"/invite(player) *(channel)");
		}

	}

}
