package me.ThaH3lper.com;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Save {
		
	private FileConfiguration DataConfig = null;
	private File data = null;
	
	private Dungeon dun;
	private String file;
	private File thefile;
	
	public Save(Dungeon dun, String newfile)
	{
		this.dun = dun;
		file = newfile;
		thefile = new File(dun.getDataFolder(), newfile);
		if(thefile.exists())
		{
			data = thefile;
		}
		reloadCustomConfig();
		saveCustomConfig();
	}
	public void reloadCustomConfig() {
	    if (data == null) 
	    {
	    	data = new File(dun.getDataFolder(), file);
	    	DataConfig = YamlConfiguration.loadConfiguration(data);
	    	InputStream defConfigStream = dun.getResource(file);
	    	if (defConfigStream != null) 
	    	{
	    		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	    		DataConfig.setDefaults(defConfig);
	    	}
	    	getCustomConfig().options().copyDefaults(true);
	    	dun.logger.info(file + " did not exist! Generated a new one!");
	    }
	    else
	    {
	    	DataConfig = YamlConfiguration.loadConfiguration(data);
	    }
	}

	public FileConfiguration getCustomConfig() {
	    if (DataConfig == null) {
	        reloadCustomConfig();
	    }
	    return DataConfig;
	}

	public void saveCustomConfig() {
	    if (DataConfig == null || data == null) {
	    return;
	    }
	    try {
	        getCustomConfig().save(data);
	    } catch (IOException ex) {
	        dun.getLogger().log(Level.SEVERE, "Could not save config to " + data, ex);
	    }
	    
	}
}
