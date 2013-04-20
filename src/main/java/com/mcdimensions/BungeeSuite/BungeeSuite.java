package com.mcdimensions.BungeeSuite;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import com.mcdimensions.BungeeSuite.banning.BanCommand;
import com.mcdimensions.BungeeSuite.banning.BanIPCommand;
import com.mcdimensions.BungeeSuite.banning.KickCommand;
import com.mcdimensions.BungeeSuite.banning.KickAllCommand;
import com.mcdimensions.BungeeSuite.banning.TempBanCommand;
import com.mcdimensions.BungeeSuite.banning.UnbanCommand;
import com.mcdimensions.BungeeSuite.banning.UnbanIPCommand;
import com.mcdimensions.BungeeSuite.chat.ChatChannel;
import com.mcdimensions.BungeeSuite.chat.ChatPlayer;
import com.mcdimensions.BungeeSuite.chat.AcceptCommand;
import com.mcdimensions.BungeeSuite.chat.BroadcastCommand;
import com.mcdimensions.BungeeSuite.chat.ChannelCommand;
import com.mcdimensions.BungeeSuite.chat.ChatSpyCommand;
import com.mcdimensions.BungeeSuite.chat.CreateChannelCommand;
import com.mcdimensions.BungeeSuite.chat.DeleteChannelCommand;
import com.mcdimensions.BungeeSuite.chat.DisplayServerCommand;
import com.mcdimensions.BungeeSuite.chat.GlobalCommand;
import com.mcdimensions.BungeeSuite.chat.IgnoreCommand;
import com.mcdimensions.BungeeSuite.chat.IgnoringCommand;
import com.mcdimensions.BungeeSuite.chat.InviteCommand;
import com.mcdimensions.BungeeSuite.chat.JoinCommand;
import com.mcdimensions.BungeeSuite.chat.LeaveChannelCommand;
import com.mcdimensions.BungeeSuite.chat.ListChannelsCommand;
import com.mcdimensions.BungeeSuite.chat.MessageCommand;
import com.mcdimensions.BungeeSuite.chat.MuteCommand;
import com.mcdimensions.BungeeSuite.chat.MuteAllCommand;
import com.mcdimensions.BungeeSuite.chat.NicknameCommand;
import com.mcdimensions.BungeeSuite.chat.ReplyCommand;
import com.mcdimensions.BungeeSuite.chat.ToggleCommand;
import com.mcdimensions.BungeeSuite.chat.ServerCommand;
import com.mcdimensions.BungeeSuite.config.Config;
import com.mcdimensions.BungeeSuite.listeners.BanListener;
import com.mcdimensions.BungeeSuite.listeners.ChatListener;
import com.mcdimensions.BungeeSuite.listeners.LoginMessages;
import com.mcdimensions.BungeeSuite.listeners.PluginMessageListener;
import com.mcdimensions.BungeeSuite.listeners.ServerLoginLogout;
import com.mcdimensions.BungeeSuite.portals.DeletePortalCommand;
import com.mcdimensions.BungeeSuite.portals.ListPortalsCommand;
import com.mcdimensions.BungeeSuite.portals.SetPortalCommand;
import com.mcdimensions.BungeeSuite.teleports.TPCommand;
import com.mcdimensions.BungeeSuite.teleports.TPACommand;
import com.mcdimensions.BungeeSuite.teleports.TPAcceptCommand;
import com.mcdimensions.BungeeSuite.teleports.TPAHereCommand;
import com.mcdimensions.BungeeSuite.teleports.TPAllCommand;
import com.mcdimensions.BungeeSuite.teleports.TPDenyCommand;
import com.mcdimensions.BungeeSuite.utilities.ColorLog;
import com.mcdimensions.BungeeSuite.utilities.SQL;
import com.mcdimensions.BungeeSuite.utilities.Utilities;
import com.mcdimensions.BungeeSuite.warps.Warp;
import com.mcdimensions.BungeeSuite.warps.WarpCommand;
import com.mcdimensions.BungeeSuite.warps.DeleteWarpCommand;
import com.mcdimensions.BungeeSuite.warps.SetWarpCommand;
import com.mcdimensions.BungeeSuite.warps.WarpSpawnCommand;
import com.mcdimensions.BungeeSuite.warps.ListWarpsCommand;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeSuite extends Plugin {
	public static final String PLUGIN_NAME = "BungeeSuite";

	/* Chat Settings */
	public Config config, channelConfig, locale, prefixConfig, commands;
	public String PLAYER_IGNORING, PLAYER_IGNORED, PLAYER_UNIGNORED,
			DEFAULT_BAN_PLAYER, PLAYER_TELEPORTED_TO,
			TELEPORTED_PLAYER_TO_TARGET, TELEPORTS_NOT_ENABLED,
			TELEPORT_REQUEST_HERE,TELEPORT_REQUEST_SENT, TELEPORT_REQUEST_TO, WARP_SPAWN_NOT_EXIST,
			WARP_NOT_EXIST, WARP_DELETE_CONFIRM, WARP_CREATE_CONFIRM,
			PORTAL_DELETE_CONFIRM, PORTAL_NOT_EXIST, DEFAULT_IPBAN_PLAYER,
			TEMP_BAN_BROADCAST, BAN_MESSAGE_BROADCAST, IP_NOT_EXIST,
			IP_UNBANNED, NO_PERMISSION, PLAYER_REPLY_NONE,
			PLAYER_NICKNAME_CHANGE, PLAYER_NICKNAMED, PLAYER_ALL_MUTED,
			PLAYER_ALL_UNMUTED, PLAYER_MUTE, PLAYER_MUTED, PLAYER_UNMUTE,
			PLAYER_UNMUTED, PLAYER_NOT_EXIST, PLAYER_NOT_ONLINE,
			PLAYER_UNBANNED, PLAYER_INVITED, PLAYER_INVITE, PLAYER_SENDING_CHAT,
			PLAYER_SENDING_SERVER, PLAYER_NOT_BANNED, DEFAULT_KICK_PLAYER,
			DEFAULT_KICK_BROADCAST, UNBAN_PLAYER, CHANNEL_INVITE_NOPERM,
			CHANNEL_NOT_INVITED, CHANNEL_NOT_LEAVE_SERVER,
			CHANNEL_TOGGLE_PERMISSION, CHANNEL_NOT_LEAVE_OWNER,
			CHANNEL_IS_MEMBER, CHANNEL_NOT_MEMBER, CHANNEL_NO_PERMISSION,
			CHANNEL_NOT_EXIST, CHANNEL_ALREADY_EXISTS, CHANNEL_NEW_OWNER, CHANNEL_CREATE_CONFIRM, CHANNEL_DELETE_CONFIRM,
			CHANNEL_PLAYER_JOINED, CHANNEL_WELCOME, CHANNEL_KICK_PLAYER,
			CHANNEL_PLAYER_LEAVE, CHANNEL_TOO_MANY, BROADCAST_MESSAGE,
			CHATSPY_TOGGLED, NO_PERMISSION_COLOR;
	
	public HashMap<String, String> prefix;
	
	/* MySQL Information */
	public String url, database, port, username, password;
	
	/* Command Configuration */
	public String bungeeSuiteCommand;
	public String ban, ipban, kick, kickall, pardon, tban, tempban, unban,
			unbanip;
	public String cignore, ignoring, broadcast, g, global, mute, muteAll, nick,
			nickname, w, world, s, server, msg, whisper, r, message, reply, t,
			tell, toggle, invite, join, accept, create, createChannel, channel,
			c, delete, deleteChannel, leave, leaveChannel, chatSpy,
			displayServer, channels, channelslist;
	public String defaultCustomChannelFormat, defaultServerChannelFormat;
	public int maxCustomChannels;
	public String setPortal, delPortal, listPortals, portalsc;
	public String tp, tpa, tpAccept, tpaHere, tpAll, tpDeny;
	public String delWarp, setWarp, spawn, warp, warpsList, warpsc;
	public boolean warpsEnabled, portalsEnabled, signsEnabled, teleportsEnabled, chatEnabled, titles, bansEnabled,
			replaceWords, formatChat, gamemode, spawnWarpEnabled, logChat, motdColored,
			allMuted, useVault, channelMessages, globalToggleable,
			globalDefault, loginMessagesEnabled;

	/* Teleport Info */
	public HashMap<ProxiedPlayer, ProxiedPlayer> teleportsPending;
	public HashMap<ProxiedPlayer, ProxiedPlayer> tpaList;
	public HashMap<ProxiedPlayer, ProxiedPlayer> tpHereList;
	public HashSet<String> blockedTeleports, IPbans;
	public HashMap<ProxiedPlayer, String> gamemodes;

	/* Warp Info */
	public HashMap<String, Warp> warpList;
	public HashMap<String, Warp> warped;

	/* Chat Info*/
	public HashMap<String, String> serverNames, replacementWords;
	public HashMap<String, Calendar> playerBans;
	public HashMap<String, ChatPlayer> onlinePlayers;
	public HashMap<String, ChatChannel> chatChannels;
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
	
	private Utilities utils;
	public ColorLog cl;
	
	private ProxyServer proxy;
	private SQL sql;

	public void onLoad() {
		this.instance = this;
	}

	public void onEnable() {
		cl = new ColorLog(ProxyServer.getInstance());
		cl.cLog("&2------------Enabling BungeeSuite------------");
		
		cl.cLog("&2 -Loading config");
		loadConfig();
		
		cl.cLog("&2 -Initialising Variables");
		try {
			loadVariables();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		cl.cLog("&2 -Creating SQL tables");
		try {
			loadSQLTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		cl.cLog("&2 -Loading Servers");
		try {
			insertServers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		cl.cLog("&2 -Registering Listeners And Channels");
		registerListeners();
		
		cl.cLog("&2 -Registering Commands");
		registerCommands();
		
		if (warpsEnabled) {
			cl.cLog("&2 -Loading Warps");
			loadWarps();
		}
		
		if (portalsEnabled) {
			cl.cLog("&2 -Loading Portals");
			loadPortals();
		}
		
		if (signsEnabled) {
			cl.cLog("&2 -Loading Signs");
			loadSigns();
		}
		
		if (teleportsEnabled) {
			cl.cLog("&2 -Loading Teleports");
			loadTeleports();
		}
		
		if (chatEnabled) {
			cl.cLog("&2 -Loading Chat");
			try {
				loadChat();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (bansEnabled) {
			cl.cLog("&2 -Loading Bans");
			try {
				loadBans();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		cl.cLog("&2 -Loading Messages");
		loadMessages();

		if (gamemode) {
			loadGamemode();
		}
	}

	private void loadMessages() {
		String localepath = "/plugins/BungeeSuite/locale.yml";
		locale = new Config(localepath);
		
		NO_PERMISSION = locale.getString("NO_PERMISSION", ChatColor.RED + "You do not have permission to use that command");
		CHATSPY_TOGGLED = locale.getString("CHATSPY_TOGGLED", ChatColor.DARK_GREEN + "Chatspy toggled");
		BROADCAST_MESSAGE = locale.getString("BROADCAST_MESSAGE", ChatColor.AQUA + "[BROADCAST]:"
						+ ChatColor.GREEN + " %message");
		PLAYER_NOT_EXIST = locale.getString("PLAYER_NOT_EXIST", ChatColor.RED + "That player does not exist");
		PLAYER_NOT_ONLINE = locale.getString("PLAYER_NOT_ONLINE", ChatColor.RED + "That player is not online");
		DEFAULT_BAN_PLAYER = locale.getString("DEFAULT_BAN_PLAYER", "You have been banned!");
		DEFAULT_IPBAN_PLAYER = locale.getString("DEFAULT_IPBAN_PLAYER", ChatColor.GOLD + "[%player]" + ChatColor.DARK_GREEN
						+ " has been ip banned from the server");
		TEMP_BAN_BROADCAST = locale.getString("TEMP_BAN_BROADCAST", ChatColor.GOLD + "[%player]" 
						+ ChatColor.DARK_GREEN + "has been temporarily banned by" + ChatColor.GOLD 
						+ " %sender" + ChatColor.DARK_GREEN + " for %days:days %hours:hours %minutes:minutes");
		BAN_MESSAGE_BROADCAST = locale .getString("BAN_MESSAGE_BROADCAST", ChatColor.GOLD + "[%player]"
						+ ChatColor.DARK_GREEN + " has been banned from the server by %sender, reason: %message");
		IP_NOT_EXIST = locale.getString("IP_NOT_EXIST", ChatColor.RED + "That IP does not exist");
		PLAYER_UNBANNED = locale.getString("PLAYER_UNBANNED", ChatColor.DARK_GREEN + "%player has been unbanned!");
		IP_UNBANNED = locale.getString("IP_UNBANNED", ChatColor.DARK_GREEN + "IP has been unbanned");
		PLAYER_NOT_BANNED = locale.getString("PLAYER_NOT_BANNED", ChatColor.RED + "That player is not banned");
		DEFAULT_KICK_PLAYER = locale.getString("DEFAULT_KICK_PLAYER", ChatColor.RED
						+ "You have been kicked from the server by %sender");
		DEFAULT_KICK_BROADCAST = locale.getString("DEFAULT_KICK_BROADCAST", ChatColor.AQUA + "[%player]"
						+ ChatColor.GOLD + " has been kicked from the server by %sender for: %message");
		TELEPORTED_PLAYER_TO_TARGET = locale.getString(
				"TELEPORTED_PLAYER_TO_TARGET", ChatColor.DARK_GREEN
						+ "You have been teleported to %player");
		PLAYER_TELEPORTED_TO = locale.getString("PLAYER_TELEPORTED_TO", ChatColor.DARK_GREEN + "%sender has teleported to you");
		TELEPORT_REQUEST_HERE = locale.getString("TELEPORT_REQUEST_HERE", ChatColor.LIGHT_PURPLE
						+ "%player has requested you teleport to them. Type /tpaccept to accept");
		TELEPORT_REQUEST_TO = locale.getString("TELEPORT_REQUEST_TO", ChatColor.LIGHT_PURPLE
						+ "%player has requested to teleport to you. Type /tpaccept to accept");
		TELEPORTS_NOT_ENABLED = locale.getString("TELEPORTS_NOT_ENABLED", ChatColor.RED + "Teleports are not enabled for this world");
		TELEPORT_REQUEST_SENT = locale.getString("TELEPORT_REQUEST_SENT",ChatColor.DARK_GREEN + "TP request sent!");
		WARP_SPAWN_NOT_EXIST = locale.getString("WARP_SPAWN_NOT_EXIST", ChatColor.RED
						+ "The warp \"Spawn\" has not been created yet, please ask your admin to set the \"Spawn\" warp");
		WARP_NOT_EXIST = locale.getString("WARP_NOT_EXIST", ChatColor.RED + "That warp does not exist");
		WARP_DELETE_CONFIRM = locale.getString("WARP_DELETE_CONFIRM", ChatColor.DARK_GREEN + "Warp deleted");
		PORTAL_DELETE_CONFIRM = locale.getString("PORTAL_DELETE_CONFIRM", ChatColor.DARK_GREEN + "Portal deleted");
		PORTAL_NOT_EXIST = locale.getString("PORTAL_NOT_EXIST", ChatColor.RED + "That portal does not exist");
		PLAYER_REPLY_NONE = locale.getString("PLAYER_REPLY_NONE", ChatColor.RED + "You have no players to reply to");
		PLAYER_NICKNAME_CHANGE = locale.getString("PLAYER_NICKNAME_CHANGE", ChatColor.GOLD + "Your nickname has been changed to "
						+ ChatColor.DARK_GREEN + "%nickname" + ChatColor.GOLD + " by %sender");
		PLAYER_NICKNAMED = locale.getString("PLAYER_NICKNAMED", ChatColor.DARK_GREEN
						+ "%player's nickname has been changed to %nickname");
		PLAYER_ALL_MUTED = locale.getString("PLAYER_ALL_MUTED", ChatColor.RED + "All players have been temporarily muted");
		PLAYER_ALL_UNMUTED = locale.getString("PLAYER_ALL_UNMUTED", ChatColor.DARK_GREEN + "All players have been unmuted!");
		PLAYER_MUTE = locale.getString("PLAYER_MUTE", ChatColor.RED + "You have been muted!");
		PLAYER_MUTED = locale.getString("PLAYER_MUTED", ChatColor.RED + "Player has been muted");
		PLAYER_UNMUTE = locale.getString("PLAYER_UNMUTE", ChatColor.DARK_GREEN + "You have been unmuted!");
		PLAYER_UNMUTED = locale.getString("PLAYER_UNMUTED", ChatColor.DARK_GREEN + "Player has been unmuted!");
		PLAYER_INVITED = locale.getString("PLAYER_INVITED", ChatColor.DARK_GREEN
						+ "%player has been invited to join the channel %channel");
		PLAYER_INVITE = locale.getString("PLAYER_INVITE", ChatColor.DARK_GREEN
						+ "You have been invited by %sender to join the channel %channel. Type /join %channel");
		PLAYER_SENDING_CHAT = locale.getString("PLAYER_SENDING_CHAT", ChatColor.DARK_GREEN + "Sending messages in the channel "+ChatColor.DARK_AQUA+"%channel");
		PLAYER_SENDING_SERVER = locale.getString("PLAYER_SENDING_SERVER", ChatColor.DARK_GREEN + "Toggled display of server");
		CHANNEL_INVITE_NOPERM = locale.getString("CHANNEL_INVITE_NOPERM", ChatColor.RED
						+ "You do not have permission to invite players to the channel " + ChatColor.AQUA + "%channel");
		CHANNEL_NOT_INVITED = locale.getString("CHANNEL_NOT_INVITED", ChatColor.RED + "You are not invited to join the channel "
						+ ChatColor.AQUA + "%channel");
		CHANNEL_NOT_LEAVE_SERVER = locale.getString("CHANNEL_NOT_LEAVE_SERVER",ChatColor.RED
						+ "You are unable to leave this channel as it is a server channel");
		CHANNEL_TOGGLE_PERMISSION = locale.getString("CHANNEL_TOGGLE_PERMISSION", ChatColor.RED
						+ "You do not have permission to toggle to this channel");
		CHANNEL_NOT_LEAVE_OWNER = locale.getString("CHANNEL_NOT_LEAVE_OWNER", ChatColor.RED
						+ "You are unable to leave the channel %channel as you are the owner");
		CHANNEL_IS_MEMBER = locale.getString("CHANNEL_IS_MEMBER", ChatColor.RED + "%player is already a member of the channel %channel");
		CHANNEL_NOT_MEMBER = locale.getString("CHANNEL_NOT_MEMBER", ChatColor.RED + "That player is not a member of the channel");
		CHANNEL_NO_PERMISSION = locale.getString("CHANNEL_NO_PERMISSION", ChatColor.RED + "You do not have permission to edit this channel");
		CHANNEL_NOT_EXIST = locale.getString("CHANNEL_NOT_EXIST", ChatColor.RED + "That channel does not exist!");
		CHANNEL_ALREADY_EXISTS = locale.getString("CHANNEL_ALREADY_EXISTS", ChatColor.RED+ "That channel already exists");
		CHANNEL_CREATE_CONFIRM = locale.getString("CHANNEL_CREATE_CONFIRM", ChatColor.DARK_GREEN + "Channel %channel created!");
		CHANNEL_DELETE_CONFIRM = locale.getString("CHANNEL_DELETE_CONFIRM", ChatColor.DARK_GREEN + "Channel deleted!");
		CHANNEL_NEW_OWNER = locale.getString("CHANNEL_NEW_OWNER", ChatColor.DARK_GREEN + "You are now the owner of the channel %channel");
		CHANNEL_PLAYER_JOINED = locale.getString("CHANNEL_PLAYER_JOINED", ChatColor.GRAY + "%player has joined the channel %channel");
		CHANNEL_WELCOME = locale.getString("CHANNEL_WELCOME", ChatColor.GRAY + "Welcome to the channel %channel");
		CHANNEL_KICK_PLAYER = locale.getString("CHANNEL_KICK_PLAYER", ChatColor.RED + "You have been kicked from the channel %channel");
		CHANNEL_PLAYER_LEAVE = locale.getString("CHANNEL_PLAYER_LEAVE", ChatColor.GRAY + "%player has left the channel %channel");
		CHANNEL_TOO_MANY = locale.getString("CHANNEL_TOO_MANY", ChatColor.RED + " Player owns to many channels");
		NO_PERMISSION_COLOR = locale.getString("NO_PERMISSION_COLOR", ChatColor.RED + "You do not have permission to use a colored nickname");
		PLAYER_IGNORING = locale.getString("PLAYER_IGNORING", ChatColor.RED + "That player is ignoring you!");
		PLAYER_IGNORED = locale.getString("PLAYER_IGNORED", ChatColor.DARK_GREEN + "Player ignored");
		PLAYER_UNIGNORED = locale.getString("PLAYER_UNIGNORED", ChatColor.DARK_GREEN + "Player unignored");
		
		locale.save();
	}

	private void loadGamemode() {
		gamemodes = new HashMap<ProxiedPlayer, String>();
	}

	private void loadBans() throws SQLException {
		IPbans = new HashSet<String>();
		playerBans = new HashMap<String, Calendar>();
		this.utils.loadBans();
	}

	private void loadChat() throws SQLException {
		onlinePlayers = new HashMap<String, ChatPlayer>();
		chatChannels = new HashMap<String, ChatChannel>();
		chatSpying = new HashSet<String>();
		
		String configpath = "/plugins/BungeeSuite/channelFormats.yml";
		channelConfig = new Config(configpath);
		
		utils.loadChannels();
		
		for (ChatChannel data : chatChannels.values()) {
			String format = channelConfig.getString("BungeeSuite.Chat.Channels." + data.getName(), data.getFormat());
			
			if (!format.equals(data.getFormat()))
				getUtilities().reformatChannel(data.getName(), format);
		}
		
		channelConfig.save();
		
		String prefixpath = "/plugins/BungeeSuite/prefixes.yml";
		this.prefix = new HashMap<String, String>();
		prefixConfig = new Config(prefixpath);
		prefix.put("owner", prefixConfig.getString("Prefix.owner", ChatColor.RED + "[Owner]"));
		prefix.put("admin", prefixConfig.getString("Prefix.admin", ChatColor.RED + "[Admin]"));
		prefix.put("mod", prefixConfig.getString("Prefix.mod", ChatColor.RED + "[Mod]"));
		prefix.put("tmod", prefixConfig.getString("Prefix.tmod", ChatColor.DARK_PURPLE + "[TMod]"));
		
		getProxy().registerChannel(ChatChannel.CHANNEL_OUT_NAME);
	}

	private void loadTeleports() {
		teleportsPending = new HashMap<ProxiedPlayer, ProxiedPlayer>();
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
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeSuiteCommand(this));
		
		if (portalsEnabled) {
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new SetPortalCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new DeletePortalCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ListPortalsCommand(this));
		}
		
		if (warpsEnabled) {
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ListWarpsCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new SetWarpCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new DeleteWarpCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand(this));
			if (spawnWarpEnabled)
				ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpSpawnCommand(this));
		}
		
		if (teleportsEnabled) {
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new TPCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new TPAHereCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new TPACommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new TPAcceptCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new TPDenyCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new TPAllCommand(this));
		}
		
		if (chatEnabled) {
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new GlobalCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ServerCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteAllCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new BroadcastCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new NicknameCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new MessageCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReplyCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ToggleCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new AcceptCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new InviteCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new JoinCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new CreateChannelCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ChannelCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new DeleteChannelCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new LeaveChannelCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ChatSpyCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new DisplayServerCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new IgnoreCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new IgnoringCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new ListChannelsCommand(this));
		}
		
		if (bansEnabled) {
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickAllCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new TempBanCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanIPCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand(this));
			ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanIPCommand(this));
		}
	}

	private void registerListeners() {
		proxy.getPluginManager().registerListener(this, new PluginMessageListener(this));
		proxy.registerChannel("BungeeSuite");
		proxy.registerChannel("BungeeSuiteMC");
		
		proxy.getPluginManager().registerListener(this, new ServerLoginLogout(this));
		
		if (loginMessagesEnabled)
			proxy.getPluginManager().registerListener(this, new LoginMessages(this));
		
		proxy.getPluginManager().registerListener(this, new ChatListener(this));

		if (bansEnabled)
			proxy.getPluginManager().registerListener(this, new BanListener(this));
	}

	private void loadSigns() {
	}

	private void loadPortals() {
	}

	private void loadWarps() {
		warpList = new HashMap<String, Warp>();
		warped = new HashMap<String, Warp>();
	}

	private void loadVariables() throws SQLException {
		proxy = ProxyServer.getInstance();
		sql = new SQL(url, port, database, username, password);
		utils = new Utilities(this);
		this.allMuted = false;
	}

	private void insertServers() throws SQLException {
		for (String data : proxy.getServers().keySet()) {
			if (!utils.serverExists(data)) {
				utils.createServer(data);
				cl.cLog("&2 Server " + data + " added to database!");
			}
		}
		
		cl.cLog("&6     -" + proxy.getServers().values().size() + " Servers loaded");
	}

	private void loadConfig() {
		String configpath = "/plugins/BungeeSuite/config.yml";
		config = new Config(configpath);
		
		username = config.getString("BungeeSuite.SQL.Username", "root");
		password = config.getString("BungeeSuite.SQL.Password", "password");
		database = config.getString("BungeeSuite.SQL.Database", "minecraft");
		url = config.getString("BungeeSuite.SQL.URL", "localhost");
		port = config.getString("BungeeSuite.SQL.Port", "3306");
		
		warpsEnabled = config.getBoolean("BungeeSuite.Warps.Enabled", true);
		spawnWarpEnabled = config.getBoolean("BungeeSuite.Warps.UseSpawn", true);
		
		signsEnabled = config.getBoolean("BungeeSuite.Signs.Enabled", true);
		loginMessagesEnabled = config.getBoolean("BungeeSuite.LoginMessages.enabled", true);
		motdColored = config.getBoolean("BungeeSuite.Signs.MOTDColored", true);
		motdFormatOnline = config.getString("BungeeSuite.Signs.MOTDFormat.Online", "&2[%server]&0:%motd");
		motdFormatOffline = config.getString("BungeeSuite.Signs.MOTDFormat.Offline", "&4Server is offline");
		playerCountFormatOnline = config.getString("BungeeSuite.Signs.PlayerCount.Online", "&2[%server],Players,%players, ");
		playerCountFormatOnlineClick = config.getString("BungeeSuite.Signs.PlayerCount.OnlineClick", "&2[%server]players");
		playerCountFormatOffline = config.getString("BungeeSuite.Signs.PlayerCount.Offline", "&4[%server],Players,&4Offline, ");
		playerCountFormatOfflineClick = config.getString("BungeeSuite.Signs.PlayerCount.OfflineClick", "&4%server is offline");
		portalFormatOnline = config.getString("BungeeSuite.Signs.Portal.Online", "&2[Portal],,%server, ");
		portalFormatOffline = config.getString("BungeeSuite.Signs.Portal.Offline","&4[Portal],%server,&4Offline, ");
		portalFormatOfflineClick = config.getString("BungeeSuite.Signs.Portal.OfflineClick", "&4Server is offline");
		
		portalsEnabled = config.getBoolean("BungeeSuite.Portals.Enabled", true);
		teleportsEnabled = config.getBoolean("BungeeSuite.Teleports.Enabled", true);
		String bt[] = config.getString("BungeeSuite.Teleports.BlockTeleportsInServers", "Towny").split(",");
		blockedTeleports = new HashSet<String>();
		for (String data : bt)
			blockedTeleports.add(data);
		
		chatEnabled = config.getBoolean("BungeeSuite.Chat.Enabled", true);
		logChat = config.getBoolean("BungeeSuite.Chat.LogChat", true);
		formatChat = config.getBoolean("BungeeSuite.Chat.FormatChat", true);
		useVault = config.getBoolean("BungeeSuite.Chat.UseVault", false);
		channelMessages = config.getBoolean("BungeeSuite.Chat.ChannelMessages", true);
		globalDefault = config.getBoolean("BungeeSuite.Chat.globalDefault", true);
		globalToggleable = config.getBoolean("BungeeSuite.Chat.GlobalIsToggleable", true);
		maxCustomChannels = config.getInt("BungeeSuite.Chat.MaxCustomChannels", 1);
		
		ignoreServers = new HashSet<String>();
		String ignore[] = config.getString("BungeeSuite.Chat.IgnoreServerChat", "Towny,Heroes").split(",");
		for (String data : ignore)
			ignoreServers.add(data);
		
		serverNames = new HashMap<String, String>();
		String[] sn = config.getString("BungeeSuite.Chat.ReplaceServers", "Spawn-S,Creative-C").split(",");
		for (String data : sn) {
			String[] s = data.split("-");
			serverNames.put(s[0], s[1]);
		}
		
		replacementWords = new HashMap<String, String>();
		String[] rw = config.getString("BungeeSuite.Chat.ReplaceWords", "blood-&4Blood&f").split(",");
		for (String data : rw) {
			String[] s = data.split("-");
			replacementWords.put(s[0], s[1]);
		}
		
		defaultCustomChannelFormat = config.getString("BungeeSuite.Chat.defaultServerChannelFormat",
				"&e[%cname]&f%prefix&f%player&f: &7%message");
		defaultServerChannelFormat = config.getString("BungeeSuite.Chat.defaultCustomChannelFormat",
				"&9[%cname]&f%player: &7%message");

		bansEnabled = config.getBoolean("BungeeSuite.Banning", true);
		config.save();
		setupCommandsConfig();
	}

	public void setupCommandsConfig() {
		String configpath = "/plugins/BungeeSuite/commands.yml";
		commands = new Config(configpath);

		bungeeSuiteCommand = commands.getString("BungeeSuite.Commands.bungeesuite", "bs");

		/* Banning Commands */
		ban = commands.getString("BungeeSuite.Commands.ban", "ban");
		ipban = commands.getString("BungeeSuite.Commands.ipban", "ipban");
		kick = commands.getString("BungeeSuite.Commands.kick", "kick");
		kickall = commands.getString("BungeeSuite.Commands.kickall", "kickall");
		pardon = commands.getString("BungeeSuite.Commands.pardon", "pardon");
		tban = commands.getString("BungeeSuite.Commands.tban", "tban");
		tempban = commands.getString("BungeeSuite.Commands.tempban", "tempban");
		unban = commands.getString("BungeeSuite.Commands.unban", "unban");
		unbanip = commands.getString("BungeeSuite.Commands.unbanip", "unbanip");

		/* Chat Commands */
		cignore = commands.getString("BungeeSuite.Commands.ignore", "ignore");
		ignoring = commands.getString("BungeeSuite.Commands.ignoring", "ignoring");
		broadcast = commands.getString("BungeeSuite.Commands.broadcast", "broadcast");
		g = commands.getString("BungeeSuite.Commands.g", "g");
		global = commands.getString("BungeeSuite.Commands.global", "global");
		w = commands.getString("BungeeSuite.Commands.w", "world");
		world = commands.getString("BungeeSuite.Commands.world", "world");
		s = commands.getString("BungeeSuite.Commands.s", "s");
		server = commands.getString("BungeeSuite.Commands.server", "server");
		mute = commands.getString("BungeeSuite.Commands.mute", "mute");
		muteAll = commands.getString("BungeeSuite.Commands.muteall", "muteall");
		nick = commands.getString("BungeeSuite.Commands.nick", "nick");
		nickname = commands.getString("BungeeSuite.Commands.nickname", "nickname");
		msg = commands.getString("BungeeSuite.Commands.msg", "msg");
		message = commands.getString("BungeeSuite.Commands.message", "message");
		whisper = commands.getString("BungeeSuite.Commands.whisper", "whisper");
		r = commands.getString("BungeeSuite.Commands.r", "r");
		reply = commands.getString("BungeeSuite.Commands.reply", "reply");
		t = commands.getString("BungeeSuite.Commands.t", "t");
		tell = commands.getString("BungeeSuite.Commands.tell", "tell");
		toggle = commands.getString("BungeeSuite.Commands.toggle", "toggle");
		invite = commands.getString("BungeeSuite.Commands.invite", "invite");
		accept = commands.getString("BungeeSuite.Commands.accept", "accept");
		join = commands.getString("BungeeSuite.Commands.join", "join");
		create = commands.getString("BungeeSuite.Commands.create", "create");
		createChannel = commands.getString( "BungeeSuite.Commands.createchannel", "createchannel");
		channel = commands.getString("BungeeSuite.Commands.channel", "channel");
		c = commands.getString("BungeeSuite.Commands.c", "c");
		delete = commands.getString("BungeeSuite.Commands.delete", "delete");
		deleteChannel = commands.getString( "BungeeSuite.Commands.deletechannel", "deletechannel");
		leave = commands.getString("BungeeSuite.Commands.leave", "leave");
		leaveChannel = commands.getString("BungeeSuite.Commands.leavechannel", "leavechannel");
		server = commands.getString("BungeeSuite.Commands.server", "server");
		s = commands.getString("BungeeSuite.Commands.s", "s");
		chatSpy = commands.getString("BungeeSuite.Commands.chatspy", "chatspy");
		displayServer = commands.getString( "BungeeSuite.Commands.displayserver", "displayserver");
		channels = commands.getString( "BungeeSuite.Commands.channels", "channels");
		channelslist = commands.getString( "BungeeSuite.Commands.channelslist", "channelslist");

		/* Portal Commands */
		setPortal = commands.getString("BungeeSuite.Commands.setportal", "setportal");
		delPortal = commands.getString("BungeeSuite.Commands.delportal", "delportal");
		listPortals = commands.getString("BungeeSuite.Commands.listportals", "listportals");
		portalsc = commands.getString("BungeeSuite.Commands.portals", "portals");

		/* Teleport Commands */
		tp = commands.getString("BungeeSuite.Commands.tp", "tp");
		tpa = commands.getString("BungeeSuite.Commands.tpa", "tpa");
		tpAccept = commands.getString("BungeeSuite.Commands.tpaccept", "tpaccept");
		tpDeny = commands.getString("BungeeSuite.Commands.tpdeny", "tpdeny");
		tpaHere = commands.getString("BungeeSuite.Commands.tpahere", "tpahere");
		tpAll = commands.getString("BungeeSuite.Commands.tpall", "tpall");

		/* Warp Commands */
		delWarp = commands.getString("BungeeSuite.Commands.delwarp", "delwarp");
		setWarp = commands.getString("BungeeSuite.Commands.setwarp", "setwarp");
		spawn = commands.getString("BungeeSuite.Commands.spawn", "spawn");
		warp = commands.getString("BungeeSuite.Commands.warp", "warp");
		warpsList = commands.getString("BungeeSuite.Commands.warplist", "warplist");
		warpsc = commands.getString("BungeeSuite.Commands.warps", "warps");
		
		commands.save();
	}

	public Config getConfig() {
		return config;
	}

	public SQL getSQL() {
		return sql;
	}

	public Utilities getUtilities() {
		return utils;
	}

	public ProxyServer getProxy() {
		return proxy;
	}

	public ChatPlayer getChatPlayer(String player) {
		return onlinePlayers.get(player);
	}

	public ChatChannel getChannel(String string) {
		return this.chatChannels.get(string);

	}

	public void reload() {
		config.load();
		channelConfig.load();
		locale.load();
		this.prefixConfig.load();
	}

	public HashMap<String, ChatChannel> getChatChannels() {
		return new HashMap<>(chatChannels);
	}
}
