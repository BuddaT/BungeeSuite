package com.mcdimensions.BungeeSuite.warps;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class DeleteWarpCommand extends Command {

	BungeeSuite plugin;

	public DeleteWarpCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.delWarp);
		this.plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!sender.hasPermission("BungeeSuite.admin")) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		if (arg1.length < 1) {
			sender.sendMessage(ChatColor.RED + "/" + plugin.delWarp + " (warp name)");
			return;
		}
		
		try {
			if (plugin.getUtilities().warpExists(arg1[0])) {
				plugin.getUtilities().deleteWarp(arg1[0]);
				sender.sendMessage(plugin.WARP_DELETE_CONFIRM);
			} else {
				sender.sendMessage(plugin.WARP_NOT_EXIST);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
