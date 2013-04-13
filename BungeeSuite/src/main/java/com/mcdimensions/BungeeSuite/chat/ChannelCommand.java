package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ChannelCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.channels", "bungeesuite.chat.*",
		"bungeesuite.chat.basic", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public ChannelCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.channel, null, bungeeSuite.c);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ChatPlayer cp = plugin.getChatPlayer(sender.getName());
		ChatChannel cc = cp.getCurrent();
		
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES) || !sender.getName().equalsIgnoreCase(cc.getOwner())) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			sendHelp(sender);
			return;
		}

		String command = args[0];

		if (command.equalsIgnoreCase("kick")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " kick (player)");
				return;
			}
			
			String player = args[1];
			if (cc.containsMember(player)) {
				cc.removeMember(player);
				plugin.getChatPlayer(player).removeChannel(cc.getName());
			} else {
				sender.sendMessage(plugin.CHANNEL_NOT_MEMBER);
				return;
			}
			
			sender.sendMessage(ChatColor.DARK_GREEN + player + "kicked from channel");

		} else if (command.equalsIgnoreCase("format")) {
			sender.sendMessage(ChatColor.GOLD + "Channel format: " + ChatColor.RESET + cc.getFormat());
			
		} else if (command.equalsIgnoreCase("editformat")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " editformat (format)");
				return;
			}
			StringBuilder format = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				format.append(args[i]);
				format.append(" ");
			}
			cc.reformat(format.toString());
			sender.sendMessage(ChatColor.DARK_GREEN + "Channel format changed");

		} else if (command.equalsIgnoreCase("rename")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " rename (name)");
				return;
			}
			String name = args[1];
			cc.rename(name);
			sender.sendMessage(ChatColor.DARK_GREEN + "Channel renamed");

		} else if (command.equalsIgnoreCase("setowner")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " setowner (name)");
				return;
			}
			String name = args[1];
			ChatPlayer player = plugin.getChatPlayer(name);
			if (player == null) {
				sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
				return;
			}
			if (!cc.containsMember(player.getName())) {
				sender.sendMessage(plugin.CHANNEL_NOT_MEMBER);
				return;
			}
			if (player.getChannelsOwned() >= plugin.maxCustomChannels) {
				sender.sendMessage(plugin.CHANNEL_TOO_MANY);
				return;
			}
			cc.setOwner(player);
			sender.sendMessage(ChatColor.DARK_GREEN + "Owner changed");

		} else if (command.equalsIgnoreCase("members")) {
			String playerList = ChatColor.DARK_AQUA + "Members: " + ChatColor.WHITE;
			
			for (String data : cc.members) {
				playerList += data + ", ";
			}
			sender.sendMessage(playerList);
		} else {
			sendHelp(sender);
		}
	}

	private void sendHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_GREEN + "----Channel help----");
		sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " kick (player)" + ChatColor.GOLD
				+ "- Kicks player from channel");
		sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " format"
				+ ChatColor.GOLD + "- Displays channel format");
		sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " editformat (format)" + ChatColor.GOLD
				+ "- edits channel format");
		sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " rename (name)" + ChatColor.GOLD + "- Renames the channel");
		sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " setowner (player)" + ChatColor.GOLD
				+ "- Changes the owner of the channel");
		sender.sendMessage(ChatColor.RED + "/" + plugin.channel + " members *(channel) " + ChatColor.GOLD
				+ "- Displays channel members");
	}

}
