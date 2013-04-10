package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class channel extends Command {
	BungeeSuite plugin;
	public channel(BungeeSuite bungeeSuite) {
		super(bungeeSuite.channel);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(arg1.length==0 || arg1[0].equalsIgnoreCase("help")){
			arg0.sendMessage(ChatColor.DARK_GREEN+"----Channel help----");
			arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" kick (player)"+ ChatColor.GOLD+"- Kicks player from channel");
			arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" format"+ ChatColor.GOLD+"- Displays channel format");
			arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" editformat (format)"+ ChatColor.GOLD+"- edits channel format");
			arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" rename (name)"+ ChatColor.GOLD+"- Renames the channel");
			arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" setowner (player)"+ ChatColor.GOLD+"- Changes the owner of the channel");
			arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" members *(channel) "+ ChatColor.GOLD+"- Displays channel members");
			return;
		}
		ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
		ChatChannel cc = cp.getCurrent();
		if(!(arg0.hasPermission("BungeeSuite.admin")|| arg0.getName().equalsIgnoreCase(cc.getOwner()))){
			arg0.sendMessage(plugin.CHANNEL_NO_PERMISSION);
			return;
		}
		if(arg1[0].equalsIgnoreCase("kick")){
			if(arg1.length<2){
				arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" kick (player)");
				return;
			}
			String player = arg1[1];
			if(cc.containsMember(player)){
				cc.removeMember(player);
				plugin.getChatPlayer(player).removeChannel(cc.getName());
			}else{
				arg0.sendMessage(plugin.CHANNEL_NOT_MEMBER);
				return;
			}
			arg0.sendMessage(ChatColor.DARK_GREEN+"Channel renamed");
			return;
		}
		if(arg1[0].equalsIgnoreCase("format")){
			arg0.sendMessage(ChatColor.GOLD+"Channel format: "+ ChatColor.RESET+ cc.getFormat());
			return;
		}
		if(arg1[0].equalsIgnoreCase("editformat")){
			if(arg1.length<2){
				arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" editformat (format)");
				return;
			}
			String format ="";
			for(String data: arg1){
				if(!data.equalsIgnoreCase(arg1[0])){
					format +=data+" ";
				}
			}
			cc.reformat(format);
			arg0.sendMessage(ChatColor.DARK_GREEN+"Channel format changed");
			return;
		}
		if(arg1[0].equalsIgnoreCase("rename")){
			if(arg1.length<2){
				arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" rename (name)");
				return;
			}
			String name = arg1[1];
			cc.rename(name);
			arg0.sendMessage(ChatColor.DARK_GREEN+"Channel renamed");
			return;
		}
		if(arg1[0].equalsIgnoreCase("setowner")){
			if(arg1.length<2){
				arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" setowner (name)");
				return;
			}
			String name = arg1[1];
			ChatPlayer player = plugin.getChatPlayer(name);
			if(player==null){
				arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
				return;
			}
			if(!cc.containsMember(player.getName())){
				arg0.sendMessage(plugin.CHANNEL_NOT_MEMBER);
				return;
			}
			if(player.getChannelsOwned()>=plugin.maxCustomChannels){
				arg0.sendMessage(plugin.CHANNEL_TOO_MANY);
				return;
			}
			cc.setOwner(player);
			arg0.sendMessage(ChatColor.DARK_GREEN+"Owner changed");
			return;
		}
		if(arg1[0].equalsIgnoreCase("members")){
			String playerList = ChatColor.DARK_AQUA+"Members: "+ChatColor.WHITE;
			for(String data: cc.members){
				playerList+=data+", ";
			}
			arg0.sendMessage(playerList);
			return;
		}	
		arg0.sendMessage(ChatColor.DARK_GREEN+"----Channel help----");
		arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" kick (player)"+ ChatColor.GOLD+"- Kicks player from channel");
		arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" format"+ ChatColor.GOLD+"- Displays channel format");
		arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" editformat (format)"+ ChatColor.GOLD+"- edits channel format");
		arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" rename (name)"+ ChatColor.GOLD+"- Renames the channel");
		arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" setowner (player)"+ ChatColor.GOLD+"- Changes the owner of the channel");
		arg0.sendMessage(ChatColor.RED+"/"+plugin.channel+" members *(channel) "+ ChatColor.GOLD+"- Displays channel members");
		return;
		
	}

}
