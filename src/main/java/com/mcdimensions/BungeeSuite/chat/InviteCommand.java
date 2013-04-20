package com.mcdimensions.BungeeSuite.chat;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class InviteCommand extends Command {
	
	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.channels", "bungeesuite.chat.*",
		"bungeesuite.chat.basic", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };
	
	private static final String[] PERMISSION_NODES_OVERRIDE = { "bungeesuite.chat.channels.override",
		"bungeesuite.admin", "bungeesuite.*" };
	
	public InviteCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.invite);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		if (arg1.length == 1) {
			String name = sender.getName();
			String player = arg1[0];
			ChatPlayer cp = plugin.getChatPlayer(name);
			ChatChannel cur = cp.getCurrentChannel();
			if(cur.isServerChannel()){
				sender.sendMessage(plugin.CHANNEL_INVITE_NOPERM);
				return;
			}
			if (cur.getOwner().equalsIgnoreCase(name) || CommandUtil.hasPermission(sender, PERMISSION_NODES_OVERRIDE)) {
				ProxiedPlayer pp = plugin.getUtilities().getClosestPlayer(player);
				inviteToChannel(cur, pp, sender);
			} else {
				String pmsg = plugin.CHANNEL_INVITE_NOPERM;
				pmsg = pmsg.replace("%channel", cur.getName());
				sender.sendMessage(pmsg);
			}
		} else if (arg1.length == 2) {
			try {
				if (plugin.getUtilities().chatChannelExists(arg1[1])) {
					ChatChannel cc = plugin.getChannel(arg1[1]);
					if(cc.isServerChannel()){
						sender.sendMessage(plugin.CHANNEL_INVITE_NOPERM);
						return;
					}
					if (cc.getOwner().equalsIgnoreCase(sender.getName()) || CommandUtil.hasPermission(sender, PERMISSION_NODES_OVERRIDE)) {
						ProxiedPlayer pp = plugin.getUtilities().getClosestPlayer(arg1[0]);
						inviteToChannel(cc, pp, sender);
					} else {
						String pmsg = plugin.CHANNEL_INVITE_NOPERM;
						pmsg = pmsg.replace("%channel", cc.getName());
						sender.sendMessage(pmsg);
					}
				} else {
					sender.sendMessage(plugin.CHANNEL_NOT_EXIST);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			sender.sendMessage(ChatColor.RED + "/" + plugin.invite + " (player) [channel]");
		}
	}
	
	private void inviteToChannel(ChatChannel channel, ProxiedPlayer player, CommandSender sender) {
		if (player != null) {
			if (channel.containsMember(player.getName())) {
				String msg = plugin.CHANNEL_IS_MEMBER;
				msg = msg.replace("%player", player.getName());
				msg = msg.replace("%channel", channel.getName());
				sender.sendMessage(msg);
				return;
			}
			
			channel.invitePlayer(player.getName());

			String pmsg = plugin.PLAYER_INVITE;
			pmsg = pmsg.replace("%channel", channel.getName());
			pmsg = pmsg.replace("%sender", sender.getName());
			player.sendMessage(pmsg);

			String imsg = plugin.PLAYER_INVITED;
			imsg = imsg.replace("%player", player.getName());
			imsg = imsg.replace("%channel", channel.getName());
			sender.sendMessage(imsg);
		} else
			sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
	}

}
