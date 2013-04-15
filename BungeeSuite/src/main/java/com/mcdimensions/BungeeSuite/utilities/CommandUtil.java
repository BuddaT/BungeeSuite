package com.mcdimensions.BungeeSuite.utilities;

import net.md_5.bungee.api.CommandSender;

public class CommandUtil {

	public static boolean hasPermission(CommandSender sender, String[] permissions) {
		for (String perm : permissions)
			if (sender.hasPermission(perm))
				return true;
		
		return false;
	}
}
