package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class leavechannel extends Command {
	BungeeSuite plugin;
	public leavechannel(BungeeSuite bungeeSuite) {
		super(bungeeSuite.leavechannel);
		this.plugin = bungeeSuite;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length==0){//leave current if not server or global
			ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
			ChatChannel cc= cp.getCurrent();
			if(cc.isServerChannel()){
				cp.sendMessage(ChatColor.RED+"Can not leave this channel");
				return;
			}else if(cc.getOwner().equalsIgnoreCase(cp.getName())){
			cp.sendMessage(ChatColor.RED+"You can not leave this channel as you are the owner use /delete (channel) instead");	
			}
				else{
				cc.removeMember(cp.getName());
				cp.removeChannel(cc.getName());
				if(plugin.globalDefault){
					cp.setCurrent(plugin.getChannel("Global"));
					return;
				}else{
				ChatChannel newc = plugin.getChannel(cp.getPlayer().getServer().getInfo().getName());
				newc.addMember(cp);
				cp.setCurrent(newc);
				return;
				}
			}
		}else if(arg1.length==1){
			String channelName = arg1[0];
			try {
				if(plugin.getUtilities().chatChannelExists(channelName)){
					ChatChannel cc = plugin.getChannel(channelName);
					ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
					if(cc.isServerChannel()){
						cp.sendMessage(ChatColor.RED+"Can not leave this channel");
						return;
					}
					ChatChannel cur = cp.getCurrent();
					if(cc.equals(cur)){
						cc.removeMember(cp.getName());
						cp.removeChannel(cc.getName());
						ChatChannel newc = plugin.getChannel(cp.getPlayer().getServer().getInfo().getName());
						newc.addMember(cp);
						cp.setCurrent(newc);
					}else{
						cc.removeMember(cp.getName());
						cp.removeChannel(cc.getName());
					}
				}else{
					arg0.sendMessage(ChatColor.RED+"That channel does not exist");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			arg0.sendMessage(ChatColor.RED+"/leave (*ChannelName)");
			return;
		}

	}

}
