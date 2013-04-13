package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class IgnoreCommand extends Command {

	private BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.chat.ignore", "bungeesuite.chat.basic",
		"bungeesuite.chat.*", "bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public IgnoreCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.cignore);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.cignore + " (player)");
			return;
		}

		ProxiedPlayer player = plugin.getUtilities().getClosestPlayer(arg1[0]);
		if (player != null) {
			ChatPlayer cp = plugin.getChatPlayer(sender.getName());

			if (cp.ignoringPlayer(player.getName())) {
				plugin.getUtilities().unignorePlayer(sender.getName(), player.getName());
				sender.sendMessage(plugin.PLAYER_UNIGNORED);
			} else {
				plugin.getUtilities().ignorePlayer(sender.getName(), player.getName());
				sender.sendMessage(plugin.PLAYER_IGNORED);
			}
		} else {
			sender.sendMessage(plugin.PLAYER_NOT_ONLINE);
		}
	}

}
