package com.mcdimensions.BungeeSuite;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class BungeeSuiteCommand extends Command {

	private BungeeSuite plugin;

	public BungeeSuiteCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.bungeeSuiteCommand);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if (!arg0.hasPermission("BungeeSuite.admin")) {
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (arg1.length == 1) {
			if (arg1[0].equalsIgnoreCase("reload")) {
				plugin.reload();
				arg0.sendMessage("All configs for BungeeSuite reloaded");
			}
		}

	}

}
