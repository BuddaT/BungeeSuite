package com.mcdimensions.BungeeSuite.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.yaml.snakeyaml.Yaml;

import com.mcdimensions.BungeeSuite.BungeeSuite;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Config {

	private Yaml config;
	private File file;
	private Map<String, String> data;
	BungeeSuite plugin;

	public Config(BungeeSuite bungeeSuite) {
		plugin = bungeeSuite;
		file = new File("plugins" + File.separator
				+ plugin.getDescription().getName() + File.separator
				+ "config.yml");
		config = new Yaml();
		loadYAML();
	}

	/**
	 * Initialize YAML
	 */
//	@SuppressWarnings("unchecked")
	public void loadYAML() {
		try {
			if(!file.exists()){
			file.getParentFile().mkdirs();
			file.createNewFile();
			}
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger()
					.log(Level.WARNING, "Could not create config file", e);
		}
		try {
			FileReader rd = new FileReader(file);
			data = config.loadAs(rd, Map.class);
		} catch (IOException ex) {
			ProxyServer
					.getInstance()
					.getLogger()
					.log(Level.WARNING, "Could not load BungeeSuite config", ex);
		}
		if (data == null) {
			data = new ConcurrentHashMap<String, String>();
		} else {
			data = new ConcurrentHashMap<String, String>(data);
		}
	}

	/**
	 * Save YAML to file
	 */
	public void saveYAML() {
		try {
			FileWriter wr = new FileWriter(file);
			config.dump(data, wr);
		} catch (IOException ex) {
			ProxyServer
					.getInstance()
					.getLogger()
					.log(Level.WARNING, "Could not load BungeeSuite config", ex);
		}
	}

	/**
	 * Get a key value from YAML
	 * 
	 * @param key
	 *            Key name
	 * @return Value of key
	 */
	public String get(String key) {
		return (String) data.get(key);
	}

	/**
	 * Put a key value into YAML
	 * 
	 * @param key
	 *            Key name
	 * @param value
	 *            Key value
	 */
	public void set(String key, String value) {
		data.put(key, value);
	}
	public void setDefault(String key, String value) {
		if(data.containsKey(key)){
			return;
		}else{
		data.put(key, value);
		}
	}
	public boolean containsKey(String key) {
		return data.containsKey(key);
	}

	public boolean containsValue(String value) {
		return data.containsValue(value);
	}
}