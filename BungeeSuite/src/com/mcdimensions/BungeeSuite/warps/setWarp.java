package com.mcdimensions.BungeeSuite.warps;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class setWarp extends Command {

	private BungeeSuite plugin;

	public setWarp(BungeeSuite bungeeSuite) {
		super(bungeeSuite.setwarp);
		plugin = bungeeSuite;
	}

	@Override
	public void execute(CommandSender arg0, String[] arg1) {
		if(!arg0.hasPermission("BungeeSuite.admin")){
			arg0.sendMessage(plugin.NO_PERMISSION);
			return;
		}
			if(arg1.length<1){
				arg0.sendMessage(ChatColor.RED+"/"+plugin.setwarp+" (name) *(private)");
				return;
			}
			String name = arg1[0];
			//sends plugin message to BungeeSuiteBukkit to get player pos
			ProxiedPlayer player = (ProxiedPlayer) arg0;
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try {
				out.writeUTF("SetWarp");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.writeUTF(arg0.getName());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				out.writeUTF(name);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(arg1.length>1 && arg1[1].equalsIgnoreCase("private")){
				try {
					out.writeBoolean(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				try {
					out.writeBoolean(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			player.getServer().sendData("BungeeSuiteMC", b.toByteArray());
		}

	}

