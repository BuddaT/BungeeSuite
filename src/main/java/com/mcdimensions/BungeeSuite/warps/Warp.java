package com.mcdimensions.BungeeSuite.warps;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Warp {
	WarpLocation wl;
	boolean visable;

	public Warp(String name, WarpLocation wl, boolean vis) {
		this.wl = wl;
		this.visable = vis;

	}

	public boolean warpPlayer(ProxiedPlayer player) throws IOException {
		ServerInfo CurrentServer = player.getServer().getInfo();
		ServerInfo TargetServer = ProxyServer.getInstance().getServerInfo(
				wl.getServer());

		if (CurrentServer.equals(TargetServer)) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream o = new DataOutputStream(b);

			try {
				o.writeUTF("Warp");
				o.writeUTF(player.getName());
				o.writeUTF(wl.serialize()); // Target Server
			} catch (IOException e) {
				// Can never happen
			}
			player.getServer().sendData("BungeeSuiteMC", b.toByteArray());
			return true;
		} else {
			// teleport player to right server then send location once loaded
			BungeeSuite plugin = (BungeeSuite) ProxyServer.getInstance()
					.getPluginManager().getPlugin("BungeeSuite");
			plugin.warped.put(player.getName(), this);
			player.connect(TargetServer);
			return false;
		}
	}
}
