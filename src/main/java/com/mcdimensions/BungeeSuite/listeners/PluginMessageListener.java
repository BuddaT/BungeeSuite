package com.mcdimensions.BungeeSuite.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.google.common.eventbus.Subscribe;
import com.mcdimensions.BungeeSuite.BungeeSuite;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;

public class PluginMessageListener implements Listener {
	BungeeSuite plugin;

	public PluginMessageListener(BungeeSuite bungeePocketKnife) {
		this.plugin = bungeePocketKnife;
	}

	@Subscribe
	public void recievePluginMessage(PluginMessageEvent event)
			throws IOException, SQLException {
		if (!event.getTag().equalsIgnoreCase(BungeeSuite.PLUGIN_NAME))
			return;
		
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
		String channel = in.readUTF();

		if (channel.equalsIgnoreCase("warp")) {
			if (!plugin.warpsEnabled) {
				return;
			}
			
			String warpname = in.readUTF();
			String playername = in.readUTF();
			if (plugin.warpList.containsKey(warpname)) {
				plugin.warpList.get(warpname).warpPlayer(plugin.getProxy().getPlayer(playername));
				return;
			}
			
			if (plugin.getUtilities().warpExists(warpname)) {
				plugin.getUtilities().loadWarp(warpname).warpPlayer(plugin.getProxy().getPlayer(playername));
				return;
			}
			
		} else if (channel.equalsIgnoreCase("PrefixSuffix")) {
			String playername = in.readUTF();
			ChatPlayer cp = plugin.getChatPlayer(playername);
			
			if(cp==null)return;
			
			cp.setPrefix(in.readUTF());
			cp.setSuffix(in.readUTF());
		} else if (channel.equalsIgnoreCase("BouncePlayer")) {
			String playername = in.readUTF();
			String server = in.readUTF();
			String message = in.readUTF();
			
			plugin.getProxy().getPlayer(playername).connect(plugin.getProxy().getServerInfo(server));
			plugin.getProxy().getPlayer(playername).sendMessage(message);
		} else if (channel.equalsIgnoreCase("JoinEvent")) {
			String playername = in.readUTF();
			
			if (plugin.warpsEnabled) {
				if (plugin.warped.containsKey(playername)) {
					plugin.warped.get(playername).warpPlayer(
							plugin.getProxy().getPlayer(playername));
					plugin.warped.remove(playername);
					return;
				}
			}
			
			if (!plugin.teleportsEnabled) {
				return;
			}
			
			ProxiedPlayer player = plugin.getProxy().getPlayer(playername);
			if (plugin.teleportsPending.containsKey(player)) {
				plugin.getUtilities().teleportToPlayer(player,
						plugin.teleportsPending.get(player));
				plugin.teleportsPending.remove(player);
				return;
			}
		}

		return;
	}

}
