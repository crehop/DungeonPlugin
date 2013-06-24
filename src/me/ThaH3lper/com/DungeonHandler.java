 package me.ThaH3lper.com;
 
 import java.util.ArrayList;
 import java.util.List;
 import org.bukkit.configuration.file.FileConfiguration;
 import org.bukkit.entity.Player;
 
 public class DungeonHandler
 {
   private static List<DungeonPlayer> players = new ArrayList<DungeonPlayer>();
   private static long defencePotionCD;
   private static long offencePotionCD;
   private static long pvpTimer;
 
   private Dungeon dun;
   
   public DungeonHandler(Dungeon dun)
   {
	   this.dun = dun;
   }
   
   public static boolean addPlayer(DungeonPlayer dp)
   {
     return players.add(dp);
   }
 
   public static boolean removePlayer(Player p)
   {
     for (DungeonPlayer dp : players)
     {
       if (dp.getName().equalsIgnoreCase(p.getName()))
         return players.remove(dp);
     }
     return false;
   }
 
   public static List<DungeonPlayer> getPlayers()
   {
     return players;
   }
 
   public static DungeonPlayer getPlayer(Player p)
   {
     for (DungeonPlayer dp : players)
     {
       if (dp.getName().equalsIgnoreCase(p.getName()))
         return dp;
     }
     return null;
   }
 
   public void load()
   {
     players.clear();
     for (Player p : dun.getServer().getOnlinePlayers())
     {
       if (p != null)
       {
         players.add(new DungeonPlayer(p, dun));
       }
     }
     defencePotionCD = dun.settings.getCustomConfig().getLong("DefencePotionCD");
     offencePotionCD = dun.settings.getCustomConfig().getLong("OffencePotionCD");
     pvpTimer = dun.settings.getCustomConfig().getLong("PVPTimer");
   }
 
   public static long getDefencePotionCD()
   {
     return defencePotionCD;
   }
 
   public static long getOffencePotionCD()
   {
     return offencePotionCD;
   }
 
   public static long getPvpTimer()
   {
     return pvpTimer;
   }
 }