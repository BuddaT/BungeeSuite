package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NicknameCommand extends Command {
	
	BungeeSuite plugin;
	public static final String[] PERMISSION_NODES = { "bungeesuite.chat.nick", 
		"bungeesuite.chat.nick.colored", "bungeesuite.nick", "bungeesuite.nickcolored", 
		"bungeesuite.chat.admin", "bungeesuite.admin", "bungeesuite.*" };
	
	public static final String[] PERMISSION_NODES_COLORED = { "bungeesuite.chat.nick.colored", 
		"bungeesuite.nickcolored", "bungeesuite.chat.admin", "bungeesuite.admin", "bungeesuite.*" };

	public static final String[] PERMISSION_NODES_OTHER = { "bungeesuite.chat.admin",
		"bungeesuite.admin", "bungeesuite.*" };
	
	public NicknameCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.nickname, null, bungeeSuite.nick);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length == 1) {
			String nick = arg1[0];
			
			if (!CommandUtil.hasPermission(sender, PERMISSION_NODES_COLORED) && arg1[0].contains("&")) {
				sender.sendMessage(plugin.NO_PERMISSION_COLOR);
				return;
			}
			
			if (nick.length() > 16)
				nick = nick.substring(0, 16);
			
			try {
				ChatPlayer cp = plugin.getChatPlayer(sender.getName());
				cp.setDisplayName(nick);
				
				String pmsg = plugin.PLAYER_NICKNAME_CHANGE;
				pmsg = pmsg.replace("%nickname", plugin.getUtilities().colorSub(nick));
				pmsg = pmsg.replace("%player", cp.getName());
				pmsg = pmsg.replace("%sender", sender.getName());
				sender.sendMessage(pmsg);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}
		
		if (arg1.length > 1) {
			if (CommandUtil.hasPermission(sender, PERMISSION_NODES_OTHER)) {
				try {
					if (plugin.getUtilities().playerExists(arg1[0])) {
						String nick = arg1[1];
						if (nick.length() > 16) {
							nick = nick.substring(0, 16);
						}
						
						ProxiedPlayer cp = plugin.getUtilities().getClosestPlayer(arg1[0]);
						if (cp == null) {
							sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
							return;
						}
						
						if (plugin.onlinePlayers.containsKey(cp.getName())) {
							plugin.onlinePlayers.get(cp.getName()).setDisplayName(nick);
							plugin.getUtilities().setNickName(cp.getName(), nick);
							
							String nmsg = plugin.PLAYER_NICKNAMED;
							nmsg = nmsg.replace("%nickname", nick);
							nmsg = nmsg.replace("%player", cp.getName());
							sender.sendMessage(nmsg);
							
							String pmsg = plugin.PLAYER_NICKNAME_CHANGE;
							pmsg = pmsg.replace("%nickname", nick);
							pmsg = pmsg.replace("%player", cp.getName());
							pmsg = pmsg.replace("%sender", sender.getName());
							cp.sendMessage(pmsg);
						} else {
							plugin.getUtilities().setNickName(cp.getName(),nick);
							
							String nmsg = plugin.PLAYER_NICKNAMED;
							nmsg = nmsg.replace("%nickname", nick);
							nmsg = nmsg.replace("%player", cp.getName());
							sender.sendMessage(nmsg);
						}
					} else {
						sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				sender.sendMessage(plugin.NO_PERMISSION);
				return;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "/" + plugin.nickname
					+ " (nickname) [player]");
		}
	}

}
