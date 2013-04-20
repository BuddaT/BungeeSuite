package com.mcdimensions.BungeeSuite.listeners;

import java.sql.SQLException;
import java.util.Calendar;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;

public class BanListener implements Listener {

	BungeeSuite plugin;
	
	public BanListener(BungeeSuite bungeeSuite) {
		this.plugin = bungeeSuite;
	}
	
	
	@Subscribe
	public void login(LoginEvent event) throws SQLException {
		if(!plugin.bansEnabled)return;
		if(plugin.IPbans.contains(event.getConnection().getAddress().getAddress().toString())){
			event.setCancelReason("Your IP is banned from this server, please appeal on the server website");
			event.setCancelled(true);
		}
		if(plugin.playerBans.containsKey(event.getConnection().getName())){
			Calendar date = plugin.playerBans.get(event.getConnection().getName());
			Calendar now = Calendar.getInstance();
			if(now.before(date)){
				double hours =((((date.getTimeInMillis()-now.getTimeInMillis())/1000)/60)/60);
				event.setCancelReason("You have been temporarilly banned until " +date.getTime() + " ("+hours+")Hours");
				event.setCancelled(true);
				return;
			}if(now.after(date)){
				plugin.playerBans.remove(event.getConnection().getName());
				return;
			}else{
				event.setCancelReason("You are banned from this server, please appeal on the server website");
				event.setCancelled(true);
				return;
			}
		}else{
			return;
		}
	}
	

}
