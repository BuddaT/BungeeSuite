package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class nickname extends Command {
	BungeeSuite plugin;

	public nickname(BungeeSuite bungeeSuite) {
		super(bungeeSuite.nickname);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!(arg0.hasPermission("BungeeSuite.nick")
				|| arg0.hasPermission("BungeeSuite.admin") || arg0
					.hasPermission("BungeeSuite.nickcolored"))) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		if (arg1.length == 1) {
			String nick = arg1[0];
			if (!(arg0.hasPermission("BungeeSuite.nickcolored") || arg0
					.hasPermission("BungeeSuite.mod")) && arg1[0].contains("&")) {
				arg0.sendMessage(plugin.NO_PERMISSION_COLOR);
				return;
			}
			if (nick.length() > 16) {
				nick = nick.substring(0, 16);
			}
			try {
				ChatPlayer cp = plugin.getChatPlayer(arg0.getName());
				cp.setDisplayName(nick);
		String pmsg = plugin.PLAYER_NICKNAME_CHANGE;
		pmsg = pmsg.replace("%nickname", plugin.getUtilities().colorSub(nick));
		pmsg = pmsg.replace("%player", cp.getName());
		pmsg = pmsg.replace("%sender", arg0.getName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return;
		}
		if (arg1.length > 1) {
			if (arg0.hasPermission("BungeeSuite.admin")) {
				try {
					if (plugin.getUtilities().playerExists(arg1[0])) {
						String nick = arg1[1];
						if (nick.length() > 16) {
							nick = nick.substring(0, 16);
						}
						ProxiedPlayer cp = plugin.getUtilities()
								.getClosestPlayer(arg1[0]);
						if (cp == null) {
							arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
							return;
						}
						if (plugin.onlinePlayers.containsKey(cp.getName())) {
							plugin.onlinePlayers.get(cp.getName())
									.setDisplayName(nick);
							plugin.getUtilities().setNickName(cp.getName(),
									nick);
							String nmsg = plugin.PLAYER_NICKNAMED;
							nmsg = nmsg.replace("%nickname", nick);
							nmsg = nmsg.replace("%player", cp.getName());
							arg0.sendMessage(nmsg);
							String pmsg = plugin.PLAYER_NICKNAME_CHANGE;
							pmsg = pmsg.replace("%nickname", nick);
							pmsg = pmsg.replace("%player", cp.getName());
							pmsg = pmsg.replace("%sender", arg0.getName());
							cp.sendMessage(pmsg);
						} else {
							plugin.getUtilities().setNickName(cp.getName(),
									nick);
							String nmsg = plugin.PLAYER_NICKNAMED;
							nmsg = nmsg.replace("%nickname", nick);
							nmsg = nmsg.replace("%player", cp.getName());
							arg0.sendMessage(nmsg);
						}
					} else {
						arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				arg0.sendMessage(plugin.NO_PERMISSION);
				return;
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "/" + plugin.nickname
					+ " (nickname/*player) *(player)");
		}
	}

}
