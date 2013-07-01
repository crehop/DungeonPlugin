 package me.ThaH3lper.com;
 
 import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.ThaH3lper.com.Crehop.EventListener;
import me.ThaH3lper.com.Grades.Grade;
import me.ThaH3lper.com.Grades.Wave;
import me.ThaH3lper.com.Portals.Portal;
import me.ThaH3lper.com.Reward.PlayerTimes;
import me.ThaH3lper.com.Reward.Rewards;
import me.ThaH3lper.com.dungeon.SaveLoad;
import me.ThaH3lper.com.dungeon.SpawnEntitys;
import me.ThaH3lper.com.dungeon.TempletFloor;
import me.ThaH3lper.com.dungeon.Tower;
import me.ThaH3lper.com.dungeon.commands.BlockListener;
import me.ThaH3lper.com.dungeon.commands.CommandHandle;
import me.ThaH3lper.com.party.CommandParty;
import me.ThaH3lper.com.party.PartyListener;

import org.bukkit.Location;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

 public class Dungeon extends JavaPlugin
 {
   public Logger logger = Logger.getLogger("Minecraft");
   public PlayerListener pl = new PlayerListener(this);
   public PartyListener pal = new PartyListener();
   public BlockListener blocklistener = new BlockListener(this);
   
   public static Dungeon plugin;
   
   public List<TempletFloor> templetFloorList = new ArrayList<TempletFloor>();
   public List<Tower> towerList = new ArrayList<Tower>();
   public List<Portal> portalList = new ArrayList<Portal>();
   public List<Wave> waveList = new ArrayList<Wave>();
   
   public DungeonHandler dungeonHandler;
   public SaveLoad saveload;
   public Save settings, save, options, waves, player;
   public SpawnEntitys spawnEntitys;
   public PlayerTimes playerTimes;
   
   public Rewards rewEasy, rewNormal, rewHard, rewTop;
   public Grade graEasy, graNormal, graHard;
   
   public Location exit;
 
   public void onDisable()
   {
     PluginDescriptionFile pdfFile = getDescription();
     this.logger.info(pdfFile.getName() + " Has Been Disabled!");
     
     saveload.SaveAll();
   }
 
   public void onEnable()
   {	 
     getServer().getPluginManager().registerEvents(this.pl, this);
     getServer().getPluginManager().registerEvents(this.pal, this);
     getServer().getPluginManager().registerEvents(this.blocklistener, this);
     getServer().getPluginManager().registerEvents(new EventListener(this), this);
     
     PluginDescriptionFile pdfFile = getDescription();
     this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
     
     settings = new Save(this, "Settings.yml");
     options = new Save(this, "Options.yml");
     save = new Save(this, "Save.yml");
     waves = new Save(this, "Waves.yml");
     player = new Save(this, "PlayerData.yml");
     
     dungeonHandler = new DungeonHandler(this);
     dungeonHandler.load();
     spawnEntitys = new SpawnEntitys(this);
     
     saveload = new SaveLoad(this);
     
     rewEasy = new Rewards("Easy.Reward", this);
     rewNormal = new Rewards("Normal.Reward", this);
     rewHard = new Rewards("Hard.Reward", this);
     //rewTop = new Rewards("TopKiller", this);
     
     LoadGrades();
     playerTimes = new PlayerTimes(this);
     
     getCommand("dungeon").setExecutor(new CommandHandle(this));
     getCommand("party").setExecutor(new CommandParty(this));
     
     for(Tower t : towerList)
     {
    	 if(t.hasTower)
    	 	t.Generate(0, 1);
     }
   }
   
   public void LoadGrades()
   {
	   graEasy = new Grade((float)options.getCustomConfig().getDouble("Easy.Boost"), options.getCustomConfig().getInt("Easy.Levels"), options.getCustomConfig().getStringList("Easy.Level"), options.getCustomConfig().getInt("Easy.Lives"), this);
	   graNormal = new Grade((float)options.getCustomConfig().getDouble("Normal.Boost"), options.getCustomConfig().getInt("Normal.Levels"), options.getCustomConfig().getStringList("Normal.Level"), options.getCustomConfig().getInt("Normal.Lives"), this);
	   graHard = new Grade((float)options.getCustomConfig().getDouble("Hard.Boost"), options.getCustomConfig().getInt("Hard.Levels"), options.getCustomConfig().getStringList("Hard.Level"), options.getCustomConfig().getInt("Hard.Lives"), this);
   }
 }