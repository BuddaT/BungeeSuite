package com.mcdimensions.BungeeSuite.utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

public class ColorLog {
	//still needs fixing basic for now
	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String DARKBLUE = "\u001B[34m";
	public static final String DARKGREEN = "\u001B[32m";
	public static final String DARKCYAN = "\u001B[36m";
	public static final String DARKRED = "\u001B[31m";
	public static final String DARKMAGENTA = "\u001B[35m";
	public static final String DARKYELLOW="\u001B[33m";
	//dark/light still same
	public static final String GREY = "\u001B[37m";
	public static final String DARKGREY = "\u001B[30m";
	public static final String BLUE = "\u001B[34m";
	public static final String GREEN  = "\u001B[32m";
	public static final String CYAN = "\u001B[36m";
	public static final String RED = "\u001B[31m";
	public static final String MAGENTA = "\u001B[35m";
	public static final String YELLOW = "\u001B[33m";
	public static final String WHITE =  "\u001B[37m";
	
	ProxyServer proxy;
	

	public ColorLog(ProxyServer proxy) {
		this.proxy = proxy;
	}


	public void cLog(String string){
		String out = "";
		out = string.replace("&0", BLACK);
		out = out.replace("&1", DARKBLUE);
		out = out.replace("&2", DARKGREEN);
		out = out.replace("&3", DARKCYAN);
		out = out.replace("&4", DARKRED);
		out = out.replace("&5", DARKMAGENTA);
		out = out.replace("&6", DARKYELLOW);
		out = out.replace("&7", GREY);
		out = out.replace("&8", DARKGREY);
		out = out.replace("&9", BLUE);
		out = out.replace("&a", GREEN);
		out = out.replace("&b", CYAN);
		out = out.replace("&c", RED);
		out = out.replace("&d", MAGENTA);
		out = out.replace("&e", YELLOW);
		out = out.replace("&f", WHITE);
		proxy.getLogger().info(out+=RESET);
		
	}
	public void log(String string){
		String message=ChatColor.stripColor(string);
		proxy.getLogger().info(message);
		
	}

}
