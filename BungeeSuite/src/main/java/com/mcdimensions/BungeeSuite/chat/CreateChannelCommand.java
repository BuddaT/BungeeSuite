package com.mcdimensions.BungeeSuite.chat;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CreateChannelCommand extends Command {

	private BungeeSuite plugin;

	public CreateChannelCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.createChannel, null, bungeeSuite.create);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!(sender.hasPermission("BungeeSuite.createchannel") || sender.hasPermission("BungeeSuite.admin"))) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}

		if (plugin.getChatPlayer(sender.getName()).getChannelsOwned() >= plugin.maxCustomChannels
				&& !sender.hasPermission("BungeeSuite.admin")) {
			sender.sendMessage(plugin.CHANNEL_TOO_MANY);
			return;
		}

		if (arg1.length == 1) {
			String name = arg1[0];
			plugin.getUtilities().createChannel(name, plugin.defaultCustomChannelFormat, false, sender.getName());
			
			sender.sendMessage(plugin.CHANNEL_CREATE_CONFIRM);
		} else if (arg1.length == 2) {
			String name = arg1[0];
			String format = arg1[1];
			plugin.getUtilities().createChannel(name, format, false, sender.getName());
			
			String chmsg = plugin.CHANNEL_CREATE_CONFIRM;
			chmsg = chmsg.replace("%channel", name);
			sender.sendMessage(chmsg);
		} else
			sender.sendMessage(ChatColor.RED + "/" + plugin.create + " (channel) [format]");
	}

}
