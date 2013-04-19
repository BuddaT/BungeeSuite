package com.mcdimensions.BungeeSuite.warps;

import java.sql.SQLException;

import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.utilities.CommandUtil;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ListWarpsCommand extends Command {
	
	BungeeSuite plugin;
	private static final String[] PERMISSION_NODES = { "bungeesuite.warp.list", "bungeesuite.warp.*",
		"bungeesuite.mod", "bungeesuite.admin", "bungeesuite.*" };
	
	private static final String[] PERMISSION_NODES_OVERRIDE = { "bungeesuite.warp.list.admin",
		"bungeesuite.admin", "bungeesuite.*" };

	public ListWarpsCommand(BungeeSuite bungeeSuite) {
		super(bungeeSuite.warpsList, null, bungeeSuite.warpsc);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender sender, String[] arg1) {
		if (!CommandUtil.hasPermission(sender, PERMISSION_NODES)) {
			sender.sendMessage(plugin.NO_PERMISSION);
			return;
		}
		
		String[] list = null;
		
		try {
			list = plugin.getUtilities().getWarpList(sender);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sender.sendMessage(list[0]);
		if (CommandUtil.hasPermission(sender, PERMISSION_NODES_OVERRIDE)) {
			sender.sendMessage(list[1]);
		}
	}

}
