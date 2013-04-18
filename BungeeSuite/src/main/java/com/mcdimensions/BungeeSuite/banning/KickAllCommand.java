package com.mcdimensions.BungeeSuite.banning;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickAllCommand extends Command {

	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.ban.kickall", "bungeesuite.ban.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };

	public KickAllCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.kickall);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		String kmessage = plugin.DEFAULT_KICK_PLAYER;
		kmessage.replace("%sender", sender.getName());
		if (arg1.length > 0) {
			kmessage = "";
			for (String data : arg1) {
				kmessage += data + " ";
			}
		}

		for (ProxiedPlayer data : plugin.getProxy().getPlayers())
			data.disconnect(kmessage);
	}

}
