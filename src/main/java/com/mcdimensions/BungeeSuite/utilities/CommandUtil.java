package com.mcdimensions.BungeeSuite.utilities;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class CommandUtil {

	public static boolean hasPermission(CommandSender sender, String[] permissions) {
		for (String perm : permissions)
			if (sender.hasPermission(perm))
				return true;
		
		return false;
	}
	public static boolean hasPermission(String sender, String[] permissions) {
		for (String perm : permissions)
			if (ProxyServer.getInstance().getPlayer(sender)!=null && ProxyServer.getInstance().getPlayer(sender).hasPermission(perm))
				return true;
		
		return false;
	}
}
