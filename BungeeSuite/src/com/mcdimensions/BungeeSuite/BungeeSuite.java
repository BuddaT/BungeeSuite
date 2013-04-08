package com.mcdimensions.BungeeSuite;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;


import com.mcdimensions.BungeeSuite.banning.ban;
import com.mcdimensions.BungeeSuite.banning.ipban;
import com.mcdimensions.BungeeSuite.banning.kick;
import com.mcdimensions.BungeeSuite.banning.kickall;
import com.mcdimensions.BungeeSuite.banning.pardon;
import com.mcdimensions.BungeeSuite.banning.tban;
import com.mcdimensions.BungeeSuite.banning.tempban;
import com.mcdimensions.BungeeSuite.banning.unban;
import com.mcdimensions.BungeeSuite.banning.unbanip;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;
import com.mcdimensions.BungeeSuite.chat.accept;
import com.mcdimensions.BungeeSuite.chat.broadcast;
import com.mcdimensions.BungeeSuite.chat.c;
import com.mcdimensions.BungeeSuite.chat.channel;
import com.mcdimensions.BungeeSuite.chat.chatspy;
import com.mcdimensions.BungeeSuite.chat.create;
import com.mcdimensions.BungeeSuite.chat.createchannel;
import com.mcdimensions.BungeeSuite.chat.delete;
import com.mcdimensions.BungeeSuite.chat.deletechannel;
import com.mcdimensions.BungeeSuite.chat.displayserver;
import com.mcdimensions.BungeeSuite.chat.g;
import com.mcdimensions.BungeeSuite.chat.global;
import com.mcdimensions.BungeeSuite.chat.invite;
import com.mcdimensions.BungeeSuite.chat.join;
import com.mcdimensions.BungeeSuite.chat.leave;
import com.mcdimensions.BungeeSuite.chat.leavechannel;
import com.mcdimensions.BungeeSuite.chat.message;
import com.mcdimensions.BungeeSuite.chat.msg;
import com.mcdimensions.BungeeSuite.chat.mute;
import com.mcdimensions.BungeeSuite.chat.muteall;
import com.mcdimensions.BungeeSuite.chat.nick;
import com.mcdimensions.BungeeSuite.chat.nickname;
import com.mcdimensions.BungeeSuite.chat.r;
import com.mcdimensions.BungeeSuite.chat.reply;
import com.mcdimensions.BungeeSuite.chat.t;
import com.mcdimensions.BungeeSuite.chat.tell;
import com.mcdimensions.BungeeSuite.chat.toggle;
import com.mcdimensions.BungeeSuite.chat.s;
import com.mcdimensions.BungeeSuite.chat.whisper;
import com.mcdimensions.BungeeSuite.chat.server;
import com.mcdimensions.BungeeSuite.config.Config;
import com.mcdimensions.BungeeSuite.listeners.BanListener;
import com.mcdimensions.BungeeSuite.listeners.ChatListener;
import com.mcdimensions.BungeeSuite.listeners.LoginMessages;
import com.mcdimensions.BungeeSuite.listeners.PluginMessageListener;
import com.mcdimensions.BungeeSuite.listeners.ServerLoginLogout;
import com.mcdimensions.BungeeSuite.portals.Portals;
import com.mcdimensions.BungeeSuite.portals.delportal;
import com.mcdimensions.BungeeSuite.portals.listPortals;
import com.mcdimensions.BungeeSuite.portals.setportal;
import com.mcdimensions.BungeeSuite.teleports.tp;
import com.mcdimensions.BungeeSuite.teleports.tpa;
import com.mcdimensions.BungeeSuite.teleports.tpaccept;
import com.mcdimensions.BungeeSuite.teleports.tpahere;
import com.mcdimensions.BungeeSuite.teleports.tpall;
import com.mcdimensions.BungeeSuite.teleports.tpdeny;
import com.mcdimensions.BungeeSuite.utilities.ColorLog;
import com.mcdimensions.BungeeSuite.utilities.SQL;
import com.mcdimensions.BungeeSuite.utilities.Utilities;
import com.mcdimensions.BungeeSuite.warps.Warp;
import com.mcdimensions.BungeeSuite.warps.WarpC;
import com.mcdimensions.BungeeSuite.warps.delWarp;
import com.mcdimensions.BungeeSuite.warps.setWarp;
import com.mcdimensions.BungeeSuite.warps.spawn;
import com.mcdimensions.BungeeSuite.warps.warplist;
import com.mcdimensions.BungeeSuite.warps.warps;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeSuite extends Plugin {
	ProxyServer proxy;
	SQL sql;
	public Config config, ChannelConfig, locale, prefixConfig;
	//Messages
	public String PLAYER_IGNORING, PLAYER_IGNORED, PLAYER_UNIGNORED, DEFAULT_BAN_PLAYER, PLAYER_TELEPORTED_TO,TELEPORTED_PLAYER_TO_TARGET, TELEPORTS_NOT_ENABLED, TELEPORT_REQUEST_HERE, TELEPORT_REQUEST_TO,WARP_SPAWN_NOT_EXIST, WARP_NOT_EXIST, WARP_DELETE_CONFIRM, WARP_CREATE_CONFIRM, PORTAL_DELETE_CONFIRM, PORTAL_NOT_EXIST, DEFAULT_IPBAN_PLAYER, TEMP_BAN_BROADCAST, BAN_MESSAGE_BROADCAST, IP_NOT_EXIST, IP_UNBANNED, NO_PERMISSION, PLAYER_REPLY_NONE, PLAYER_NICKNAME_CHANGE, PLAYER_NICKNAMED, PLAYER_ALL_MUTED, PLAYER_ALL_UNMUTED, PLAYER_MUTE, PLAYER_MUTED, PLAYER_UNMUTE, PLAYER_UNMUTED, PLAYER_NOT_EXIST, PLAYER_NOT_ONLINE, PLAYER_UNBANNED, PLAYER_INVITED, PLAYER_INVITE, PLAYER_SENDING_SERVER, PLAYER_NOT_BANNED, DEFAULT_KICK_PLAYER, DEFAULT_KICK_BROADCAST, UNBAN_PLAYER, CHANNEL_INVITE_NOPERM, CHANNEL_NOT_INVITED, CHANNEL_NOT_LEAVE_SERVER, CHANNEL_TOGGLE_PERMISSION, CHANNEL_NOT_LEAVE_OWNER, CHANNEL_IS_MEMBER, CHANNEL_NOT_MEMBER, CHANNEL_NO_PERMISSION, CHANNEL_NOT_EXIST, CHANNEL_CREATE_CONFIRM, CHANNEL_DELETE_CONFIRM, CHANNEL_PLAYER_JOINED, CHANNEL_WELCOME, CHANNEL_KICK_PLAYER, CHANNEL_PLAYER_LEAVE, CHANNEL_TOO_MANY, BROADCAST_MESSAGE, CHATSPY_TOGGLED, NO_PERMISSION_COLOR;
	//PREFIXES
	public HashMap<String, String> prefix;
	//MySQL
	public String url, database, port, username, password;
	//Commands
		public String bungeeSuiteCommand;
		//banning
		public String ban,ipban,kick,kickall,pardon,tban,tempban,unban,unbanip;
		//chat
		public String cignore,ignoring,broadcast,g,global,mute,muteall,nick,nickname,w,world,s,server,msg,whisper,r,message,reply,t,tell,toggle,invite,join,accept,create,createchannel,channel,c,delete,deletechannel,leave,leavechannel,chatspy,displayserver;
		public String defaultCustomChannelFormat,defaultServerChannelFormat;
		public int maxCustomChannels;
		//portal
		public String setportal,delportal,listportals,portalsc;
		//teleport
		public String tp,tpa,tpaccept,tpahere,tpall,tpdeny;
		//warp
		public String delwarp,setwarp,spawn,warp,warplist,warpsc; 
	Utilities utils;
	public ColorLog cl;
	public boolean warps,portals,signs,teleports, chat, titles, banning, replaceWords, formatChat, gamemode, useSpawn,logChat, motdColored,allMuted, useVault,channelMessages,globalToggleable,globalDefault,loginMessages;
	//teleports
	public HashMap<ProxiedPlayer, ProxiedPlayer> teleportsPending;
	public HashMap<ProxiedPlayer, ProxiedPlayer> tpaList;
	public HashMap<ProxiedPlayer, ProxiedPlayer> tpHereList;
	public HashSet<String> blockedTeleports, IPbans;
	public HashMap<ProxiedPlayer, String> gamemodes;
	//warps
	public HashMap<String, Warp> warpList;
	public HashMap<String,Warp> warped;
	//chat
	public HashMap<String,String> serverNames, replacementWords;

	public HashMap<String, Calendar> PlayerBans;
	public HashMap<String, ChatPlayer> OnlinePlayers;
	public HashMap<String ,ChatChannel> chatChannels;
	public String motdFormatOnline;
	public String motdFormatOffline;
	public String playerCountFormatOnline;
	public String playerCountFormatOnlineClick;
	public String playerCountFormatOffline;
	public String playerCountFormatOfflineClick;
	public String portalFormatOnline;
	public String portalFormatOffline;
	public String portalFormatOfflineClick;
	public HashSet<String> chatSpying, ignoreServers;
	public BungeeSuite instance;
	
	public void onLoad(){
		this.instance = this;
	}

	public void onEnable() {
		cl = new ColorLog(ProxyServer.getInstance());
		cl.Color("&2------------Enabling BungeeSuite------------");
		cl.Color("&2 -Loading config");
		loadConfig();
		cl.Color("&2 -Initialising Variables");
		try {
			loadVariables();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		cl.Color("&2 -Creating SQL tables");
		try {
			loadSQLTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			cl.Color("&2 -Loading Servers");
			insertServers();
		} catch (SQLException e) {
	
			e.printStackTrace();
		}
		cl.Color("&2 -Registering Listeners And Channels");
		registerListeners();
		cl.Color("&2 -Registering Commands");
		registerCommands();
		if(warps){
			cl.Color("&2 -Loading Warps");
			loadWarps();
		}
		if(portals){
			cl.Color("&2 -Loading Portals");
			loadPortals();
		}
		if(signs){
			cl.Color("&2 -Loading Signs");
			loadSigns();
		}
		if(teleports){
			cl.Color("&2 -Loading Teleports");
			loadTeleports();
		}
		if(chat){
			cl.Color("&2 -Loading Chat");
		}
			try {
				loadChat();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(banning){
			cl.Color("&2 -Loading Bans");
			try {
				loadBans();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			cl.Color("&2 -Loading Messages");
			loadMessages();
		
		if(gamemode){
//			cl.Color("GREEN -Loading Bans");
			loadGamemode();
		}
	}

	private void loadMessages() {
		 String localepath= "/plugins/BungeeSuite/locale.yml";
		locale=new Config(localepath);
		NO_PERMISSION = locale.getString("NO_PERMISSION", ChatColor.RED+"You do not have permission to use that command");
		CHATSPY_TOGGLED = locale.getString("CHATSPY_TOGGLED", ChatColor.DARK_GREEN+"Chatspy toggled");
		BROADCAST_MESSAGE = locale.getString("BROADCAST_MESSAGE", ChatColor.AQUA+"[BROADCAST]:"+ChatColor.GREEN+" %message");
		PLAYER_NOT_EXIST = locale.getString("PLAYER_NOT_EXIST", ChatColor.RED+"That player does not exist");
		PLAYER_NOT_ONLINE = locale.getString("PLAYER_NOT_ONLINE", ChatColor.RED+"That player is not online");
		DEFAULT_BAN_PLAYER = locale.getString("DEFAULT_BAN_PLAYER", "You have been banned!");
		DEFAULT_IPBAN_PLAYER = locale.getString("DEFAULT_IPBAN_PLAYER", ChatColor.GOLD+"[%player]"+ChatColor.DARK_GREEN+ " has been ip banned from the server");
		TEMP_BAN_BROADCAST = locale.getString("TEMP_BAN_BROADCAST", ChatColor.GOLD+"[%player]"+ChatColor.DARK_GREEN+ "has been temporarily banned by"+ChatColor.GOLD+" %sender"+ChatColor.DARK_GREEN+" for %days:days %hours:hours %minutes:minutes");
		BAN_MESSAGE_BROADCAST =locale.getString("BAN_MESSAGE_BROADCAST", ChatColor.GOLD+"[%player]"+ChatColor.DARK_GREEN+" has been banned from the server by %sender, reason: %message");
		IP_NOT_EXIST = locale.getString("IP_NOT_EXIST", ChatColor.RED+"That IP does not exist");
		PLAYER_UNBANNED= locale.getString("PLAYER_UNBANNED", ChatColor.DARK_GREEN+ "%player has been unbanned!");
		IP_UNBANNED = locale.getString("IP_UNBANNED", ChatColor.DARK_GREEN+"IP has been unbanned");
		PLAYER_NOT_BANNED = locale.getString("PLAYER_NOT_BANNED", ChatColor.RED+"That player is not banned");
		DEFAULT_KICK_PLAYER = locale.getString("DEFAULT_KICK_PLAYER", ChatColor.RED+"You have been kicked from the server by %sender");
		DEFAULT_KICK_BROADCAST = locale.getString("DEFAULT_KICK_BROADCAST", ChatColor.AQUA+"[%player]"+ChatColor.GOLD+" has been kicked from the server by %sender for: %message");
		TELEPORTED_PLAYER_TO_TARGET = locale.getString("TELEPORTED_PLAYER_TO_TARGET", ChatColor.DARK_GREEN+"You have been teleported to %player");
		PLAYER_TELEPORTED_TO = locale.getString("PLAYER_TELEPORTED_TO", ChatColor.DARK_GREEN+"%sender has teleported to you");
		TELEPORT_REQUEST_HERE = locale.getString("TELEPORT_REQUEST_HERE", ChatColor.LIGHT_PURPLE+"%player has requested you teleport to them");
		TELEPORT_REQUEST_TO = locale.getString("TELEPORT_REQUEST_TO", ChatColor.LIGHT_PURPLE+"%player has requested to teleport to you");
		TELEPORTS_NOT_ENABLED = locale.getString("TELEPORTS_NOT_ENABLED", ChatColor.RED+"Teleports are not enabled for this world");
		WARP_SPAWN_NOT_EXIST = locale.getString("WARP_SPAWN_NOT_EXIST", ChatColor.RED+"The warp \"Spawn\" has not been created yet, please ask your admin to set the \"Spawn\" warp");
		WARP_NOT_EXIST= locale.getString("WARP_NOT_EXIST", ChatColor.RED+"That warp does not exist");
		WARP_DELETE_CONFIRM= locale.getString("WARP_DELETE_CONFIRM", ChatColor.DARK_GREEN+"Warp deleted");
		PORTAL_DELETE_CONFIRM = locale.getString("PORTAL_DELETE_CONFIRM", ChatColor.DARK_GREEN+"Portal deleted");
		PORTAL_NOT_EXIST = locale.getString("PORTAL_NOT_EXIST", ChatColor.RED+"That portal does not exist");
		PLAYER_REPLY_NONE = locale.getString("PLAYER_REPLY_NONE", ChatColor.RED+"You have no players to reply to");
		PLAYER_NICKNAME_CHANGE = locale.getString("PLAYER_NICKNAME_CHANGE", ChatColor.GOLD+"Your nickname has been changed to "+ChatColor.DARK_GREEN+ "%nickname"+ ChatColor.GOLD+" by %sender");
		PLAYER_NICKNAMED = locale.getString("PLAYER_NICKNAMED", ChatColor.DARK_GREEN+"%player's nickname has been changed to %nickname");
		PLAYER_ALL_MUTED = locale.getString("PLAYER_ALL_MUTED", ChatColor.RED+"All players have been temporarily muted");
		PLAYER_ALL_UNMUTED = locale.getString("PLAYER_ALL_UNMUTED", ChatColor.DARK_GREEN+"All players have been unmuted!");
		PLAYER_MUTE = locale.getString("PLAYER_MUTE", ChatColor.RED+"You have been muted!");
		PLAYER_MUTED = locale.getString("PLAYER_MUTED", ChatColor.RED+"Player has been muted");
		PLAYER_UNMUTE = locale.getString("PLAYER_UNMUTE", ChatColor.DARK_GREEN+"You have been unmuted!");
		PLAYER_UNMUTED = locale.getString("PLAYER_UNMUTED", ChatColor.DARK_GREEN+"Player has been unmuted!");
		PLAYER_INVITED = locale.getString("PLAYER_INVITED", ChatColor.DARK_GREEN+"%player has been invited to join the channel %channel");
		PLAYER_INVITE = locale.getString("PLAYER_INVITE", ChatColor.DARK_GREEN+"You have been invited by %sender to join the channel %channel");
		PLAYER_SENDING_SERVER = locale.getString("PLAYER_SENDING_SERVER", ChatColor.DARK_GREEN+"Toggled display of server");
		CHANNEL_INVITE_NOPERM = locale.getString("CHANNEL_INVITE_NOPERM", ChatColor.RED+"You do not have permission to invite players to the channel "+ChatColor.AQUA+"%channel");
		CHANNEL_NOT_INVITED = locale.getString("CHANNEL_NOT_INVITED", ChatColor.RED+"You are not invited to join the channel "+ChatColor.AQUA+"%channel");
		CHANNEL_NOT_LEAVE_SERVER = locale.getString("CHANNEL_NOT_LEAVE_SERVER", ChatColor.RED+"You are unable to leave this channel as it is a server channel");
		CHANNEL_TOGGLE_PERMISSION = locale.getString("CHANNEL_TOGGLE_PERMISSION", ChatColor.RED+"You do not have permission to toggle to this channel");
		CHANNEL_NOT_LEAVE_OWNER = locale.getString("CHANNEL_NOT_LEAVE_OWNER", ChatColor.RED+"You are unable to leave the channel %channel as you are the owner");
		CHANNEL_IS_MEMBER = locale.getString("CHANNEL_IS_MEMBER", ChatColor.RED+"%player is already a member of the channel %channel");
		CHANNEL_NOT_MEMBER = locale.getString("CHANNEL_NOT_MEMBER", ChatColor.RED+"That player is not a member of the channel");
		CHANNEL_NO_PERMISSION = locale.getString("CHANNEL_NO_PERMISSION", ChatColor.RED+"You do not have permission to edit this channel");
		CHANNEL_NOT_EXIST = locale.getString("CHANNEL_NOT_EXIST", ChatColor.RED+"That channel does not exist!");
		CHANNEL_CREATE_CONFIRM = locale.getString("CHANNEL_CREATE_CONFIRM", ChatColor.DARK_GREEN+"Channel %channel created!");
		CHANNEL_DELETE_CONFIRM = locale.getString("CHANNEL_DELETE_CONFIRM", ChatColor.DARK_GREEN+"Channel deleted!");
		CHANNEL_PLAYER_JOINED = locale.getString("CHANNEL_PLAYER_JOINED", ChatColor.GRAY+"%player has joined the channel %channel");
		CHANNEL_WELCOME = locale.getString("CHANNEL_WELCOME", ChatColor.GRAY+"Welcome to the channel %channel");
		CHANNEL_KICK_PLAYER = locale.getString("CHANNEL_KICK_PLAYER", ChatColor.RED+"You have been kicked from the channel %channel");
		CHANNEL_PLAYER_LEAVE = locale.getString("CHANNEL_PLAYER_LEAVE", ChatColor.GRAY+"%player has left the channel %channel");
		CHANNEL_TOO_MANY = locale.getString("CHANNEL_TOO_MANY", ChatColor.RED +" Player owns to many channels");
		NO_PERMISSION_COLOR = locale.getString("NO_PERMISSION_COLOR", ChatColor.RED+"You do not have permission to use a colored nickname");
		PLAYER_IGNORING = locale.getString("PLAYER_IGNORING", ChatColor.RED+"That player is ignoring you!");
		PLAYER_IGNORED = locale.getString("PLAYER_IGNORED", ChatColor.DARK_GREEN+"Player ignored");
		PLAYER_UNIGNORED = locale.getString("PLAYER_UNIGNORED", ChatColor.DARK_GREEN+"Player unignored");
		locale.save();
		
		
	}

	private void loadGamemode() {
		gamemodes = new HashMap<ProxiedPlayer,String>();
		
	}

	private void loadBans() throws SQLException {
		IPbans = new HashSet<String>();
		PlayerBans = new HashMap<String, Calendar>();
		this.utils.loadBans();
		
	}

	private void loadChat() throws SQLException {
		OnlinePlayers=new HashMap<String, ChatPlayer> ();
		chatChannels = new HashMap<String,ChatChannel>();
		chatSpying = new HashSet<String>();
	 	String configpath= "/plugins/BungeeSuite/channelFormats.yml";
	 	ChannelConfig=new Config(configpath);
		utils.loadChannels();
		for(ChatChannel data:chatChannels.values()){
			String format = ChannelConfig.getString("BungeeSuite.Chat.Channels."+data.getName(), data.getFormat());
			if(!format.equals(data.getFormat())){
				getUtilities().reformatChannel(data.getName(), format);
			}
		}
		ChannelConfig.save();
		chatSpying = new HashSet<String>();
	 	String prefixpath= "/plugins/BungeeSuite/prefixes.yml";
	 	this.prefix = new HashMap<String,String>();
	 	prefixConfig=new Config(prefixpath);
	 	prefix.put("owner", prefixConfig.getString("Prefix.owner", ChatColor.RED+"[Owner]"));
	 	prefix.put("admin", prefixConfig.getString("Prefix.admin", ChatColor.RED+"[Admin]"));
	 	prefix.put("mod", prefixConfig.getString("Prefix.mod", ChatColor.RED+"[Mod]"));
	 	prefix.put("tmod", prefixConfig.getString("Prefix.tmod", ChatColor.DARK_PURPLE+"[TMod]"));
	}

	private void loadTeleports() {
		teleportsPending =new HashMap<ProxiedPlayer, ProxiedPlayer>();
		tpaList = new HashMap<ProxiedPlayer, ProxiedPlayer>();
		tpHereList = new HashMap<ProxiedPlayer, ProxiedPlayer>();
	}

	private void loadSQLTables() throws SQLException {
			utils.selectDatabase();
			utils.CreateChatSQLTables();
			utils.createBanningSQLTables();
			utils.createSQLServerTable();
			utils.CreateSignSQLTables();
			utils.UpdateSignFormats();
			utils.CreateWarpSQLTables();
			utils.CreatePortalSQLTables();
		   utils.createStandardChannels();
			utils.updateTables();
	}

	private void registerCommands() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this,new bungeeSuiteCommand(this));
		if(portals){
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new setportal(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new delportal(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new listPortals(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new Portals(this));
		}
		if(warps){
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new warps(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new warplist(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new setWarp(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new delWarp(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new WarpC(this));
			if(useSpawn){
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new spawn(this));
			}
		}
		if(teleports){
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tp(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tpahere(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tpa(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tpaccept(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tpdeny(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tpall(this));
		}
		if(chat){
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new g(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new global(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new s(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new server(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new mute(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new muteall(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new broadcast(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new nick(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new nickname(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new msg(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new message(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new r(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new reply(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new whisper(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new t(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tell(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new toggle(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new accept(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new invite(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new join(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new create(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new createchannel(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new channel(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new c(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new delete(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new deletechannel(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new leave(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new leavechannel(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new chatspy(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new displayserver(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new ignore(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new ignoring(this));
		}
		if(banning){
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new kick(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new kickall(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new ban(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tban(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new tempban(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new ipban(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new pardon(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new unban(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this,new unbanip(this));
		}
	}

	private void registerListeners() {
		proxy.getPluginManager().registerListener(this,new PluginMessageListener(this));
		proxy.registerChannel("BungeeSuite");
		proxy.registerChannel("BungeeSuiteMC");
		proxy.getPluginManager().registerListener(this,new ServerLoginLogout(this));	
		if(loginMessages){
			proxy.getPluginManager().registerListener(this,new LoginMessages(this));	
		}
		if(warps){
//			proxy.getPluginManager().registerListener(this,new WarpListener(this));
		}
		if(teleports){
//			proxy.getPluginManager().registerListener(this,new TpListener(this));
		}
			proxy.getPluginManager().registerListener(this,new ChatListener(this)); //removed chat check as temp to fix issues.
		
		if(banning){
			proxy.getPluginManager().registerListener(this,new BanListener(this));
		}
	}

	private void loadSigns() {
		
	}

	private void loadPortals() {
		// TODO Auto-generated method stub
	}

	private void loadWarps() {
		// TODO Auto-generated method stub
		 warpList = new HashMap<String, Warp>();
		 warped = new HashMap<String, Warp>();
	}

	private void loadVariables() throws SQLException {
		proxy = ProxyServer.getInstance();
		sql = new SQL(url,port, database, username, password);
		utils = new Utilities(this);
		this.allMuted = false;
	}

	private void insertServers() throws SQLException {
		for (String data : proxy.getServers().keySet()) {
			if (!utils.serverExists(data)) {
				utils.createServer(data);
				cl.Color("&2 Server " + data
						+ " added to database!");
			}
		}
		cl.Color("&6     -"+proxy.getServers().values().size()+" Servers loaded");

	}


	private void loadConfig() {
		 	String configpath= "/plugins/BungeeSuite/config.yml";
		 	config=new Config(configpath);
			username = config.getString("BungeeSuite.SQL.Username", "root");
			password = config.getString("BungeeSuite.SQL.Password", "password");
			database = config.getString("BungeeSuite.SQL.Database", "minecraft");
			url = config.getString("BungeeSuite.SQL.URL", "localhost");
			port = config.getString("BungeeSuite.SQL.Port", "3306");
			warps = config.getBoolean("BungeeSuite.Warps.Enabled", true);
			useSpawn = config.getBoolean("BungeeSuite.Warps.UseSpawn", true);
			signs =  config.getBoolean("BungeeSuite.Signs.Enabled", true);
			loginMessages = config.getBoolean("BungeeSuite.LoginMessages.enabled", true);
			motdColored =  config.getBoolean("BungeeSuite.Signs.MOTDColored", true);
			motdFormatOnline =  config.getString("BungeeSuite.Signs.MOTDFormat.Online", "&2[%server]&0:%motd");
			motdFormatOffline = config.getString("BungeeSuite.Signs.MOTDFormat.Offline", "&4Server is offline");
			playerCountFormatOnline =  config.getString("BungeeSuite.Signs.PlayerCount.Online", "&2[%server],Players,%players, ");
			playerCountFormatOnlineClick =  config.getString("BungeeSuite.Signs.PlayerCount.OnlineClick", "&2[%server]players");
			playerCountFormatOffline =  config.getString("BungeeSuite.Signs.PlayerCount.Offline", "&4[%server],Players,&4Offline, ");
			playerCountFormatOfflineClick =  config.getString("BungeeSuite.Signs.PlayerCount.OfflineClick", "&4%server is offline");
			portalFormatOnline =  config.getString("BungeeSuite.Signs.Portal.Online", "&2[Portal],,%server, ");
			portalFormatOffline =  config.getString("BungeeSuite.Signs.Portal.Offline", "&4[Portal],%server,&4Offline, ");
			portalFormatOfflineClick =  config.getString("BungeeSuite.Signs.Portal.OfflineClick", "&4Server is offline");	
			portals = config.getBoolean("BungeeSuite.Portals.Enabled", true);
			teleports =  config.getBoolean("BungeeSuite.Teleports.Enabled", true);
			String bt[] = config.getString("BungeeSuite.Teleports.BlockTeleportsInServers", "Towny").split(",");
			blockedTeleports = new HashSet<String>();
			for(String data: bt){
				blockedTeleports.add(data);
			}
			//chat
			chat =  config.getBoolean("BungeeSuite.Chat.Enabled", true);
			logChat = config.getBoolean("BungeeSuite.Chat.LogChat", true);
			formatChat = config.getBoolean("BungeeSuite.Chat.FormatChat", true);
			useVault = config.getBoolean("BungeeSuite.Chat.UseVault", false);
			channelMessages = config.getBoolean("BungeeSuite.Chat.ChannelMessages", true);
			globalDefault = config.getBoolean("BungeeSuite.Chat.globalDefault", true);
			globalToggleable= config.getBoolean("BungeeSuite.Chat.GlobalIsToggleable", true);
			maxCustomChannels = config.getInt("BungeeSuite.Chat.MaxCustomChannels", 1);
			ignoreServers = new HashSet<String>();
			String ignore[] = config.getString("BungeeSuite.Chat.IgnoreServerChat", "Towny,Heroes").split(",");
			for(String data:ignore){
				ignoreServers.add(data);
			}
			serverNames = new HashMap<String,String>();
			String [] sn = config.getString("BungeeSuite.Chat.ReplaceServers", "Spawn-S,Creative-C").split(",");
			for(String data: sn){
			String[] s = data.split("-");
			serverNames.put(s[0], s[1]);
		}
			replacementWords = new HashMap<String,String>();
			String [] rw = config.getString("BungeeSuite.Chat.ReplaceWords", "blood-&4Blood&f").split(",");
			for(String data: rw){
				String[] s = data.split("-");
				replacementWords.put(s[0], s[1]);
			}
			defaultCustomChannelFormat = config.getString("BungeeSuite.Chat.defaultServerChannelFormat", "&e[%cname]&f%prefix&f%player&f: &7%message");
			defaultServerChannelFormat = config.getString("BungeeSuite.Chat.defaultCustomChannelFormat", "&9[%cname]&f%player: &7%message");
			//other
			banning = config.getBoolean("BungeeSuite.Banning", true);
			//commands
				bungeeSuiteCommand = config.getString("BungeeSuite.Commands.bungeesuite", "bs");
				//banning
				ban = config.getString("BungeeSuite.Commands.ban", "ban");
				ipban = config.getString("BungeeSuite.Commands.ipban", "ipban");
				kick = config.getString("BungeeSuite.Commands.kick", "kick");
				kickall = config.getString("BungeeSuite.Commands.kickall", "kickall");
				pardon = config.getString("BungeeSuite.Commands.pardon", "pardon");
				tban = config.getString("BungeeSuite.Commands.tban", "tban");
				tempban = config.getString("BungeeSuite.Commands.tempban", "tempban");
				unban = config.getString("BungeeSuite.Commands.unban","unban");
				unbanip= config.getString("BungeeSuite.Commands.unbanip", "unbanip");
				//chat
				cignore = config.getString("BungeeSuite.Commands.ignore", "ignore");
				ignoring = config.getString("BungeeSuite.Commands.ignoring", "ignoring");
				broadcast = config.getString("BungeeSuite.Commands.broadcast", "broadcast");
				g= config.getString("BungeeSuite.Commands.g", "g");
				global = config.getString("BungeeSuite.Commands.global", "global");
				w= config.getString("BungeeSuite.Commands.w", "world");
				world = config.getString("BungeeSuite.Commands.world", "world");
				s = config.getString("BungeeSuite.Commands.s", "s");
				server = config.getString("BungeeSuite.Commands.server", "server");
				mute = config.getString("BungeeSuite.Commands.mute", "mute");
				muteall = config.getString("BungeeSuite.Commands.muteall", "muteall");
				nick = config.getString("BungeeSuite.Commands.nick", "nick");
				nickname = config.getString("BungeeSuite.Commands.nickname", "nickname");
				msg = config.getString("BungeeSuite.Commands.msg", "msg");
				message = config.getString("BungeeSuite.Commands.message", "message");
				whisper = config.getString("BungeeSuite.Commands.whisper", "whisper");
				r = config.getString("BungeeSuite.Commands.r", "r");
				reply = config.getString("BungeeSuite.Commands.reply", "reply");
				t= config.getString("BungeeSuite.Commands.t", "t");
				tell = config.getString("BungeeSuite.Commands.tell", "tell");
				toggle= config.getString("BungeeSuite.Commands.toggle","toggle");
				invite=config.getString("BungeeSuite.Commands.invite", "invite");
				accept=config.getString("BungeeSuite.Commands.accept", "accept");
				join=config.getString("BungeeSuite.Commands.join", "join");
				create=config.getString("BungeeSuite.Commands.create", "create");
				createchannel=config.getString("BungeeSuite.Commands.createchannel", "createchannel");
				channel= config.getString("BungeeSuite.Commands.channel", "channel");
				c = config.getString("BungeeSuite.Commands.c", "c");
				delete = config.getString("BungeeSuite.Commands.delete", "delete");
				deletechannel = config.getString("BungeeSuite.Commands.deletechannel", "deletechannel");
				leave = config.getString("BungeeSuite.Commands.leave","leave");
				leavechannel = config.getString("BungeeSuite.Commands.leavechannel", "leavechannel");
				server = config.getString("BungeeSuite.Commands.server", "server");
				s = config.getString("BungeeSuite.Commands.s", "s");
				chatspy = config.getString("BungeeSuite.Commands.chatspy", "chatspy");
				displayserver = config.getString("BungeeSuite.Commands.displayserver", "displayserver");
				//Portal
				setportal = config.getString("BungeeSuite.Commands.setportal", "setportal");
				delportal = config.getString("BungeeSuite.Commands.delportal", "delportal");
				listportals = config.getString("BungeeSuite.Commands.listportals", "listportals");
				portalsc = config.getString("BungeeSuite.Commands.portals", "portals");
				//teleport
				tp = config.getString("BungeeSuite.Commands.tp", "tp");
				tpa = config.getString("BungeeSuite.Commands.tpa", "tpa");
				tpaccept = config.getString("BungeeSuite.Commands.tpaccept", "tpaccept");
				tpdeny = config.getString("BungeeSuite.Commands.tpdeny", "tpdeny");
				tpahere = config.getString("BungeeSuite.Commands.tpahere", "tpahere");
				tpall = config.getString("BungeeSuite.Commands.tpall", "tpall");
				//warps
				delwarp = config.getString("BungeeSuite.Commands.delwarp", "delwarp");
				setwarp = config.getString("BungeeSuite.Commands.setwarp", "setwarp");
				spawn = config.getString("BungeeSuite.Commands.spawn", "spawn");
				warp = config.getString("BungeeSuite.Commands.warp", "warp");
				warplist = config.getString("BungeeSuite.Commands.warplist", "warplist");
				warpsc= config.getString("BungeeSuite.Commands.warps", "warps");
				config.save();
	}
	
	

	public Config getConfig() {
		return config;
	}

	public SQL getSQL() {
		return sql;
	}
	
	public Utilities getUtilities(){
		return utils;
	}
	public ProxyServer getProxy(){
		return proxy;
	}

	public ChatPlayer getChatPlayer(String player) {
		return OnlinePlayers.get(player);
	}

	public ChatChannel getChannel(String string) {
		return this.chatChannels.get(string);
		
	}

	public void reload() {
		config.load();
		ChannelConfig.load();
		locale.load();
		this.prefixConfig.load();
		
	}

}
