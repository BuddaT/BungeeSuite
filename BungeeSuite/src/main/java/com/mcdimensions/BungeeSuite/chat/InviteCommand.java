package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class InviteCommand extends Command {
	BungeeSuite plugin;

	public InviteCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.invite);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (arg1.length == 1) {// invite to current channel if op or owner
			String name = arg0.getName();
			String player = arg1[0];
			ChatPlayer cp = plugin.getChatPlayer(name);
			ChatChannel cur = cp.getCurrent();

			if (cur.getOwner().equalsIgnoreCase(name) || arg0.hasPermission("BungeeSuite.admin")) {
				ProxiedPlayer pp = plugin.getUtilities().getClosestPlayer(player);
				if (pp != null) {
					cur.invitePlayer(pp.getName());
					if (cur.containsMember(cp.getName())) {
						String msg = plugin.CHANNEL_IS_MEMBER;
						msg = msg.replace("%player", pp.getName());
						msg = msg.replace("%channel", cur.getName());
						arg0.sendMessage(msg);
						return;
					}
					
					String pmsg = plugin.PLAYER_INVITE;
					pmsg = pmsg.replace("%channel", cur.getName());
					pmsg = pmsg.replace("%sender", arg0.getName());
					pp.sendMessage(pmsg);
					
					String imsg = plugin.PLAYER_INVITED;
					imsg = imsg.replace("%player", pp.getName());
					imsg = imsg.replace("%channel", cur.getName());
					arg0.sendMessage(imsg);
					
					return;
				} else {
					arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
					return;
				}
			} else {
				String pmsg = plugin.CHANNEL_INVITE_NOPERM;
				pmsg = pmsg.replace("%channel", cur.getName());
				arg0.sendMessage(pmsg);
				return;
			}
		} else if (arg1.length == 2) {// invite to a specific channel
			try {
				if (plugin.getUtilities().chatChannelExists(arg1[1])) {
					ChatChannel cc = plugin.getChannel(arg1[1]);
					if (cc.getOwner().equalsIgnoreCase(arg0.getName()) || arg0.hasPermission("BungeeSuite.admin")) {
						ProxiedPlayer pp = plugin.getUtilities().getClosestPlayer(arg1[0]);
						if (pp != null) {
							if (cc.containsMember(pp.getName())) {
								String msg = plugin.CHANNEL_IS_MEMBER;
								msg = msg.replace("%player", pp.getName());
								msg = msg.replace("%channel", cc.getName());
								arg0.sendMessage(msg);
								return;
							}
							cc.invitePlayer(pp.getName());
							
							String pmsg = plugin.PLAYER_INVITE;
							pmsg = pmsg.replace("%channel", cc.getName());
							pmsg = pmsg.replace("%sender", arg0.getName());
							pp.sendMessage(pmsg);
							
							String imsg = plugin.PLAYER_INVITED;
							imsg = imsg.replace("%player", pp.getName());
							imsg = imsg.replace("%channel", cc.getName());
							arg0.sendMessage(imsg);
							
							return;
						} else {
							arg0.sendMessage(plugin.PLAYER_NOT_ONLINE);
						}
					} else {
						arg0.sendMessage(plugin.CHANNEL_INVITE_NOPERM);
						return;
					}
				} else {
					arg0.sendMessage(plugin.CHANNEL_NOT_EXIST);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			arg0.sendMessage(ChatColor.RED + "/" + plugin.invite + " (player) *(channel)");
		}
	}

}
